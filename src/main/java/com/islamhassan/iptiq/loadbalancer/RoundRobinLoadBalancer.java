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

        return providers.get(key);
    }

    @Override
    protected void onProviderRegistered(Provider<T> provider) {
        providerKeys.add(provider.getId());
    }
}
