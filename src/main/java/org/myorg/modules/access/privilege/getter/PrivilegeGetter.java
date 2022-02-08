package org.myorg.modules.access.privilege.getter;

import org.myorg.modules.access.privilege.AbstractPrivilege;
import org.myorg.modules.access.privilege.AccessOpCollection;
import org.myorg.modules.access.privilege.PrivilegePair;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleDto;
import org.myorg.modules.modules.core.database.service.accessrole.PrivilegeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrivilegeGetter {

    private List<? extends AbstractPrivilege> privileges;

    @Autowired
    public PrivilegeGetter(List<? extends ModulePrivilegeGetter> privilegeGetters) {
        this.privileges = privilegeGetters.stream()
                .flatMap(o -> o.getPrivileges().stream())
                .collect(Collectors.toList());
    }


    public List<? extends AbstractPrivilege> getAllPrivileges() {
        return privileges;
    }

    public Set<PrivilegePair> mergeAccessRoles(Set<AccessRoleDto> accessRoleDtos) {
        Map<String, Integer> keyToValueMap = new HashMap<>();
        for (AccessRoleDto accessRoleDto : accessRoleDtos) {
            for (PrivilegeDto privilegeDto : accessRoleDto.getPrivileges()) {
                AccessOpCollection accessOpCollection = new AccessOpCollection(privilegeDto.getOps());
                keyToValueMap.compute(
                        privilegeDto.getKey(),
                        (key, value) -> value == null ? accessOpCollection.getValue() : value ^ accessOpCollection.getValue()
                );
            }
        }

        Set<PrivilegePair> privileges = new HashSet<>();
        keyToValueMap.forEach((key, value) -> privileges.add(new PrivilegePair(key, new AccessOpCollection(value))));

        return privileges;
    }
}
