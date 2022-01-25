package org.myorg.modules.web.security.authorization;

import org.myorg.modules.access.context.Context;
import org.myorg.modules.access.privilege.PrivilegePair;
import org.myorg.modules.web.ControllerInfo;
import org.myorg.modules.web.ControllerMappingInfoInitializer;
import org.myorg.modules.web.security.authentication.token.CustomAbstractAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PrivilegeAccessDecisionVoter implements AccessDecisionVoter {

    private ControllerMappingInfoInitializer controllerMappingInfoInitializer;

    @Autowired
    public PrivilegeAccessDecisionVoter(ControllerMappingInfoInitializer controllerMappingInfoInitializer) {
        this.controllerMappingInfoInitializer = controllerMappingInfoInitializer;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {
        // Запрещаем выполнять запрос для других Authentication
        if (!(authentication instanceof CustomAbstractAuthenticationToken)) {
            return ACCESS_DENIED;
        }

        if (!authentication.isAuthenticated()) {
            return ACCESS_DENIED;
        }

        Method method = (Method) authentication.getDetails();
        ControllerInfo controllerInfo = controllerMappingInfoInitializer.getControllersInfo().get(method);
        if (controllerInfo == null) {
            //  Кинуть 404
        }


        // Чекаем контекст
        Context<?> context = (Context<?>) authentication.getPrincipal();
        Class<? extends Context> requestContextClazz = context.getClass();
        Class<? extends Context> controllerContextClazz = controllerInfo.getContext();
        if (!controllerContextClazz.isAssignableFrom(requestContextClazz)) {
            return ACCESS_DENIED;
        }

        // Чекаем привилегии
        Set<PrivilegePair> requestPrivileges = authentication.getAuthorities().stream()
                .map(authority -> (PrivilegePair) authority)
                .collect(Collectors.toSet());
        
        if (controllerInfo.getPrivilege() == null) {
            return ACCESS_GRANTED;    
        }
        
        PrivilegePair controllerPrivilege = new PrivilegePair(
                controllerInfo.getPrivilege().getKey(),
                controllerInfo.getAccessOps()
        );

        PrivilegePair appropriateRequestPrivilege = requestPrivileges.stream()
                .filter(privilegePair -> Objects.equals(privilegePair.getKey(), controllerPrivilege.getKey()))
                .findAny()
                .orElse(null);

        if (appropriateRequestPrivilege != null
                && appropriateRequestPrivilege.getAccessOpCollection().contains(controllerPrivilege.getAccessOpCollection().getValue())) {
            return ACCESS_GRANTED;
        }

        return ACCESS_DENIED;
    }
}
