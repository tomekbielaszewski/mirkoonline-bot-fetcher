package org.grizz;

import org.grizz.springconfig.MainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Starter {
    public static void main(String... args) {
        SpringApplication.run(MainConfig.class, args);
    }
}
