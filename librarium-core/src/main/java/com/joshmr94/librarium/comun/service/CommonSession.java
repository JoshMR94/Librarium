package com.joshmr94.librarium.comun.service;

import com.joshmr94.librarium.rbac.model.IEntity;
import com.joshmr94.librarium.rbac.model.IEntityBaja;
import com.joshmr94.librarium.rbac.model.IEntityCodigoDescripcion;
import com.joshmr94.librarium.rbac.model.IEntityDescripcion;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author jmartin 
 */
public abstract class CommonSession<T extends IEntity> {
    
    private static final Logger LOG = Logger.getLogger(CommonSession.class);

    EntityManager entityManager;
    EntityManager sharedEntityManager;
    
    static volatile EntityManagerFactory emf;

    public CommonSession(EntityManager em) {
        this();

        if (em == null) {
            throw new NullPointerException("Entity manager cannot be null.");
        }
        this.sharedEntityManager = em;
    }
    
    public CommonSession() {
        initEntityManagerFactory();
    }
    
    static final void initEntityManagerFactory() {
        EntityManagerFactory localEmf = emf;
        if (localEmf == null) {
            synchronized (CommonSession.class) {
                if (emf == null) {
                    LOG.debug("EntityManagerFactory initialization...");
                    emf = Persistence.createEntityManagerFactory("com.inerza_SolicitudHSR_PU");
                    LOG.debug("EntityManagerFactory created.");
                }
            }
        }
    }
    
    protected EntityManager getEntityManager() {
        LOG.debug("EntityManager resquested...");
        if (sharedEntityManager != null) {
            LOG.debug("Shared EntityManager returned");
            return sharedEntityManager;
        } else {
            if (entityManager == null || !entityManager.isOpen()) {
                LOG.debug("EntityManager initialization...");
                entityManager = buildEntityManager();
                LOG.debug(String.format("EntityManager created: %s", entityManager));
            }
            LOG.debug("EntityManager returned");
            return entityManager;
        }
    }
    
    public static EntityManager buildEntityManager() {
        initEntityManagerFactory();
        return emf.createEntityManager();
    }
    
