package org.myorg.modules.modules;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModuleConfig {

    private final String uuid;
    private final Set<Class<? extends Module>> dependencies;

    public ModuleConfig(String uuid, Set<Class<? extends Module>> dependencies) {
        this.uuid = uuid;
        this.dependencies = dependencies;
    }

    public ModuleConfig(String uuid, Class<? extends Module>[] dependencies) {
        this(uuid, Stream.of(dependencies).collect(Collectors.toSet()));
    }

    public String getUuid() {
        return uuid;
    }

    public Set<Class<? extends Module>> getDependencies() {
        return dependencies;
    }
}
