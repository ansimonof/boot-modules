package org.myorg.modules.modules.database.dao;

import org.myorg.modules.modules.database.DomainObject;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.modules.exception.ModuleExceptionBuilder;
import org.myorg.modules.utils.Supplier;


import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public abstract class GenericDAOImpl<T extends DomainObject> implements GenericDAO<T> {

    @PersistenceContext
    protected EntityManager em;

    protected final Class<T> entityClass;

    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T findById(long id) {
        return findById(id, LockModeType.NONE);
    }

    @Override
    public T findById(long id, LockModeType lock) {
        return em.find(entityClass, id, lock);
    }

    @Override
    public T findReferenceById(long id) {
        return em.getReference(entityClass, id);
    }

    @Override
    public Set<T> findAll() {
        CriteriaQuery<T> c = em.getCriteriaBuilder().createQuery(entityClass);
        c.select(c.from(entityClass));
        return new HashSet<>(em.createQuery(c).getResultList());
    }

    @Override
    public long getCount() {
        CriteriaQuery<Long> c = em.getCriteriaBuilder().createQuery(long.class);
        c.select(em.getCriteriaBuilder().count(c.from(entityClass)));
        return em.createQuery(c).getSingleResult();
    }

    @Override
    public T makePersistent(T instance) {
        return em.merge(instance);
    }

    @Override
    public void makeTransient(T instance) {
        em.remove(instance);
    }

    @Override
    public void checkVersion(T entity, boolean forceUpdate) {
        em.lock(
                entity,
                forceUpdate
                        ? LockModeType.OPTIMISTIC_FORCE_INCREMENT
                        : LockModeType.OPTIMISTIC
        );
    }

    @Override
    public Stream<T> execNamedQuery(String query, HashMap<String, Object> params) {
        TypedQuery<T> namedQuery = em.createNamedQuery(query, entityClass);
        params.forEach(namedQuery::setParameter);
        return namedQuery.getResultStream();
    }

    @Override
    public T checkExistenceAndGet(long id) throws ModuleException {
        T domainObject = findById(id);
        if (domainObject == null) {
            throw ModuleExceptionBuilder.buildNotFoundDomainObjectException(entityClass, id);
        }

        return domainObject;
    }

    @Override
    public void checkUniqueness(T domainObject,
                                Supplier<T> domainObjectSupplier,
                                Supplier<? extends ModuleException> exceptionSupplier) throws ModuleException {
        T anotherDomainObject = domainObjectSupplier.get();
        if (anotherDomainObject != null && !Objects.equals(domainObject.getId(), anotherDomainObject.getId())) {
            throw exceptionSupplier.get();
        }
    }
}
