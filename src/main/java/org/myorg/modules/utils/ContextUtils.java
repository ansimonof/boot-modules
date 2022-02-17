package org.myorg.modules.utils;

import org.myorg.modules.access.context.Context;
import org.myorg.modules.access.context.source.Source;
import org.myorg.modules.access.context.source.SystemSource;

public class ContextUtils {

    public static Context<?> createSystemContext() {
        return new Context<Source>(new SystemSource()) {
            @Override
            public Source getSource() {
                return super.getSource();
            }
        };
    }
}
