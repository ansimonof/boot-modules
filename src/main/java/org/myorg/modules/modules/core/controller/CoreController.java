package org.myorg.modules.modules.core.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myorg.modules.access.AccessPermission;
import org.myorg.modules.access.context.UnauthorizedContext;
import org.myorg.modules.modules.core.CoreModuleConsts;
import org.myorg.modules.modules.core.database.service.user.UserBuilder;
import org.myorg.modules.modules.core.database.service.user.UserDto;
import org.myorg.modules.modules.core.database.service.user.UserService;
import org.myorg.modules.modules.exception.ModuleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(CoreModuleConsts.REST_CONTROLLER_PREFIX)
public class CoreController {

    private final UserService userService;

    @Autowired
    public CoreController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/init")
    @AccessPermission(
            context = UnauthorizedContext.class
    )
    public ResponseEntity<UserDto> init(
            @RequestBody final InitForm initForm
    ) throws ModuleException {
        UserDto user = userService.create(
                UserBuilder.builder()
                .username(initForm.username)
                .passwordHash(initForm.passwordHash)
                .isEnabled(true)
                .isAdmin(true)
        );
        return ResponseEntity.ok(user);
    }

    @Data
    @NoArgsConstructor
    private static class InitForm {
        @JsonProperty("username")
        private String username;
        @JsonProperty("password_hash")
        private String passwordHash;
    }
}
