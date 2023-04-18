package com.islamhassan.iptiq.loadbalancer;

public class RandomLoadBalancer<T> extends BaseLoadBalancer<T> {
    private String[] providerKeys = new String[]{};

    @Override
    public T get() {
        if (providers.isEmpty()) {
            throw new NoProvidersRegisteredException();
        }

        return getNextProvider().get();
    }

    @Override
    protected void onProviderRegistered(Provider<T> provider) {
        providerKeys = providers.keySet().toArray(providerKeys);
    }

    private int randomInt() {
        return (int) (Math.random() * providerKeys.length);
    }

    private Provider<T> getNextProvider() {
        return providers.get(providerKeys[randomInt()]);
    }
}
