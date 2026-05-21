package com.project.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class DatabaseUrlProperties {
    private DatabaseUrlProperties() {
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
