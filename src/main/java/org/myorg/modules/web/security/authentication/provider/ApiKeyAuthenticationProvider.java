package org.myorg.modules.web.security.authentication.provider;

import org.myorg.modules.access.context.ApiKeyContext;
import org.myorg.modules.access.context.source.ApiKeySource;
import org.myorg.modules.access.privilege.PrivilegePair;
import org.myorg.modules.access.privilege.getter.PrivilegeGetter;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleDto;
import org.myorg.modules.modules.core.database.service.apikey.ApiKeyDto;
import org.myorg.modules.modules.core.database.service.apikey.ApiKeyService;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.web.security.authentication.token.ApiKeyAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ApiKeyAuthenticationProvider implements CustomAuthenticationProvider {

    private final ApiKeyService apiKeyService;
    private final PrivilegeGetter privilegeGetter;

    @Autowired
    public ApiKeyAuthenticationProvider(ApiKeyService apiKeyService, PrivilegeGetter privilegeGetter) {
        this.apiKeyService = apiKeyService;
        this.privilegeGetter = privilegeGetter;
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
        Set<AccessRoleDto> accessRoleDtos = apiKeyService.findAllAccessRoles(apiKeyDto.getId());
        return privilegeGetter.mergeAccessRoles(accessRoleDtos);
    }

    private ApiKeyContext createContext(ApiKeyDto apiKeyDto, Set<PrivilegePair> privileges) {
        ApiKeySource source = new ApiKeySource(apiKeyDto.getId());
        return new ApiKeyContext(source, privileges);
    }

}
