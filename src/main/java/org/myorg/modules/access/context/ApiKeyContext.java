package org.myorg.modules.access.context;

import org.myorg.modules.access.context.source.ApiKeySource;
import org.myorg.modules.access.privilege.PrivilegePair;

import java.util.Set;

public class ApiKeyContext extends AuthorizedContext<ApiKeySource> {

    public ApiKeyContext(ApiKeySource source, Set<PrivilegePair> privileges) {
        super(source, privileges);
    }
}
