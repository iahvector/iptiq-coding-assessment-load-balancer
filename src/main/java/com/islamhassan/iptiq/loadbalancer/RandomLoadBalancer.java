package com.islamhassan.iptiq.loadbalancer;

import java.util.ArrayList;

public class RandomLoadBalancer<T> extends BaseLoadBalancer<T> {
    private final ArrayList<String> providerKeys = new ArrayList<>();

    @Override
    protected void onProviderRegistered(Provider<T> provider) {
        providerKeys.add(provider.getId());
    }

    private int randomInt() {
        return (int) (Math.random() * providerKeys.size());
    }

    @Override
    protected Provider<T> getNextProvider() {
        return providers.get(providerKeys.get(randomInt()));
    }
}
