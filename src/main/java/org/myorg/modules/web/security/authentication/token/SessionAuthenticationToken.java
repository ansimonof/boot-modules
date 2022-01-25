package org.myorg.modules.web.security.authentication.token;

import org.myorg.modules.access.context.UserContext;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

public class SessionAuthenticationToken extends CustomAbstractAuthenticationToken {

    private String session;
    private UserContext context;

    public SessionAuthenticationToken(String session,
                                      Collection<? extends GrantedAuthority> authorities,
                                      UserContext context) {
        super(authorities);
        this.session = session;
        this.context = context;
    }

    public SessionAuthenticationToken(String session) {
        super(new HashSet<>());
        this.session = session;
    }

    @Override
    public Object getCredentials() {
        return session;
    }

    @Override
    public Object getPrincipal() {
        return context;
    }
}
