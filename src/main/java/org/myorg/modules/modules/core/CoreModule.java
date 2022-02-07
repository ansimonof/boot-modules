package org.myorg.modules.modules.core;

import org.myorg.modules.access.privilege.AccessOp;
import org.myorg.modules.modules.Module;
import org.myorg.modules.modules.core.access.privilege.AccessRoleManagementPrivilege;
import org.myorg.modules.modules.core.access.privilege.UserManagementPrivilege;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleBuilder;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleDto;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleService;
import org.myorg.modules.modules.core.database.service.accessrole.PrivilegeBuilder;
import org.myorg.modules.modules.core.database.service.apikey.ApiKeyBuilder;
import org.myorg.modules.modules.core.database.service.apikey.ApiKeyDto;
import org.myorg.modules.modules.core.database.service.apikey.ApiKeyService;
import org.myorg.modules.modules.exception.ModuleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import java.util.HashSet;

@Component
@Priority(value = 1)
public class CoreModule extends Module {

    @Autowired
    ApiKeyService apiKeyService;

    @Autowired
    private AccessRoleService accessRoleService;

    @Override
    public void init() throws ModuleException {

        PrivilegeBuilder privilege1 = PrivilegeBuilder.builder()
                .key(UserManagementPrivilege.INSTANCE.getKey())
                .ops(AccessOp.READ, AccessOp.DELETE);

        PrivilegeBuilder privilege2 = PrivilegeBuilder.builder()
                .key(AccessRoleManagementPrivilege.INSTANCE.getKey())
                .ops(AccessOp.READ, AccessOp.DELETE);

        AccessRoleDto accessRoleDto = accessRoleService.create(AccessRoleBuilder.builder().name("ar"));
        accessRoleDto = accessRoleService.addPrivileges(accessRoleDto.getId(), new HashSet<PrivilegeBuilder>() {{
            add(privilege1);
            add(privilege2);
        }});

        ApiKeyDto apiKeyDto = apiKeyService.create(ApiKeyBuilder.builder().name("APIQWE").value("123"));
    }

    @Override
    public void destroy() throws ModuleException {

    }
}
