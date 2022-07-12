package org.myorg.modules.modules.database.service;

import org.myorg.modules.access.context.Context;
import org.myorg.modules.modules.database.DomainObject;
import org.myorg.modules.modules.database.dao.GenericDAO;
import org.myorg.modules.modules.dto.AbstractDto;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.utils.DomainObjectBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class DomainObjectService<
        D extends DomainObject,
        T extends GenericDAO<D>,
        S extends DomainObjectBuilder,
        U extends AbstractDto> {

    protected final T dao;
    protected final Function<D, U> dtoBuilder;

    public DomainObjectService(T dao, Function<D, U> dtoBuilder) {
        this.dao = dao;
        this.dtoBuilder = dtoBuilder;
    }

    @Transactional(readOnly = true)
    public U findById(long id) {
        return dtoBuilder.apply(dao.findById(id));
    }

    @Transactional(readOnly = true)
    public Set<U> findAll() {
        return dao.findAll().stream()
                .map(dtoBuilder)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void remove(long id) throws ModuleException {
        D domainObject = dao.findById(id);
        if (domainObject != null) {
            dao.makeTransient(domainObject);
        }
    }

    @Transactional
    public abstract U create(S builder, Context<?> context) throws ModuleException;

    @Transactional
    public abstract U update(long id, S builder, Context<?> context) throws ModuleException;

}
