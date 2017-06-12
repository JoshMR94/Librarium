package com.joshmr94.librarium.rbac.session;

import com.joshmr94.librarium.comun.service.CommonSession;
import com.joshmr94.librarium.comun.service.ServiceRuntimeException;
import java.util.List;
import java.util.Stack;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.log4j.Logger;

import com.joshmr94.librarium.rbac.model.Permiso;
import com.joshmr94.librarium.rbac.model.Rol;
import javax.persistence.NoResultException;

/**
 *
 * @author joshmr94
 */
public class RolSession extends CommonSession<Rol> {
    
    private static final Logger LOG = Logger.getLogger(RolSession.class);

    public RolSession(EntityManager em) {
        super(em);
    }

    public RolSession() {
        super();
    }
    
    @Override
    public void delete(Rol entity, String idUsuario) {
        try {
            super.delete(entity, idUsuario);
            new RbacSession().modeloActualizado();
        } catch (Exception e) {
            rollbackTransaction();
            throw new ServiceRuntimeException(e);
        } finally {
            closeEntityManager();
        }
    }

    @Override
    public Rol save(Rol entity, String idUsuario) {
        try {
            Rol r = super.save(entity, idUsuario); 
            new RbacSession().modeloActualizado();
            return r;
        } catch (Exception e) {
            rollbackTransaction();
            throw new ServiceRuntimeException(e);
        } finally {
            closeEntityManager();
        }
    }
    
    public Rol findByName(String rolname) {
        try {
            String hql = "SELECT r FROM " + Rol.class.getName() + " AS r"
                    + " WHERE r.descripcion=:rolname";
            Query query = getEntityManager().createQuery(hql);
            query.setParameter("rolname", rolname);
            return (Rol)query.getSingleResult();
        } catch (NoResultException e) {
            LOG.warn("No results", e);
            return null;
        } finally {
            closeEntityManager();
        }
    }

    public List<Permiso> getPermisos(Long idRol) {
        try {
            String hql = "SELECT per FROM " + Permiso.class.getName() + " AS per"
                    + " WHERE per.rol.id=:id ORDER BY per.id";
            Query query = getEntityManager().createQuery(hql);
            query.setParameter("id", idRol);
            return query.getResultList();
        } finally {
            closeEntityManager();
        }
    }

    public void compruebaCiclos(Rol rol) {
        compruebaCiclosRecursivo(rol, new Stack<>());
    }
    
    protected void compruebaCiclosRecursivo(Rol rol, Stack<String> stack) {
        if (stack.contains(rol.getDescripcion())) {
            throw new ServiceRuntimeException("El rol '" + rol.getDescripcion() + "' forma un ciclo en el árbol de roles: " + HelperRbac.imprimeCiclo(stack, rol.getDescripcion()));
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Iteración en nodo: " + HelperRbac.imprimeCiclo(stack, rol.getDescripcion()));
        }
        
        stack.add(rol.getDescripcion());
        for (Rol subrol : rol.getSubroles()) {
            compruebaCiclosRecursivo(subrol, stack);
        }
        stack.pop();
    }

}
