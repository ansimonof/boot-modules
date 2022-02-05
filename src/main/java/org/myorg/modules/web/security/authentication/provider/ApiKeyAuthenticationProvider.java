package org.myorg.modules.web.security.authentication.provider;

import org.myorg.modules.access.context.ApiKeyContext;
import org.myorg.modules.access.context.source.ApiKeySource;
import org.myorg.modules.access.privilege.AccessOpCollection;
import org.myorg.modules.access.privilege.PrivilegePair;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleDto;
import org.myorg.modules.modules.core.database.service.accessrole.PrivilegeDto;
import org.myorg.modules.modules.core.database.service.apikey.ApiKeyDto;
import org.myorg.modules.modules.core.database.service.apikey.ApiKeyService;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.web.security.authentication.token.ApiKeyAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    private ApiKeyService apiKeyService;

    @Autowired
    public ApiKeyAuthenticationProvider(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            return auth(authentication);
        } catch (ModuleException e) {
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ApiKeyAuthenticationToken.class.equals(clazz);
    }

    private Authentication auth(Authentication authentication) throws ModuleException {
        ApiKeyAuthenticationToken apiKeyAuth = (ApiKeyAuthenticationToken) authentication;
        if (apiKeyAuth.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Already authenticated");
        }

        String apiKey = (String) apiKeyAuth.getCredentials();
        ApiKeyDto apiKeyDto = apiKeyService.findByValue(apiKey);

        if (apiKeyDto == null) {
            throw new BadCredentialsException("Bad API key: " + apiKey);
        }

        Set<PrivilegePair> privileges = getPrivileges(apiKeyDto);
        apiKeyAuth = new ApiKeyAuthenticationToken(apiKey, privileges, createContext(apiKeyDto, privileges));
        apiKeyAuth.setAuthenticated(true);

        return apiKeyAuth;
    }

    private Set<PrivilegePair> getPrivileges(ApiKeyDto apiKeyDto) throws ModuleException {
        AccessRoleDto accessRoleDto = apiKeyService.getAccessRole(apiKeyDto.getId());
        Set<PrivilegePair> privileges = new HashSet<>();
        if (accessRoleDto != null) {
            for (PrivilegeDto privilegeDto : accessRoleDto.getPrivileges()) {
                privileges.add(
                        new PrivilegePair(privilegeDto.getKey(), new AccessOpCollection(privilegeDto.getOps()))
                );
            }
        }

        return privileges;
    }

    private ApiKeyContext createContext(ApiKeyDto apiKeyDto, Set<PrivilegePair> privileges) {
        ApiKeySource source = new ApiKeySource(apiKeyDto.getId());
        return new ApiKeyContext(source, privileges);
    }

}
