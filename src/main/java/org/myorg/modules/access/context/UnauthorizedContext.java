package org.myorg.modules.access.context;

import org.myorg.modules.access.context.source.Source;

public class UnauthorizedContext<S extends Source> extends Context<S> {

    public UnauthorizedContext(S source) {
        super(source);
    }
}
