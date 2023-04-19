package com.islamhassan.iptiq.loadbalancer;


import java.util.ArrayList;

public class RoundRobinLoadBalancer<T> extends BaseLoadBalancer<T> {
    private ArrayList<String> providerKeys = new ArrayList<>();
    private int pointer = 0;

    @Override
    protected Provider<T> getNextProvider() {
        if (pointer >= providerKeys.size()) {
            pointer = 0;
        }

        String key = providerKeys.get(pointer);
        pointer++;

        return enabledProviders.get(key);
    }

    @Override
    protected void onProviderEnabled(Provider<T> provider) {
        providerKeys.add(provider.getId());
    }

    @Override
    protected void onProviderDisabled(Provider<T> provider) {
        providerKeys.remove(provider.getId());
    }
}
