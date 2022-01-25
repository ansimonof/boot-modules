package org.myorg.modules.web.security.authentication.provider;

import org.myorg.modules.access.context.ApiKeyContext;
import org.myorg.modules.access.context.source.ApiKeySource;
import org.myorg.modules.access.privilege.AccessOpCollection;
import org.myorg.modules.access.privilege.PrivilegePair;
import org.myorg.modules.crypto.CryptoService;
import org.myorg.modules.modules.core.domainobjects.DbAccessRole;
import org.myorg.modules.modules.core.domainobjects.DbApiKey;
import org.myorg.modules.modules.core.domainobjects.DbPrivilege;
import org.myorg.modules.modules.core.service.database.apikey.DbApiKeyService;
import org.myorg.modules.web.security.authentication.token.ApiKeyAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    private DbApiKeyService dbApiKeyService;
    private CryptoService cryptoService;


    @Autowired
    public ApiKeyAuthenticationProvider(DbApiKeyService dbApiKeyService, CryptoService cryptoService) {
        this.dbApiKeyService = dbApiKeyService;
        this.cryptoService = cryptoService;
    }

    private Set<PrivilegePair> getPrivileges(DbApiKey dbApiKey) {
        DbAccessRole accessRole = dbApiKey.getAccessRole();
        Set<PrivilegePair> privileges = new HashSet<>();
        if (accessRole != null) {
            for (DbPrivilege privilege : accessRole.getPrivileges()) {
                privileges.add(
                        new PrivilegePair(privilege.getKey(), new AccessOpCollection(privilege.getValue()))
                );
            }
        }

        return privileges;
    }

    private ApiKeyContext createContext(DbApiKey dbApiKey, Set<PrivilegePair> privileges) {
        ApiKeySource source = new ApiKeySource(dbApiKey.getId());
        return new ApiKeyContext(source, privileges);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ApiKeyAuthenticationToken apiKeyAuth = (ApiKeyAuthenticationToken) authentication;
        if (apiKeyAuth.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Already authed");
        }

        String apiKey = (String) apiKeyAuth.getCredentials();

        byte[] encode = cryptoService.encode(apiKey);
        DbApiKey dbApiKey = dbApiKeyService.findByValue(encode);
        if (dbApiKey == null) {
            throw new BadCredentialsException("Bad API key: " + apiKey);
        }

        Set<PrivilegePair> privileges = getPrivileges(dbApiKey);
        apiKeyAuth = new ApiKeyAuthenticationToken(apiKey, privileges, createContext(dbApiKey, privileges));
        apiKeyAuth.setAuthenticated(true);

        return apiKeyAuth;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ApiKeyAuthenticationToken.class.equals(clazz);
    }
}
