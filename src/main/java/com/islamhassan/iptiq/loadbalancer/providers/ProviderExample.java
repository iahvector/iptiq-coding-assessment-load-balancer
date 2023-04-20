package com.islamhassan.iptiq.loadbalancer.providers;

import com.islamhassan.iptiq.loadbalancer.providers.Provider;

public class ProviderExample implements Provider<String> {
    private final String id;
    private boolean ready;

    public ProviderExample(String id) {
        this.id = id;
        ready = true;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String get() {
        return id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean check() {
        return isReady();
    }
}
