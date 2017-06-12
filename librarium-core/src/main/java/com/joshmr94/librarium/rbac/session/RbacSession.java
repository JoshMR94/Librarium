package com.joshmr94.librarium.rbac.session;

import com.joshmr94.librarium.comun.service.ServiceRuntimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.apache.log4j.Logger;

import com.joshmr94.librarium.rbac.model.Operacion;
import com.joshmr94.librarium.rbac.model.Permiso;
import com.joshmr94.librarium.rbac.model.Recurso;
import com.joshmr94.librarium.rbac.model.Rol;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author joshmr94
 */
public class RbacSession {

    private static final Logger LOG = Logger.getLogger(RbacSession.class);

    static final Object syncObject = new Object();

    static final Map<String, Map<String, Set<String>>> cacheRolRecursoOperacion = new HashMap<>();
    static final Map<String, Pattern> cachePatterns = new HashMap<>();
    static final Map<String, Set<String>> cacheRolSubroles = new HashMap<>();
    static long cacheTime = -1;
    static volatile long cacheDirtyTime = 0;
    
    boolean usingRegularExpressions;

    public RbacSession() {
    }

    public RbacSession(boolean usingRegularExpressions) {
        this.usingRegularExpressions = usingRegularExpressions;
    }

    public boolean isUsingRegularExpressions() {
        return usingRegularExpressions;
    }

    public void setUsingRegularExpressions(boolean usingRegularExpressions) {
        this.usingRegularExpressions = usingRegularExpressions;
    }

    public boolean compruebaPermiso(Rol rol, Operacion operacion) {
        return compruebaPermiso(
                new String[]{rol.getDescripcion()},
                operacion.getRecurso().getDescripcion(),
                operacion.getOperacion());
    }

    public boolean compruebaPermiso(Set<Rol> roles, Operacion operacion) {
        ArrayList<String> list = new ArrayList<>();
        for (Rol rol : roles) {
            list.add(rol.getDescripcion());
        }
        return compruebaPermiso(
                list.toArray(new String[list.size()]),
                operacion.getRecurso().getDescripcion(),
                operacion.getOperacion());
    }

    public boolean compruebaPermiso(String rol, String recurso, String operacion) {
        return compruebaPermiso(new String[]{rol}, recurso, operacion);
    }

    public boolean compruebaPermiso(String[] roles, String recurso, String operacion) {
        if (cacheDirtyTime > cacheTime) {
            //refrescamos
            synchronized (syncObject) {
                if (cacheDirtyTime > cacheTime) {
                    refreshCache();
                }
                cacheTime = System.currentTimeMillis();
                //los siguientes hilos bloqueados no refrescarán la cache (no se cumplirá cacheDirtyTime > cacheTime), con lo que optimizamos la ejecución
            }
        }

        for (String rol : roles) {
            if (compruebaPermisoRecursivo(rol, recurso, operacion)) {
                return true;
            }
        }
        return false;
    }

    public void modeloActualizado() {
        cacheDirtyTime = System.currentTimeMillis();
    }

    /**
     * Recorrido recursivo por el árbol de roles. Se controla si hay ciclos en
     * el árbol para lanzar una excepción.
     *
     * @param rol descriptor del rol objeto de la consulta
     * @param recurso descriptor del recurso objeto de la consulta
     * @param operacion descriptor de la operación objeto de la consulta
     * @param rolesRecorridos conjunto de roles recorridos durante la consulta,
     * para evitar consultar permisos ya tratados
     * @param rutaRecorrida pila con la ruta de roles recorrida, usada para
     * detectar ciclos
     * @return "true" si el rol indicado puede realizar la operación indicada
     * sobre el recurso indicado.
     */
    private boolean compruebaPermisoRecursivo(
            String rol, String recurso, String operacion,
            Set<String> rolesRecorridos,
            Stack<String> rutaRecorrida) {

        //comprobamos ciclos (siempre antes que rolesRecorridos)
        if (rutaRecorrida.contains(rol)) {
            throw new ServiceRuntimeException("El rol '" + rol + "' forma un ciclo en el árbol de roles: " + HelperRbac.imprimeCiclo(rutaRecorrida, rol));
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Iteración en nodo: " + HelperRbac.imprimeCiclo(rutaRecorrida, rol));
        }

        //comprobamos si ya se ha recorrido en otro subárbol
        if (rolesRecorridos.contains(rol)) {
            return false;
        } //ya recorrido y no devolvió "true"

        if (checkRecursoOperacion(rol, recurso, operacion)) {
            return true;
        }

        //acabada comprobación directa
        rolesRecorridos.add(rol);

        if (checkSubRoles(rol, rutaRecorrida, recurso, operacion, rolesRecorridos)) {
            return true;
        }

        return false; //ningún subrol ha devuelto "true"
    }