    protected void beginTransaction() {
        if (sharedEntityManager == null) {
            LOG.debug("Transaction initialization...");
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
                LOG.debug("Transaction created.");
            }
        }
    }
    
    protected void rollbackTransaction() {
        try {
            if (getEntityManager().getTransaction().isActive()) {
                LOG.debug("Transaction rollbacking...");
                getEntityManager().getTransaction().rollback();
                LOG.debug("Transaction rollbacked .");
            }
        } catch (Exception e) {
            LOG.error("Error rollbacking transaction.", e);
            throw new ServiceRuntimeException(e);
        }
    }
    
    protected void commitTransaction() {
        if (sharedEntityManager == null) {
            LOG.debug("Transaction committing...");
            getEntityManager().getTransaction().commit();
            LOG.debug("Transaction committed .");
        }
    }
    
    protected void closeEntityManager() {
        if (sharedEntityManager == null) {
            LOG.debug("EntityManager clossing ...");
            if (getEntityManager().getTransaction().isActive()) {
                LOG.warn("Transaction rollbacking on close...");
                getEntityManager().getTransaction().rollback();
                LOG.warn("Transaction rollbacked on close.");
            }
            entityManager.close();
            LOG.debug("EntityManager closed.");
        }
    }
    
    public void audit(final T entity, final String idUsuario, AuditOperation oper) {
        audit(entity, idUsuario, oper, null);
    }

    public void audit(final T entity, final String idUsuario, AuditOperation oper, String desc) {

    }
    
    public T save(final T entity, final String idUsuario) {
        beginTransaction();
        try {
            T o = entity;
            if (entity.isNew()) {
                getEntityManager().persist(entity);
                audit(entity, idUsuario, AuditOperation.CREATE);
            } else {
                o = getEntityManager().merge(entity);
                audit(entity, idUsuario, AuditOperation.UPDATE);
            }
            commitTransaction();
            return o;
        } catch (Exception e) {
            rollbackTransaction();
            throw new ServiceRuntimeException(e);
        } finally {
            closeEntityManager();
        }
    }
    
    public T restore(final T entity, final String idUsuario) {
        if (!(entity instanceof IEntityBaja)) {
            throw new IllegalArgumentException("No IEntityBaja");
        }
        beginTransaction();
        try {
            IEntityBaja o = (IEntityBaja) entity;
            o.setBaja(false);
            o = getEntityManager().merge(o);
            audit(entity, idUsuario, AuditOperation.RESTORE);
            commitTransaction();
            return (T) o;
        } catch (Exception e) {
            rollbackTransaction();
            throw new ServiceRuntimeException(e);
        } finally {
            closeEntityManager();
        }
    }
    
    public void delete(final T entity, final String idUsuario) {
        beginTransaction();
        try {
            doDelete(entity, idUsuario);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            LOG.error(e);
            throw new ServiceRuntimeException(e);
        } finally {
            closeEntityManager();
        }
    }

    public void delete(long id, final String idUsuario) {
        beginTransaction();
        try {
            T entity = getEntityManager().find(getActualTypeArgument(), id);
            if (entity == null) {
                throw new IllegalArgumentException(String.format("Invalid ID %d for entity of type %s", id, getActualTypeArgument()));
            }
            doDelete(entity, idUsuario);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw new ServiceRuntimeException(e);
        } finally {
            closeEntityManager();
        }
    }

    void doDelete(T entity, final String idUsuario) {
        if (!(entity instanceof IEntityBaja)) {
            System.out.println("REMOVE");
            getEntityManager().remove(entity); //test OperacionSession no funciona aqui
            audit(entity, idUsuario, AuditOperation.DELETE);
        } else {
            IEntityBaja o = (IEntityBaja) entity;
            o.setBaja(true);
            getEntityManager().merge(o);
            audit(entity, idUsuario, AuditOperation.DISABLE);
        }
    }
    
    public T open(Long id, String idUsuario) {
        try {
            T o = getEntityManager().find(getActualTypeArgument(), id);
            audit(o, idUsuario, AuditOperation.OPEN);
            return o;
        } finally {
            closeEntityManager();
        }
    }

    protected Class<T> getActualTypeArgument() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }
    
    public List<T> findAll(String idUsuario) {
        return doFind(false, null, -1, false, -1, -1, idUsuario);
    }

    public List<T> findAll(Boolean disabled, String idUsuario) {
        return doFind(disabled, null, -1, false, -1, -1, idUsuario);
    }

    public List<T> findAllIncludedDisabled(String idUsuario) {
        return doFind((Boolean) null, null, -1, false, -1, -1, idUsuario);
    }

    public List<T> findAllDisabled(String idUsuario) {
        return doFind(true, null, -1, false, -1, -1, idUsuario);
    }

    public List<T> find(String desc, String idUsuario) {
        return doFind(false, desc, -1, false, -1, -1, idUsuario);
    }

    public List<T> find(Boolean disabled, String desc, String idUsuario) {
        return doFind(disabled, desc, -1, false, -1, -1, idUsuario);
    }

    public List<T> findIncludedDisabled(String desc, String idUsuario) {
        return doFind(null, desc, -1, false, -1, -1, idUsuario);
    }

    public List<T> findDisabled(String desc, String idUsuario) {
        return doFind(true, desc, -1, false, -1, -1, idUsuario);
    }

    public List<T> findAll(int first, int max, String idUsuario) {
        return doFind(false, null, -1, false, first, max, idUsuario);
    }

    public List<T> findAll(Boolean disabled, int first, int max, String idUsuario) {
        return doFind(disabled, null, -1, false, first, max, idUsuario);
    }

    public List<T> findAllIncludedDisabled(int first, int max, String idUsuario) {
        return doFind((Boolean) null, null, -1, false, first, max, idUsuario);
    }

    public List<T> findAllDisabled(int first, int max, String idUsuario) {
        return doFind(true, null, -1, false, first, max, idUsuario);
    }

    public List<T> find(String desc, int oby, boolean asc, int first, int max, String idUsuario) {
        return doFind(false, desc, oby, asc, first, max, idUsuario);
    }

    public List<T> find(Boolean disabled, String desc, int oby, boolean asc, int first, int max, String idUsuario) {
        return doFind(disabled, desc, oby, asc, first, max, idUsuario);
    }

    public List<T> findAllIncludedDisabled(String desc, int oby, boolean asc, int first, int max, String idUsuario) {
        return doFind(null, desc, oby, asc, first, max, idUsuario);
    }

    public List<T> findDisabled(String desc, int oby, boolean asc, int first, int max, String idUsuario) {
        return doFind(true, desc, oby, asc, first, max, idUsuario);
    }
    
    public long countAll(String idUsuario) {
        return doCount(false, null, idUsuario);
    }

    public long countAll(Boolean disabled, String idUsuario) {
        return doCount(disabled, null, idUsuario);
    }

    public long countAllIncludedDisabled(String idUsuario) {
        return doCount((Boolean) null, null, idUsuario);
    }

    public long countDisabled(String idUsuario) {
        return doCount(true, null, idUsuario);
    }

    public long countAll(String desc, String idUsuario) {
        return doCount(false, desc, idUsuario);
    }

    public long countAll(Boolean disabled, String desc, String idUsuario) {
        return doCount(disabled, desc, idUsuario);
    }

    public long countAllIncludedDisabled(String desc, String idUsuario) {
        return doCount(null, desc, idUsuario);
    }

    public long countDisabled(String desc, String idUsuario) {
        return doCount(true, desc, idUsuario);
    }
    
    protected List<T> doFind(Boolean disabled, String description, int oby, boolean asc, int first, int max, String idUsuario) {
        HashMap<String, Object> pars = new HashMap<>();
        pars.put("descripcion", description);

        return doFind(pars, disabled, oby, asc, first, max, idUsuario);
    }
    
    protected long doCount(Boolean disabled, String description, String idUsuario) {
        HashMap<String, Object> pars = new HashMap<>();
        pars.put("descripcion", description);

        return doCount(pars, disabled, idUsuario);
    }
    
    protected List<T> doFind(Map<String, Object> parameters, Boolean disabled, int oby, boolean asc, int first, int max, String idUsuario) {

        String select = "o";

        String from = getFindFrom();

        String where = getFindWhere(parameters, disabled);

        String orderBy = getFindOrderBy(oby, asc);

        String jpql = getFindQuery(select, from, where, orderBy);

        Query query = getEntityManager().createQuery(jpql);
        try {
            prepareFindParameters(parameters, query);

            if (first >= 0) {
                query.setFirstResult(first);
            }
            if (max > 0) {
                query.setMaxResults(max);
            }
            audit(null, idUsuario, AuditOperation.FIND, getActualTypeArgument().getName() + ".doFind");
            return query.getResultList();
        } catch (NoResultException e) {
            LOG.warn("No results", e);
            return new ArrayList<>();
        } finally {
            closeEntityManager();
        }
    }
    
    protected long doCount(Map<String, Object> parameters, Boolean disabled, String idUsuario) {

        String select = "COUNT(o)";

        String from = getFindFrom();

        String where = getFindWhere(parameters, disabled);

        String jpql = getFindQuery(select, from, where, "");

        Query query = getEntityManager().createQuery(jpql);
        try {
            prepareFindParameters(parameters, query);

            Long result;
            try {
                result = (Long) query.getSingleResult();
            } catch (Exception e) {
                throw new ServiceRuntimeException(e);
            }
            audit(null, idUsuario, AuditOperation.COUNT, getActualTypeArgument().getName() + ".doCount");
            return result;
        } finally {
            closeEntityManager();
        }
    }
    
    protected void prepareFindParameters(Map<String, Object> parameters,
            Query query) {
        if (IEntityDescripcion.class.isAssignableFrom(getActualTypeArgument()) && parameters.get("descripcion") != null) {
            query.setParameter("descripcion", "%" + ((String) parameters.get("descripcion")).toLowerCase() + "%");
        }
        parameters.entrySet().stream().forEach(entry -> {
            String par = entry.getKey();
            if (!"descripcion".equals(par) && !"baja".equals(par)) { //estos campos se tratan a parte
                Object parVal = entry.getValue();
                if (parVal instanceof String) {
                    query.setParameter(par, "%" + ((String) parVal).toLowerCase() + "%");
                } else {
                    query.setParameter(par, parVal);
                }
            }
        });
    }
    
    protected String getFindQuery(String select,
            String from,
            String where,
            String orderBy) {
        String jpql = "SELECT " + select + " FROM " + from;
        if (!where.isEmpty()) {
            jpql += " WHERE " + where;
        }
        if (!orderBy.isEmpty()) {
            jpql += " ORDER BY " + orderBy;
        }
        return jpql;
    }
    
    protected String getFindOrderBy(int oby, boolean asc) {
        String orderBy = "";
        if (oby <= 0 && IEntityCodigoDescripcion.class.isAssignableFrom(getActualTypeArgument())) {
            orderBy += "o.codigo" + (asc ? " ASC" : " DESC");
        } else if (oby > 0) {
            orderBy += getOrderedFieldNames().get(oby - 1) + (asc ? " ASC" : " DESC");
        }
        return orderBy;
    }
    
    protected List<String> getOrderedFieldNames() {
        return Arrays.asList(getActualTypeArgument().getDeclaredFields()).stream().map(f -> f.getName()).sorted().collect(Collectors.toList());
    }
    
    protected String getFindWhere(Map<String, Object> parameters, Boolean disabled) {
        final StringBuilder where = new StringBuilder("");
        if (IEntityDescripcion.class.isAssignableFrom(getActualTypeArgument()) && parameters.get("descripcion") != null) {
            where.append(" LOWER(o.descripcion) LIKE :descripcion");
        }
        checkDisabled(disabled, where);

        checkParameters(parameters, where);

        return where.toString();
    }
    
    private void checkParameters(Map<String, Object> parameters, final StringBuilder where) {
        parameters.entrySet().stream().forEach(entry -> {
            String par = entry.getKey();
            if (!"descripcion".equals(par) && !"baja".equals(par)) { //estos campos se tratan a parte
                if (where.length() > 0) {
                    where.append(" and");
                }
                where.append(" o.").append(par).append(" = :").append(par);
            }
        });
    }
    
    private void checkDisabled(Boolean disabled, final StringBuilder where) {
        if ((disabled != null && !disabled) && IEntityBaja.class.isAssignableFrom(getActualTypeArgument())) {
            if (where.length() > 0) {
                where.append(" and");
            }
            where.append(" (o.baja IS NULL OR o.baja = false)");
        }
        if ((disabled != null && disabled) && IEntityBaja.class.isAssignableFrom(getActualTypeArgument())) {
            if (where.length() > 0) {
                where.append(" and");
            }
            where.append(" o.baja = true");
        }
    }

    protected String getFindFrom() {
        return getActualTypeArgument().getName() + " AS o";
    }
    
}
