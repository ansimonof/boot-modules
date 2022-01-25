package org.myorg.modules.modules.core.service.access;

import org.myorg.modules.access.privilege.AbstractPrivilege;
import org.myorg.modules.access.privilege.getter.ModulePrivilegeGetter;
import org.myorg.modules.modules.core.access.privilege.AccessRoleManagementPrivilege;
import org.myorg.modules.modules.core.access.privilege.UserManagementPrivilege;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CorePrivilegeGetter extends ModulePrivilegeGetter {

    @Override
    public List<? extends AbstractPrivilege> getPrivileges() {
        return new ArrayList<AbstractPrivilege>() {{
            add(UserManagementPrivilege.INSTANCE);
            add(AccessRoleManagementPrivilege.INSTANCE);
        }};
    }
}
