package com.joshmr94.librarium.rbac.session;

import com.joshmr94.librarium.comun.service.CommonSession;
import java.util.List;
import javax.persistence.Query;

import com.joshmr94.librarium.rbac.model.Permiso;
import javax.persistence.EntityManager;

/**
 *
 * @author joshmr94
 */
public class PermisoSession extends CommonSession<Permiso>{

    public PermisoSession(EntityManager em) {
        super(em);
    }

    public PermisoSession() {
        super();
    }
    

    @Override
    public void delete(Permiso entity,
            String idUsuario) {
        super.delete(entity, idUsuario); //To change body of generated methods, choose Tools | Templates.
        new RbacSession().modeloActualizado();
    }

    @Override
    public Permiso save(Permiso entity,
            String idUsuario) {
        Permiso p = super.save(entity, idUsuario); //To change body of generated methods, choose Tools | Templates.
        new RbacSession().modeloActualizado();
        return p;
    }
    

    public List<Permiso> getAll() {
        try {
            String hql = "SELECT per FROM " + Permiso.class.getName() + " AS per";
            Query query = getEntityManager().createQuery(hql);
            return query.getResultList();
        } finally {
            closeEntityManager();
        }
    }

}
