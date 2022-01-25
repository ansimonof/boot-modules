package org.myorg.modules.modules.core.controller;

import org.myorg.modules.access.AccessPermission;
import org.myorg.modules.access.context.AuthorizedContext;
import org.myorg.modules.access.privilege.AbstractPrivilege;
import org.myorg.modules.access.privilege.AccessOp;
import org.myorg.modules.access.privilege.getter.PrivilegeGetter;
import org.myorg.modules.modules.core.CoreModuleConsts;
import org.myorg.modules.modules.core.access.privilege.AccessRoleManagementPrivilege;
import org.myorg.modules.modules.core.domainobjects.DbAccessRole;
import org.myorg.modules.modules.core.service.database.accessrole.AccessRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(CoreModuleConsts.REST_CONTROLLER_PREFIX + "/access_roles")
public class AccessRoleController {

    @Autowired
    private PrivilegeGetter privilegeGetter;

    @Autowired
    private AccessRoleService accessRoleService;

    @GetMapping
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.READ }
    )
    public ResponseEntity<?> getAccessRoles() {
        return ResponseEntity.ok(accessRoleService.findAll().stream()
                .map(DbAccessRole::getName).collect(Collectors.toList()));
    }

    @GetMapping("/privileges")
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.READ }
    )
    public ResponseEntity<?> getPrivileges() {
        List<? extends AbstractPrivilege> privileges = privilegeGetter.getAllPrivileges();
        return ResponseEntity.ok(privileges.stream()
                .map(AbstractPrivilege::getKey).collect(Collectors.toList()));
    }

    @PostMapping
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.WRITE }
    )
    public ResponseEntity<?> createAccessRole(@RequestParam("name") String name) {
        DbAccessRole dbAccessRole = new DbAccessRole();
        dbAccessRole.setName(name);
        accessRoleService.merge(dbAccessRole);

        return ResponseEntity.ok().build();
    }

}
