package com.islamhassan.iptiq.loadbalancer;

import java.util.ArrayList;

public class RandomLoadBalancer<T> extends BaseLoadBalancer<T> {
    private final ArrayList<String> providerKeys = new ArrayList<>();

    @Override
    protected void onProviderEnabled(Provider<T> provider) {
        providerKeys.add(provider.getId());
    }

    @Override
    protected void onProviderDisabled(Provider<T> provider) {
        providerKeys.remove(provider.getId());
    }

    private int randomInt() {
        return (int) (Math.random() * providerKeys.size());
    }

    @Override
    protected Provider<T> getNextProvider() {
        return enabledProviders.get(providerKeys.get(randomInt()));
    }
}
