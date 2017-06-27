package com.joshmr94.librarium.session;

import com.joshmr94.librarium.comun.service.CommonSession;
import com.joshmr94.librarium.model.Autor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author joshmr94
 */
public class AutorSession extends CommonSession<Autor> {
    
    private static final Logger LOG = Logger.getLogger(Autor.class);
    
    public AutorSession(EntityManager em) {
        super(em);
    }
    
    public AutorSession() {
        super();
    }
    
    public long countAutores() {
        try {
            String queryString;
            queryString = String.format("select count(autor.id) from autor", Autor.class.getName());
            Query query = getEntityManager().createQuery(queryString);
            return (long) query.getSingleResult();
        } catch (Exception e) {
            LOG.warn("Error al obtener el numero de autores", e);
            return 0L;
        } finally {
            closeEntityManager();
        }
    }
    
    public Autor findAutorById(Long id) {
        try {
            String queryString;
            queryString = String.format("select * from autor where autor.id = :id", Autor.class.getName());
            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("id", id);
            return (Autor) query.getSingleResult();
        } catch (Exception e) {
            LOG.error("Error al obtener el autor por id", e);
            return null;
        } finally {
            closeEntityManager();
        }
    }
    
    public List<Autor> findAutores() {
        try {
            String queryString;
            queryString = String.format("select * from autor", Autor.class.getName());
            Query query = getEntityManager().createQuery(queryString);
            return (List<Autor>) query.getResultList();
        } catch (NoResultException e) {
            LOG.error("Error al obtener los autores", e);
            return new ArrayList<>();
        } finally {
            closeEntityManager();
        }
    }
    
}
