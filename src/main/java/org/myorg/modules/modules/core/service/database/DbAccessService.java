package org.myorg.modules.modules.core.service.database;

import org.myorg.modules.modules.core.domainobjects.DomainObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class DbAccessService<T extends DomainObject> {

    @PersistenceContext
    protected EntityManager em;

    private Class<T> clazz;

    public DbAccessService(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T get(long id) {
        return em.find(clazz, id);
    }
}
