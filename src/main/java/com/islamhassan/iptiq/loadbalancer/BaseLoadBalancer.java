package com.islamhassan.iptiq.loadbalancer;

import java.util.HashMap;

public abstract class BaseLoadBalancer<T> {
    protected int maxProvidersCount;
    protected HashMap<String, Provider<T>> providers;

    public BaseLoadBalancer(int maxProvidersCount) {
        this.maxProvidersCount = maxProvidersCount;
        this.providers = new HashMap<>();
    }

    public BaseLoadBalancer() {
        this(10);
    }

    public void registerProvider(Provider<T> provider) {
        if (providers.size() >= maxProvidersCount) {
            throw new MaxProvidersCountReachedException("Max providers count: " + maxProvidersCount);
        }

        providers.put(provider.getId(), provider);
        onProviderRegistered(provider);
    }

    public int getProvidersCount() {
        return providers.size();
    }

    public int getMaxProvidersCount() {
        return maxProvidersCount;
    }

    public T get() {
        if (providers.isEmpty()) {
            throw new NoProvidersRegisteredException();
        }
        return getNextProvider().get();
    }

    protected abstract Provider<T> getNextProvider();
    protected abstract void onProviderRegistered(Provider<T> provider);
}
