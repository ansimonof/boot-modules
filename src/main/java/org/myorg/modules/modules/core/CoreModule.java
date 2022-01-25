package org.myorg.modules.modules.core;

import org.myorg.modules.access.privilege.AccessOp;
import org.myorg.modules.access.privilege.AccessOpCollection;
import org.myorg.modules.modules.core.access.privilege.AccessRoleManagementPrivilege;
import org.myorg.modules.modules.core.access.privilege.UserManagementPrivilege;
import org.myorg.modules.modules.core.domainobjects.DbAccessRole;
import org.myorg.modules.modules.core.domainobjects.DbApiKey;
import org.myorg.modules.modules.core.domainobjects.DbPrivilege;
import org.myorg.modules.modules.core.service.database.accessrole.AccessRoleService;
import org.myorg.modules.modules.core.service.database.apikey.DbApiKeyService;
import org.myorg.modules.crypto.CryptoService;
import org.myorg.modules.modules.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;

@Component
@Priority(value = 1)
public final class CoreModule extends Module {

    @Autowired
    CryptoService cryptoService;

    @Autowired
    DbApiKeyService dbApiKeyService;

    @Autowired
    private AccessRoleService accessRoleService;

    @Override
    public void init() {

        DbPrivilege dbPrivilege1 = new DbPrivilege();
        dbPrivilege1.setKey(UserManagementPrivilege.INSTANCE.getKey());
        dbPrivilege1.setValue(new AccessOpCollection(AccessOp.READ, AccessOp.DELETE).getValue());
        DbPrivilege dbPrivilege2 = new DbPrivilege();
        dbPrivilege2.setKey(AccessRoleManagementPrivilege.INSTANCE.getKey());
        dbPrivilege2.setValue(new AccessOpCollection(AccessOp.READ, AccessOp.DELETE).getValue());
//        dbPrivilege1 = privilegeService.merge(dbPrivilege1);

        DbAccessRole dbAccessRole = new DbAccessRole();
        dbAccessRole.setName("ar");
        dbAccessRole.addPrivilege(dbPrivilege1);
        dbAccessRole.addPrivilege(dbPrivilege2);
        dbAccessRole = accessRoleService.merge(dbAccessRole);

        DbApiKey dbApiKey = new DbApiKey();
        byte[] encode = cryptoService.encode("123");
        dbApiKey.setValue(encode);
        dbApiKey = dbApiKeyService.merge(dbApiKey);
        dbApiKey.setAccessRole(dbAccessRole);
        dbApiKeyService.merge(dbApiKey);

    }

    @Override
    public void destroy() {

    }
}
