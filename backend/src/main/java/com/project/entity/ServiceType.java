package com.project.entity;

public enum ServiceType {
    SERVICE_1("Service 1"),
    SERVICE_2("Service 2"),
    SERVICE_3("Service 3");

    private final String label;

    ServiceType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
