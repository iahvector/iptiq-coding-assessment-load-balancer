package com.islamhassan.iptiq.loadbalancer;

public class ProviderExample implements Provider<String> {
    private final String id;

    public ProviderExample(String id) {
        this.id = id;
    }

    @Override
    public String get() {
        return id;
    }

    @Override
    public String getId() {
        return id;
    }
}
