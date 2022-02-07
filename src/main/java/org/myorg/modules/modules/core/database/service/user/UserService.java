package org.myorg.modules.modules.core.database.service.user;

import org.myorg.modules.modules.core.database.domainobjects.DbUser;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleDto;
import org.myorg.modules.modules.database.service.DomainObjectService;
import org.myorg.modules.modules.exception.ModuleException;

public interface UserService extends DomainObjectService<DbUser, UserBuilder, UserDto> {

    UserDto findByUsername(String username) throws ModuleException;

    AccessRoleDto findAccessRole(long userId) throws ModuleException;

    UserDto addAccessRole(long userId, long accessRoleId) throws ModuleException;
}
