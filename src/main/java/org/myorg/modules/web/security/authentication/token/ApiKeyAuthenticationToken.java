package org.myorg.modules.web.security.authentication.token;

import org.myorg.modules.access.context.ApiKeyContext;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

public class ApiKeyAuthenticationToken extends CustomAbstractAuthenticationToken {

    private String apiKey;
    private ApiKeyContext context;

    public ApiKeyAuthenticationToken(String apiKey,
                                     Collection<? extends GrantedAuthority> authorities,
                                     ApiKeyContext context) {
        super(authorities);
        this.apiKey = apiKey;
        this.context = context;
    }

    public ApiKeyAuthenticationToken(String apiKey) {
        this(apiKey, new HashSet<>(), null);
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getPrincipal() {
        return context;
    }
}
