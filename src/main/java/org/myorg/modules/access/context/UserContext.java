package org.myorg.modules.access.context;

import org.myorg.modules.access.context.source.UserSource;
import org.myorg.modules.access.privilege.PrivilegePair;

import java.util.Set;

public class UserContext extends AuthorizedContext<UserSource> {

    public UserContext(UserSource source, Set<PrivilegePair> privileges) {
        super(source, privileges);
    }
}
