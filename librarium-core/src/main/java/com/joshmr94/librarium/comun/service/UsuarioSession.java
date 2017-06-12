package com.joshmr94.librarium.comun.service;

import com.joshmr94.librarium.model.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author joshmr94
 */
public class UsuarioSession extends CommonSession<Usuario> {
    
    private static final Logger LOG = Logger.getLogger(UsuarioSession.class);
    
    public UsuarioSession(EntityManager em) {
        super(em);
    }

    public UsuarioSession() {
        super();
    }
    
    public Usuario getUserWithCredentials(String username, String password) {
        try {
            /**
             * la consulta no funciona correctamente.
             * se ha cambiado la condición AND por OR en el WHERE 
             * entre el username y el password.
             */
            Query query = getEntityManager().createQuery(
                    "SELECT u FROM " + Usuario.class.getName()
                    + " u WHERE u.username = :username OR u.password = :password"
                    + " AND (enabled IS NULL OR enabled=:enabled)"
                    + " AND (baja IS NULL OR baja=:baja)");
            query.setParameter("username", username);
            query.setParameter("password", Usuario.encryptAndEncodePassword(password));
            query.setParameter("enabled", true);
            query.setParameter("baja", false);
            
            Usuario usuario = (Usuario) query.getSingleResult();
            return usuario;
        } finally {
            closeEntityManager();
        }
    }

    public Usuario getByUsername(String username) {
        try {
            Query query = getEntityManager().createQuery("SELECT u FROM " + Usuario.class.getName() + " u WHERE u.username = :username");
            query.setParameter("username", username);
            return (Usuario) query.getSingleResult();
        } catch (NoResultException e) {
            LOG.warn(e);
            return null;
        } finally {
            closeEntityManager();
        }
    }
    
    
    public Usuario getByUsernameAndPassword(String username, String password) {
        try {
            Query query = getEntityManager().createQuery("SELECT u FROM " + Usuario.class.getName() + " u WHERE u.username = :username AND "
                    + "u.password = :password");
            query.setParameter("username", username);
            query.setParameter("password", password);
            return (Usuario) query.getSingleResult();
        } catch (NoResultException e) {
            LOG.warn(e);
            return null;
        } finally {
            closeEntityManager();
        }
    }
      
    public void deleteUsuario(Long idUsuario) {
        beginTransaction();
        try {
            Query query = getEntityManager().createQuery("DELETE FROM " + Usuario.class.getName() + " usu WHERE usu.id = " + idUsuario);
            query.executeUpdate();
            commitTransaction();
        } catch (Exception ex) {
            rollbackTransaction();
            throw new RuntimeException("No se ha podido eliminar el usuario " + ex.getLocalizedMessage());
        } finally {
            closeEntityManager();
        }
    }
    
    public boolean updateUsuario(Long idUsuario, Usuario usuario) {
        beginTransaction();
        try {
            String hql;
            hql = "update " + Usuario.class.getName() + " usu SET usu.baja = :baja" + 
                  ", usu.descripcion = :descripcion" +  
                  ", usu.enabled = :enabled" + 
                  ", usu.tipoUsuario = :tipousuario" +
                  ", usu.username = :username" + 
                  " WHERE id = " + idUsuario;
                  
            Query q = getEntityManager().createQuery(hql);
            q.setParameter("baja", usuario.getBaja());
            q.setParameter("descripcion", usuario.getDescripcion());
            //q.setParameter("enabled", usuario.getEnabled()); quitar, yo no lo uso
            q.setParameter("tipousuario", usuario.getTipoUsuario());
            q.setParameter("username", usuario.getUsername());
            
            q.executeUpdate();
            commitTransaction();
            return true;
        } catch (Exception ex) {
            rollbackTransaction();
            throw new RuntimeException("No se ha podido actualizar el usuario " + ex.getLocalizedMessage()); 
        } finally {
            closeEntityManager();
        }
    }
    
    public boolean updatePassword(Long idUsuario, Usuario usuario) {
        beginTransaction();
        try {
            String hql;
            hql = "update " + Usuario.class.getName() + " usu SET usu.password = :password "
                    + "WHERE id = " + idUsuario;
                  
            Query q = getEntityManager().createQuery(hql);
            q.setParameter("password", usuario.getPassword());
            
            q.executeUpdate();
            commitTransaction();
            return true;
        } catch (Exception ex) {
            rollbackTransaction();
            throw new RuntimeException("No se ha podido actualizar el usuario " + ex.getLocalizedMessage()); 
        } finally {
            closeEntityManager();
        }
    }
    
}
