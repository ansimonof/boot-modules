package org.myorg.modules.access.privilege.getter;

import org.myorg.modules.access.privilege.AbstractPrivilege;

import java.util.List;

public abstract class ModulePrivilegeGetter {

    public abstract List<? extends AbstractPrivilege> getPrivileges();
}
