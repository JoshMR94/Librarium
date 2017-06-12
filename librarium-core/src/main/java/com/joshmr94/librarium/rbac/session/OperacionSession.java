package com.joshmr94.librarium.rbac.session;

import com.joshmr94.librarium.comun.service.CommonSession;
import javax.persistence.EntityManager;

import com.joshmr94.librarium.rbac.model.Operacion;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author jmoran
 */
public class OperacionSession extends CommonSession<Operacion>{

    public OperacionSession(EntityManager em) {
        super(em);
    }

    public OperacionSession() {
        super();
    }

    @Override
    public void delete(Operacion entity,
            String idUsuario) {
        super.delete(entity, idUsuario); //To change body of generated methods, choose Tools | Templates.
        new RbacSession().modeloActualizado();
    }

    @Override
    public Operacion save(Operacion entity,
            String idUsuario) {
        Operacion o = super.save(entity, idUsuario); //To change body of generated methods, choose Tools | Templates.
        new RbacSession().modeloActualizado();
        return o;
    }
    
    public List<Operacion> findByRecurso(String recurso) {
        try {
            String jpql = "SELECT o FROM " + Operacion.class.getName() + " o JOIN o.recurso r WHERE r.descripcion=:recurso";
            Query query = getEntityManager().createQuery(jpql);
            query.setParameter("recurso", recurso);
            return query.getResultList();
        } catch (NoResultException e) {
            throw e;
        } finally {
            closeEntityManager();
        }
    }

}
