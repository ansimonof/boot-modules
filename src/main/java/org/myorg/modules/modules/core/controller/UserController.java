package org.myorg.modules.modules.core.controller;

import org.myorg.modules.access.AccessPermission;
import org.myorg.modules.access.context.AuthorizedContext;
import org.myorg.modules.access.context.UnauthorizedContext;
import org.myorg.modules.access.privilege.AccessOp;
import org.myorg.modules.modules.core.CoreModuleConsts;
import org.myorg.modules.modules.core.access.privilege.UserManagementPrivilege;
import org.myorg.modules.modules.core.database.service.user.UserBuilder;
import org.myorg.modules.modules.core.database.service.user.UserDto;
import org.myorg.modules.modules.core.database.service.user.UserService;
import org.myorg.modules.modules.exception.ModuleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(CoreModuleConsts.REST_CONTROLLER_PREFIX + "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    @AccessPermission(
            context = UnauthorizedContext.class
    )
    public ResponseEntity<UserDto> registration(
            @RequestParam String username,
            @RequestParam String passwordHash
    ) throws ModuleException {
        UserDto user = userService.create(
                UserBuilder.builder()
                        .username(username)
                        .passwordHash(passwordHash)
                        .isEnabled(true)
                        .isAdmin(false)
        );
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = UserManagementPrivilege.class,
            ops = { AccessOp.READ }
    )
    public ResponseEntity<UserDto> findById(@RequestParam long id) throws ModuleException {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/access_role/add")
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = UserManagementPrivilege.class,
            ops = { AccessOp.WRITE }
    )
    public ResponseEntity<Boolean> addAccessRole(
            @RequestParam long userId,
            @RequestParam long accessRoleId
    ) throws ModuleException {
        userService.addAccessRole(userId, accessRoleId);
        return ResponseEntity.ok(true);
    }
}
