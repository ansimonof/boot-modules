package org.myorg.modules.web.security.authentication.provider;

import org.myorg.modules.web.security.authentication.token.CustomAnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AnonymousAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAnonymousAuthenticationToken anonymousAuth = (CustomAnonymousAuthenticationToken) authentication;
        if (anonymousAuth.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Already authenticated");
        }

        anonymousAuth.setAuthenticated(true);
        return anonymousAuth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAnonymousAuthenticationToken.class.equals(authentication);
    }
}