    private boolean checkRecursoOperacion(String rol, String recurso, String operacion) {
        //Comprobamos si el rol tiene permiso directo sobre la operación
        Map<String, Set<String>> cacheRecursoOperacion = cacheRolRecursoOperacion.get(rol);
        if (cacheRecursoOperacion != null) {
            //comprobamos siempre por igualdad, se usen expresiones regulares o no
            Set<String> cacheOperaciones = cacheRecursoOperacion.get(recurso);
            if (cacheOperaciones != null && cacheOperaciones.contains(operacion)) {
                return true;
            }
            if (checkRegularExpressions(cacheRecursoOperacion, recurso, operacion)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkSubRoles(String rol, Stack<String> rutaRecorrida, String recurso, String operacion, Set<String> rolesRecorridos) {
        //probamos con los permisos de los subroles
        if (cacheRolSubroles.containsKey(rol)) {
            Set<String> subroles = cacheRolSubroles.get(rol);
            rutaRecorrida.push(rol);
            if (subroles.stream().map((subrol)
                    -> compruebaPermisoRecursivo(subrol, recurso,
                            operacion, rolesRecorridos,
                            rutaRecorrida)).anyMatch((resul) -> resul)) {
                return true;
            }
            rutaRecorrida.pop();
        }
        return false;
    }

    private boolean checkRegularExpressions(Map<String, Set<String>> cacheRecursoOperacion, String recurso, String operacion) {
        if (isUsingRegularExpressions()) {
            for (Map.Entry<String, Set<String>> recursoOperaciones : cacheRecursoOperacion.entrySet()) {
                Pattern recursoPattern = cachePatterns.get(recursoOperaciones.getKey());
                if (checkPattern(recursoPattern, recurso, recursoOperaciones, operacion)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkPattern(Pattern recursoPattern, String recurso, Map.Entry<String, Set<String>> recursoOperaciones, String operacion) {
        if ((recursoPattern != null && recursoPattern.matcher(recurso).matches()) || recursoOperaciones.getKey().equals(recurso)) {
            for (String oper : recursoOperaciones.getValue()) {
                //recorremos todos los patrones de operaciones para la expresion regular de recurso
                Pattern operacionPattern = cachePatterns.get(oper);
                if (operacionPattern != null && operacionPattern.matcher(operacion).matches()) {
                    return true;
                } else if (operacion.equals(oper)) {
                    //comprobamos igualdad exacta
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean compruebaPermisoRecursivo(String rol, String recurso, String operacion) {
        return compruebaPermisoRecursivo(rol, recurso, operacion, new HashSet<>(), new Stack<>());
    }

    protected void refreshCache() {
        cacheRolRecursoOperacion.clear();
        cachePatterns.clear();
        cacheRolSubroles.clear();
        
        PermisoSession permisoSession = new PermisoSession();

        List<Permiso> permisos = permisoSession.getAll();
        for (Permiso permiso : permisos) {
            Rol rol = permiso.getRol();
            Operacion operacion = permiso.getOperacion();
            Recurso recurso = operacion.getRecurso();

            refreshSubRolesCache(rol);

            Map<String, Set<String>> cacheRecursoOperaciones
                    = cacheRolRecursoOperacion.get(rol.getDescripcion());
            if (cacheRecursoOperaciones == null) {
                cacheRecursoOperaciones = new HashMap<>();
                cacheRolRecursoOperacion.put(rol.getDescripcion(), cacheRecursoOperaciones);
            }

            Set<String> cacheOperaciones = cacheRecursoOperaciones.get(recurso.getDescripcion());
            if (cacheOperaciones == null) {
                cacheOperaciones = new HashSet<>();
                cacheRecursoOperaciones.put(recurso.getDescripcion(), cacheOperaciones);
            }
            cacheOperaciones.add(operacion.getOperacion());

            checkRegularExpressionsPatterns(recurso, operacion);
        }
    }

    private void checkRegularExpressionsPatterns(Recurso recurso, Operacion operacion) throws PatternSyntaxException {
        //patrones de expresiones regulares
        if (!cachePatterns.containsKey(recurso.getDescripcion())) {
            try {
                cachePatterns.put(recurso.getDescripcion(), Pattern.compile(recurso.getDescripcion()));
            } catch (PatternSyntaxException e) {
                //ignoro si no es una expresión regular
                LOG.warn(String.format("'%s' no es un patrón válido para una expresión regular (recursos)", recurso.getDescripcion()));
                throw e;
            }
        }
        if (!cachePatterns.containsKey(operacion.getOperacion())) {
            try {
                cachePatterns.put(operacion.getOperacion(), Pattern.compile(operacion.getOperacion()));
            } catch (PatternSyntaxException e) {
                //ignoro si no es una expresión regular
                LOG.warn(String.format("'%s' no es un patrón válido para una expresión regular (operaciones)", recurso.getDescripcion()));
                throw e;
            }
        }
    }

    private void refreshSubRolesCache(Rol rol) {
        //subroles
        Set<String> cacheSubroles = cacheRolSubroles.get(rol.getDescripcion());
        if (cacheSubroles == null) {
            cacheSubroles = new HashSet<>();
            cacheRolSubroles.put(rol.getDescripcion(), cacheSubroles);
            for (Rol subrol : rol.getSubroles()) {
                cacheSubroles.add(subrol.getDescripcion());
            }
        }
    }
    
}
