package com.islamhassan.iptiq.loadbalancer.datastores;

public interface ProviderMetaDataRepository {
    void addProvider(String providerId, ProviderMetaData data);
    ProviderMetaData removeProvider(String providerId);
    ProviderMetaData getProvider(String providerId);

    void enableProvider(String providerId);
    void disableProvider(String providerId);
    boolean isProviderEnabled(String providerId);
    long getEnabledProvidersCount();

    int getProviderConsecutiveChecksCount(String providerId);
    int incrementProviderConsecutiveChecksCount(String providerId);
    void resetProviderConsecutiveChecksCount(String providerId);
}
