package com.islamhassan.iptiq.loadbalancer;

import com.islamhassan.iptiq.loadbalancer.datastores.ProviderMetaDataRepository;
import com.islamhassan.iptiq.loadbalancer.providers.Provider;

import java.util.ArrayList;

public class RandomLoadBalancer<T> extends BaseLoadBalancer<T> {
    private final ArrayList<String> providerKeys = new ArrayList<>();

    public RandomLoadBalancer(ProviderMetaDataRepository repo, int maxProvidersCount, long heartBeatPeriodSeconds) {
        super(repo, maxProvidersCount, heartBeatPeriodSeconds);
    }

    public RandomLoadBalancer(ProviderMetaDataRepository repo) {
        super(repo);
    }

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
        return providers.get(providerKeys.get(randomInt()));
    }
}
