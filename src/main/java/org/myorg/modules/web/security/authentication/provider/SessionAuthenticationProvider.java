package org.myorg.modules.web.security.authentication.provider;

import org.myorg.modules.access.privilege.AccessOpCollection;
import org.myorg.modules.access.privilege.PrivilegePair;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleDto;
import org.myorg.modules.modules.core.database.service.accessrole.PrivilegeDto;
import org.myorg.modules.modules.core.database.service.user.UserDto;
import org.myorg.modules.modules.core.database.service.user.UserService;
import org.myorg.modules.web.security.authentication.token.SessionAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SessionAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String session = (String) authentication.getCredentials();
//
//        UserDto byEmail = userService.findByUsername(session);
//        if (byEmail == null) {
//            throw new SessionAuthenticationException("session: " + session);
//        }
//
//        AccessRoleDto accessRoleDto = userService.getAccessRole(byEmail.getId());
//
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        if (accessRoleDto != null) {
//            for (PrivilegeDto privilege : accessRoleDto.getPrivileges()) {
//                authorities.add(
//                        new PrivilegePair(
//                                privilege.getKey(),
//                                new AccessOpCollection(privilege.getValue())
//                        )
//                );
//            }
//        }
//
//        return new SessionAuthenticationToken(session, authorities, null);
//
      return null;  }

    @Override
    public boolean supports(Class<?> authentication) {
        return SessionAuthenticationToken.class.equals(authentication);
    }
}
