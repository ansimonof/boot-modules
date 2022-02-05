package org.myorg.modules.access.privilege.getter;

import org.myorg.modules.access.privilege.AbstractPrivilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
