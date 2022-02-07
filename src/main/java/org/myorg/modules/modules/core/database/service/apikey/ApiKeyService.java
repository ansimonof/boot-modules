package org.myorg.modules.modules.core.database.service.apikey;

import org.myorg.modules.modules.core.database.domainobjects.DbApiKey;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleDto;
import org.myorg.modules.modules.database.service.DomainObjectService;
import org.myorg.modules.modules.exception.ModuleException;

public interface ApiKeyService extends DomainObjectService<DbApiKey, ApiKeyBuilder, ApiKeyDto> {

    AccessRoleDto findAccessRole(long apiKeyId) throws ModuleException;

    ApiKeyDto addAccessRole(long apiKeyId, long accessRoleId) throws ModuleException;

    ApiKeyDto findByValue(String value) throws ModuleException;
}
