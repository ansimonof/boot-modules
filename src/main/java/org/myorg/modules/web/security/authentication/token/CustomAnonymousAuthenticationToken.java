package org.myorg.modules.web.security.authentication.token;

import org.myorg.modules.access.context.UnauthorizedContext;
import org.myorg.modules.access.context.source.AnonymousSource;

import java.util.HashSet;

public class CustomAnonymousAuthenticationToken extends CustomAbstractAuthenticationToken {

    private UnauthorizedContext<?> context;

    public CustomAnonymousAuthenticationToken() {
        super(new HashSet<>());
        this.context = new UnauthorizedContext<>(new AnonymousSource());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return context;
    }
}
