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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam("username") String username,
            @RequestParam("password_hash") String passwordHash
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

    @PostMapping("/access_role/add")
    @AccessPermission(
            context = AuthorizedContext.class,
            privilege = UserManagementPrivilege.class,
            ops = { AccessOp.WRITE }
    )
    public ResponseEntity<Boolean> addAccessRole(
            @RequestParam("user_id") long userId,
            @RequestParam("access_role_id") long accessRoleId
    ) throws ModuleException {
        userService.addAccessRole(userId, accessRoleId);
        return ResponseEntity.ok(true);
    }
}
