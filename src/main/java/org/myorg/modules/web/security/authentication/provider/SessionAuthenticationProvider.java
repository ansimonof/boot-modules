package org.myorg.modules.web.security.authentication.provider;

import org.myorg.modules.access.privilege.AccessOpCollection;
import org.myorg.modules.access.privilege.PrivilegePair;
import org.myorg.modules.modules.core.domainobjects.DbAccessRole;
import org.myorg.modules.modules.core.domainobjects.DbPrivilege;
import org.myorg.modules.modules.core.domainobjects.DbUser;
import org.myorg.modules.web.security.authentication.token.SessionAuthenticationToken;
import org.myorg.modules.modules.core.service.database.user.DbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SessionAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private DbUserService dbUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String session = (String) authentication.getCredentials();

        DbUser byEmail = dbUserService.findByUsername(session);
        if (byEmail == null) {
            throw new SessionAuthenticationException("session: " + session);
        }

        DbAccessRole accessRole = byEmail.getAccessRole();

        Set<GrantedAuthority> authorities = new HashSet<>();
        if (accessRole != null) {
            for (DbPrivilege privilege : accessRole.getPrivileges()) {
                authorities.add(
                        new PrivilegePair(
                                privilege.getKey(),
                                new AccessOpCollection(privilege.getValue())
                        )
                );
            }
        }

        return new SessionAuthenticationToken(session, authorities, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SessionAuthenticationToken.class.equals(authentication);
    }
}
