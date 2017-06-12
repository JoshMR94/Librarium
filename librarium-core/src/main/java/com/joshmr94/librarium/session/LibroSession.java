package com.joshmr94.librarium.session;

import com.joshmr94.librarium.comun.service.CommonSession;
import com.joshmr94.librarium.model.Libro;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author joshmr94
 */
public class LibroSession extends CommonSession<Libro>{
    
    private static final Logger LOG = Logger.getLogger(Libro.class);
    
    public LibroSession(EntityManager em){
        super(em);
    }
    
    public LibroSession() {
        super();
    }
    
    public long countLibros(){
        try {
            String queryString;
            queryString = String.format("select count(libro.id) from libro", Libro.class.getName());
            Query query = getEntityManager().createQuery(queryString);
            return (long) query.getSingleResult();
        } catch (Exception e) {
            LOG.warn("Error al obtener el número de libros", e);
            return 0L;
        } finally {
            closeEntityManager();
        }
    }
    
    public Libro findLibroById(Long id) {
        try {
            String queryString;
            queryString = String.format("select * from libro where libro.id = :id", Libro.class.getName());
            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("id", id);
            return (Libro) query.getSingleResult();
        } catch (NoResultException e) {
            LOG.error("Error al obtener el libro por id", e);
            return null;
        } finally {
            closeEntityManager();
        }
    }
    
    public List<Libro> findLibros() {
        return null;
    }
     
}
