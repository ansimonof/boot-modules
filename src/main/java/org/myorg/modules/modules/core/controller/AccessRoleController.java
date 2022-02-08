package org.myorg.modules.modules.core.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myorg.modules.access.AccessPermission;
import org.myorg.modules.access.context.AuthorizedContext;
import org.myorg.modules.access.privilege.AbstractPrivilege;
import org.myorg.modules.access.privilege.AccessOp;
import org.myorg.modules.access.privilege.getter.PrivilegeGetter;
import org.myorg.modules.modules.core.CoreModuleConsts;
import org.myorg.modules.modules.core.access.privilege.AccessRoleManagementPrivilege;
import org.myorg.modules.modules.core.database.service.accessrole.*;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.modules.exception.ModuleExceptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(CoreModuleConsts.REST_CONTROLLER_PREFIX + "/access_role")
public class AccessRoleController {

    private final PrivilegeGetter privilegeGetter;
    private final AccessRoleService accessRoleService;

    @Autowired
    public AccessRoleController(PrivilegeGetter privilegeGetter, AccessRoleService accessRoleService) {
        this.privilegeGetter = privilegeGetter;
        this.accessRoleService = accessRoleService;
    }

    @GetMapping
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.READ }
    )
    public ResponseEntity<AccessRoleDto> findById(
            @RequestParam long id
    ) throws ModuleException {
        return ResponseEntity.ok(accessRoleService.findById(id));
    }


    @GetMapping("/list")
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.READ }
    )
    public ResponseEntity<Set<AccessRoleDto>> list() throws ModuleException {
        return ResponseEntity.ok(accessRoleService.findAll());
    }

    @PostMapping("/create")
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.WRITE }
    )
    public ResponseEntity<AccessRoleDto> create(
            @RequestParam final String name
    ) throws ModuleException {
        AccessRoleDto accessRoleDto = accessRoleService.create(
                AccessRoleBuilder.builder()
                        .name(name)
        );

        return ResponseEntity.ok(accessRoleDto);
    }

    @PutMapping("/update")
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.WRITE }
    )
    public ResponseEntity<AccessRoleDto> update(
            @RequestParam final Long id,
            @RequestParam(required = false) final String name
    ) throws ModuleException {
        return ResponseEntity.ok(accessRoleService.update(id, AccessRoleBuilder.builder().name(name)));
    }

    @DeleteMapping("/remove")
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.DELETE }
    )
    public ResponseEntity<Long> remove(
            @RequestParam final Long id
    ) throws ModuleException {
        accessRoleService.remove(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/privilege/list")
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.READ }
    )
    public ResponseEntity<List<PrivilegeDto>> privilegesList() {
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

    @GetMapping("/privilege")
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.READ }
    )
    public ResponseEntity<PrivilegeDto> findPrivilegeByKey(
            @RequestParam final String key
    ) throws ModuleException {
        AbstractPrivilege privilege = privilegeGetter.getAllPrivileges().stream()
                .filter(p -> Objects.equals(p.getKey(), key))
                .findFirst()
                .orElse(null);
        if (privilege == null) {
            throw ModuleExceptionBuilder.buildInvalidValueException(key);
        }

        PrivilegeDto privilegeDto = PrivilegeDto.builder()
                .key(privilege.getKey())
                .ops(privilege.getAccessOpCollection().getOps())
                .build();

        return ResponseEntity.ok(privilegeDto);
    }

    @PutMapping("/set_privileges")
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = AccessRoleManagementPrivilege.class,
            ops = { AccessOp.WRITE }
    )
    public ResponseEntity<AccessRoleDto> setPrivileges(
            @RequestParam(name = "access_role_id") final Long accessRoleId,
            @RequestBody final PrivilegeSet newPrivileges
    ) throws ModuleException {
        AccessRoleDto accessRoleDto = accessRoleService.addPrivileges(
                accessRoleId,
                newPrivileges.getPrivileges().stream()
                        .map(p -> PrivilegeBuilder.builder()
                                .key(p.getKey())
                                .ops(p.getOps())
                        )
                        .collect(Collectors.toSet())
        ) ;

        return ResponseEntity.ok(accessRoleDto);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class PrivilegeSet {
        List<PrivilegeRequest> privileges;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class PrivilegeRequest {
        String key;
        AccessOp[] ops;
    }

}
