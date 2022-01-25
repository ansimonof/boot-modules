package org.myorg.modules.modules.core.access.privilege;

import org.myorg.modules.access.privilege.AbstractPrivilege;
import org.myorg.modules.access.privilege.AccessOp;

public class UserManagementPrivilege extends AbstractPrivilege {

    public static final UserManagementPrivilege INSTANCE = new UserManagementPrivilege();

    private UserManagementPrivilege() {
        super(
                "core.module.user_management",
                AccessOp.READ, AccessOp.WRITE, AccessOp.DELETE
        );
    }

    @Override
    public AbstractPrivilege getInstance() {
        return INSTANCE;
    }
}
