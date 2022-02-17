package org.myorg.modules.modules.database.service;

import org.myorg.modules.access.context.Context;
import org.myorg.modules.modules.dto.AbstractDto;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.utils.DomainObjectBuilder;

import java.util.Set;

public interface DomainObjectService<S extends DomainObjectBuilder, U extends AbstractDto> {

    U findById(long id, Context<?> context) throws ModuleException;

    Set<U> findAll(Context<?> context) throws ModuleException;

    U create(S builder, Context<?> context) throws ModuleException;

    U update(long id, S builder, Context<?> context) throws ModuleException;

    void remove(long id, Context<?> context) throws ModuleException;

}
