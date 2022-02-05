package org.myorg.modules.modules.core.controller;

import org.myorg.modules.access.AccessPermission;
import org.myorg.modules.access.context.AuthorizedContext;
import org.myorg.modules.access.privilege.AbstractPrivilege;
import org.myorg.modules.access.privilege.AccessOp;
import org.myorg.modules.access.privilege.getter.PrivilegeGetter;
import org.myorg.modules.modules.core.CoreModuleConsts;
import org.myorg.modules.modules.core.access.privilege.AccessRoleManagementPrivilege;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleBuilder;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleDto;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleService;
import org.myorg.modules.modules.core.database.service.accessrole.PrivilegeDto;
import org.myorg.modules.modules.exception.ModuleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public ResponseEntity<Set<AccessRoleDto>> getList() throws ModuleException {
        return ResponseEntity.ok(accessRoleService.findAll());
    }

    @GetMapping("/privileges")
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.READ }
    )
    public ResponseEntity<List<PrivilegeDto>> getPrivileges() {
        List<? extends AbstractPrivilege> privileges = privilegeGetter.getAllPrivileges();
        List<PrivilegeDto> result = new ArrayList<>();
        for (AbstractPrivilege privilege : privileges) {
            PrivilegeDto privilegeDto = new PrivilegeDto();
            privilegeDto.setKey(privilege.getKey());
            privilegeDto.setOps(privilege.getAccessOpCollection().getOps());

            result.add(privilegeDto);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create")
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.WRITE }
    )
    public ResponseEntity<AccessRoleDto> create(@RequestParam("name") String name) throws ModuleException {
        AccessRoleDto accessRoleDto = accessRoleService.create(
                AccessRoleBuilder.builder()
                        .name(name)
        );

        return ResponseEntity.ok(accessRoleDto);
    }

}
