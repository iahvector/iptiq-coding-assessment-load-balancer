package com.islamhassan.iptiq.loadbalancer;


import com.islamhassan.iptiq.loadbalancer.datastores.ProviderMetaDataRepository;
import com.islamhassan.iptiq.loadbalancer.providers.Provider;

import java.util.ArrayList;

public class RoundRobinLoadBalancer<T> extends BaseLoadBalancer<T> {
    private ArrayList<String> providerKeys = new ArrayList<>();
    private int pointer = 0;

    public RoundRobinLoadBalancer(ProviderMetaDataRepository repo, int maxProvidersCount, long heartBeatPeriodSeconds) {
        super(repo, maxProvidersCount, heartBeatPeriodSeconds);
    }

    public RoundRobinLoadBalancer(ProviderMetaDataRepository repo) {
        super(repo);
    }

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
    protected void onProviderEnabled(Provider<T> provider) {
        providerKeys.add(provider.getId());
    }

    @Override
    protected void onProviderDisabled(Provider<T> provider) {
        providerKeys.remove(provider.getId());
    }
}
