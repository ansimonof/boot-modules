package org.myorg.modules.modules;

import org.myorg.modules.modules.exception.ModuleException;

public abstract class Module {

    public abstract void init() throws ModuleException;

    public abstract void destroy() throws ModuleException;
}
