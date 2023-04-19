package com.islamhassan.iptiq.loadbalancer;

import java.util.HashMap;

public abstract class BaseLoadBalancer<T> {
    protected int maxProvidersCount;
    protected HashMap<String, Provider<T>> enabledProviders = new HashMap<>();
    protected HashMap<String, Provider<T>> disabledProviders = new HashMap<>();

    public BaseLoadBalancer(int maxProvidersCount) {
        this.maxProvidersCount = maxProvidersCount;
    }

    public BaseLoadBalancer() {
        this(10);
    }

    public void registerProvider(Provider<T> provider) {
        if (enabledProviders.size() + disabledProviders.size() >= maxProvidersCount) {
            throw new MaxProvidersCountReachedException("Max providers count: " + maxProvidersCount);
        }

        enabledProviders.put(provider.getId(), provider);
        onProviderEnabled(provider);
    }

    public void enableProvider(String id) {
        if (disabledProviders.containsKey(id)) {
            Provider<T> p = disabledProviders.remove(id);
            enabledProviders.put(id, p);
            onProviderEnabled(p);
        }
    }

    public void disableProvider(String id) {
        if (enabledProviders.containsKey(id)) {
            Provider<T> p = enabledProviders.remove(id);
            disabledProviders.put(id, p);
            onProviderDisabled(p);
        }
    }

    public boolean isProviderEnabled(String id) {
        return enabledProviders.containsKey(id);
    }

    public int getProvidersCount() {
        return enabledProviders.size() + disabledProviders.size();
    }

    public int getEnabledProvidersCount() {
        return enabledProviders.size();
    }

    public int getMaxProvidersCount() {
        return maxProvidersCount;
    }

    public T get() {
        if (enabledProviders.isEmpty()) {
            throw new NoProvidersAvailableException(
                    "Total providers: " + getProvidersCount() + ", Enabled providers: " + getEnabledProvidersCount()
            );
        }
        return getNextProvider().get();
    }

    protected abstract Provider<T> getNextProvider();
    protected abstract void onProviderEnabled(Provider<T> provider);
    protected abstract void onProviderDisabled(Provider<T> provider);
}
