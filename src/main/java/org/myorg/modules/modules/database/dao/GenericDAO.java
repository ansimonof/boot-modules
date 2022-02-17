package org.myorg.modules.modules.database.dao;

import org.myorg.modules.modules.database.DomainObject;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.utils.Supplier;

import javax.persistence.LockModeType;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public interface GenericDAO<T extends DomainObject> {

    T findById(long id);

    T findById(long id, LockModeType lock);

    T findReferenceById(long id);

    Set<T> findAll();

    long getCount();

    T makePersistent(T instance);

    void makeTransient(T instance);

    void checkVersion(T entity, boolean forceUpdate);

    Stream<T> execNamedQuery(String query, Map<String, Object> params);

    T checkExistenceAndReturn(long id) throws ModuleException;

    void checkUniqueness(T anotherDomainObject, Supplier<T> domainObjectSupplier, Supplier<? extends ModuleException> exceptionSupplier) throws ModuleException;
}
