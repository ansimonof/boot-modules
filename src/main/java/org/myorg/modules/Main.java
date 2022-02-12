package org.myorg.modules;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = "org.myorg"
)
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
