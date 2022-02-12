package org.myorg.modules;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(
        scanBasePackages = "org.myorg"
)
@PropertySource(
        value = {
                "modules.application.properties"
        }
)
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
