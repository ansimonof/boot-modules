package org.myorg.modules.modules;

import com.google.common.collect.Lists;
import org.myorg.modules.modules.exception.ModuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource(
        value = {
                "boot-properties/modules.application.properties",
                "boot-properties/database.application.properties"
        }
)
public class Modules {

    private static final Logger log = LoggerFactory.getLogger(Modules.class);

    private List<? extends Module> modules;

    @Autowired(required = false)
    public Modules(List<? extends Module> modules) {
        this.modules = modules;
    }

    @PostConstruct
    private void init() throws ModuleException {
        log.info("Modules are initializing");

        modules.forEach(module -> {
            Class<? extends Module> moduleClazz = module.getClass();
            if (!moduleClazz.isAnnotationPresent(BootModule.class)) {
                log.error("Module {} doesn't have @BootModule annotation", moduleClazz.getSimpleName());
                throw new RuntimeException();
            }

            BootModule bootModuleAnn = moduleClazz.getAnnotation(BootModule.class);
            String uuid = bootModuleAnn.uuid();
            Class<? extends Module>[] dependencies = bootModuleAnn.dependencies();
            module.config = new ModuleConfig(uuid, dependencies);
        });

        checkAndSortModuleDependencies();

        for (Module module : modules) {
            module.init();
        }

        log.info("Module are initialized ({})", modules);
    }

    @PreDestroy
    private void destroy() throws ModuleException {
        log.info("Modules are destroying...");

        for (Module module : Lists.reverse(modules)) {
            module.destroy();
        }
    }

    private void checkAndSortModuleDependencies() {
        List<Module> result = new ArrayList<>();
        while (result.size() != modules.size()) {
            Module next = null;
            for (Module module : modules) {
                if (!result.contains(module)) {
                    boolean isNext = modules.stream().allMatch(
                            m -> !module.config.getDependencies().contains(m.getClass()) || result.contains(m));
                    if (isNext) {
                        next = module;
                        break;
                    }
                }
            }

            if (next == null) {
                throw new RuntimeException("Modules have circular dependencies");
            }

            result.add(next);
        }

        modules = result;
    }
}
