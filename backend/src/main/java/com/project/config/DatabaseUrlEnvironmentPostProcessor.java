package com.project.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final String PROPERTY_SOURCE_NAME = "databaseUrlProperties";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (environment.getProperty("SPRING_DATASOURCE_URL") != null) {
            return;
        }

        String databaseUrl = environment.getProperty("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            return;
        }

        Map<String, Object> properties = new HashMap<>();

        if (databaseUrl.startsWith("jdbc:postgresql://")) {
            properties.put("spring.datasource.url", databaseUrl);
        } else {
            URI uri = URI.create(databaseUrl);
            String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + resolvePort(uri) + uri.getPath();
            if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
                jdbcUrl += "?" + uri.getQuery();
            }
            properties.put("spring.datasource.url", jdbcUrl);

            String userInfo = uri.getUserInfo();
            if (userInfo != null) {
                String[] credentials = userInfo.split(":", 2);
                properties.put("spring.datasource.username", decode(credentials[0]));
                if (credentials.length > 1) {
                    properties.put("spring.datasource.password", decode(credentials[1]));
                }
            }
        }

        environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, properties));
    }

    private static int resolvePort(URI uri) {
        return uri.getPort() == -1 ? 5432 : uri.getPort();
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
