package org.myorg.modules.utils;

import org.myorg.modules.modules.exception.ModuleException;

@FunctionalInterface
public interface Supplier<T> {

    T get() throws ModuleException;
}
