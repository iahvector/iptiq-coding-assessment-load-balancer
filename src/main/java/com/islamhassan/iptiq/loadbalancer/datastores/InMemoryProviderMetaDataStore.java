package com.islamhassan.iptiq.loadbalancer.datastores;

import com.islamhassan.iptiq.loadbalancer.exceptions.ProviderNotFoundException;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryProviderMetaDataStore implements ProviderMetaDataRepository {
    private final ConcurrentHashMap<String, ProviderMetaData> store = new ConcurrentHashMap<>();

    @Override
    public void addProvider(String providerId, ProviderMetaData data) {
        store.put(providerId, data);
    }

    @Override
    public ProviderMetaData removeProvider(String providerId) {
        checkProviderExists(providerId);
        return store.remove(providerId);
    }

    @Override
    public ProviderMetaData getProvider(String providerId) {
        checkProviderExists(providerId);
        return store.get(providerId);
    }

    @Override
    public void enableProvider(String providerId) {
        checkProviderExists(providerId);

        store.computeIfPresent(providerId, (String id, ProviderMetaData data) -> {
            data.setEnabled(true);
            return data;
        });
    }

    @Override
    public void disableProvider(String providerId) {
        checkProviderExists(providerId);

        store.computeIfPresent(providerId, (String id, ProviderMetaData data) -> {
            data.setEnabled(false);
            return data;
        });
    }

    @Override
    public boolean isProviderEnabled(String providerId) {
        checkProviderExists(providerId);

        return store.get(providerId).isEnabled();
    }

    @Override
    public long getEnabledProvidersCount() {
        return store.values().stream().filter(ProviderMetaData::isEnabled).count();
    }

    @Override
    public int getProviderConsecutiveChecksCount(String providerId) {
        checkProviderExists(providerId);
        return store.get(providerId).getConsecutiveChecksCount();
    }

    @Override
    public int incrementProviderConsecutiveChecksCount(String providerId) {
        checkProviderExists(providerId);
        ProviderMetaData updated = store.computeIfPresent(providerId, (String id, ProviderMetaData data) -> {
            data.setConsecutiveChecksCount(data.getConsecutiveChecksCount() + 1);
            return data;
        });
        return updated.getConsecutiveChecksCount();
    }

    @Override
    public void resetProviderConsecutiveChecksCount(String providerId) {
        checkProviderExists(providerId);
        store.computeIfPresent(providerId, (String id, ProviderMetaData data) -> {
            data.setConsecutiveChecksCount(0);
            return data;
        });
    }

    void checkProviderExists(String providerId) {
        if (!store.containsKey(providerId)) {
            throw new ProviderNotFoundException("Provider id: " + providerId);
        }
    }
}
