package com.joshmr94.librarium.rbac.session;

import com.joshmr94.librarium.comun.service.CommonSession;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import com.joshmr94.librarium.rbac.model.Operacion;
import com.joshmr94.librarium.rbac.model.Recurso;

/**
 *
 * @author joshmr94
 */
public class RecursoSession extends CommonSession<Recurso> {

    public RecursoSession(EntityManager em) {
        super(em);
    }

    public RecursoSession() {
        super();
    }

    @Override
    public void delete(Recurso entity,
            String idUsuario) {
        super.delete(entity, idUsuario); //To change body of generated methods, choose Tools | Templates.
        new RbacSession().modeloActualizado();
    }

    @Override
    public Recurso save(Recurso entity,
            String idUsuario) {
        Recurso r = super.save(entity, idUsuario); //To change body of generated methods, choose Tools | Templates.
        new RbacSession().modeloActualizado();
        return r;
    }

    public List<Operacion> getOperaciones(Long idRecurso) {
        try {
            String hql = "SELECT op FROM " + Operacion.class.getName() + " AS op"
                    + " WHERE op.recurso.id=:id ORDER BY op.operacion";
            Query query = getEntityManager().createQuery(hql);
            query.setParameter("id", idRecurso);
            return query.getResultList();
        } finally {
            closeEntityManager();
        }
    }

}
