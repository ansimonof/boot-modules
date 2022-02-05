package org.myorg.modules.modules.core.controller;

import org.myorg.modules.access.AccessPermission;
import org.myorg.modules.access.context.UnauthorizedContext;
import org.myorg.modules.modules.core.CoreModuleConsts;
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
@RequestMapping(CoreModuleConsts.REST_CONTROLLER_PREFIX)
public class CoreController {

    @Autowired
    private UserService userService;

    @PostMapping("/init")
    @AccessPermission(
            context = UnauthorizedContext.class
    )
    public ResponseEntity<UserDto> init(
            @RequestParam("username") String username,
            @RequestParam("password_hash") String passwordHash
    ) throws ModuleException {
        UserDto user = userService.create(
                UserBuilder.builder()
                .username(username)
                .passwordHash(passwordHash)
                .isEnabled(true)
                .isAdmin(true)
        );
        return ResponseEntity.ok(user);
    }
}
