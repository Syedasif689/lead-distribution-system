package com.project.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class DatabaseUrlProperties {
    private static final String[] DATABASE_URL_KEYS = {
        "SPRING_DATASOURCE_URL",
        "DATABASE_URL",
        "JDBC_DATABASE_URL",
        "POSTGRES_URL",
        "DATABASE_PRIVATE_URL"
    };

    private DatabaseUrlProperties() {
    }

    public static Map<String, Object> fromEnv(Function<String, String> environment) {
        for (String key : DATABASE_URL_KEYS) {
            Map<String, Object> properties = from(environment.apply(key));
            if (!properties.isEmpty()) {
                return properties;
            }
        }

        return Map.of();
    }

    public static Map<String, Object> from(String databaseUrl) {
        Map<String, Object> properties = new HashMap<>();

        if (databaseUrl == null || databaseUrl.isBlank()) {
            return properties;
        }

        if (databaseUrl.startsWith("jdbc:postgresql://")) {
            properties.put("spring.datasource.url", databaseUrl);
            return properties;
        }

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

        return properties;
    }

    private static int resolvePort(URI uri) {
        return uri.getPort() == -1 ? 5432 : uri.getPort();
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
