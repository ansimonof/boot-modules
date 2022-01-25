package org.myorg.modules.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Modules {

    private List<? extends Module> modules;

    @Autowired
    public Modules(List<? extends Module> modules) {
        this.modules = modules;
    }

    @PostConstruct
    private void init() {
        modules = modules.stream()
                .sorted((o1, o2) -> {
                    if (!o2.getClass().isAnnotationPresent(Priority.class)) {
                        return -1;
                    } else if (!o1.getClass().isAnnotationPresent(Priority.class)) {
                        return 1;
                    } else {
                        int value1 = o1.getClass().getAnnotation(Priority.class).value();
                        int value2 = o2.getClass().getAnnotation(Priority.class).value();
                        return Integer.compare(value1, value2);
                    }
                })
                .collect(Collectors.toList());
        modules.forEach(Module::init);
    }

    @PreDestroy
    private void destroy() {
        for (int i = modules.size() - 1; i > -1; i--) {
            modules.get(i).destroy();
        }
    }
}
