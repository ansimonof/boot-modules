package org.myorg.modules.access.context;

import org.myorg.modules.access.context.source.Source;
import org.myorg.modules.access.privilege.AccessOpCollection;
import org.myorg.modules.access.privilege.PrivilegePair;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AuthorizedContext<S extends Source> extends Context<S> {

    private Map<String, AccessOpCollection> privileges;

    public AuthorizedContext(S source, Set<PrivilegePair> privileges) {
        super(source);
        this.privileges = privileges.stream().collect(
                Collectors.toMap(PrivilegePair::getKey, PrivilegePair::getAccessOpCollection)
        );
    }

    public Map<String, AccessOpCollection> getPrivileges() {
        return privileges;
    }
}
