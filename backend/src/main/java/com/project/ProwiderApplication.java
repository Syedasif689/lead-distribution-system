package com.project;

import com.project.config.DatabaseUrlProperties;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProwiderApplication {
    public static void main(String[] args) {
        applyDatabaseUrl();
        SpringApplication.run(ProwiderApplication.class, args);
    }

    private static void applyDatabaseUrl() {
        if (System.getenv("SPRING_DATASOURCE_URL") != null || System.getProperty("spring.datasource.url") != null) {
            return;
        }

        for (Map.Entry<String, Object> entry : DatabaseUrlProperties.from(System.getenv("DATABASE_URL")).entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue().toString());
        }
    }
}
