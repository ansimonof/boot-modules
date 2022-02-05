package org.myorg.modules.modules.database.service;

import org.myorg.modules.modules.database.DomainObject;
import org.myorg.modules.modules.dto.AbstractDto;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.utils.DomainObjectBuilder;

import java.util.Set;

public interface DomainObjectService<T extends DomainObject, S extends DomainObjectBuilder, U extends AbstractDto> {

    U findById(long id) throws ModuleException;

    Set<U> findAll() throws ModuleException;

    U create(S builder) throws ModuleException;

    U update(long id, S builder) throws ModuleException;

    void remove(long id) throws ModuleException;

}
