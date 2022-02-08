package org.myorg.modules.modules.core.database.service.user;

import org.myorg.modules.modules.core.database.domainobjects.DbUser;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleDto;
import org.myorg.modules.modules.database.service.DomainObjectService;
import org.myorg.modules.modules.exception.ModuleException;

import java.util.Set;

public interface UserService extends DomainObjectService<DbUser, UserBuilder, UserDto> {

    UserDto findByUsername(String username) throws ModuleException;

    Set<AccessRoleDto> findAllAccessRoles(long userId) throws ModuleException;

    UserDto addAccessRole(long userId, long accessRoleId) throws ModuleException;

    UserDto removeAccessRole(long userId, long accessRoleId) throws ModuleException;
}
