package com.islamhassan.iptiq.loadbalancer;

import com.islamhassan.iptiq.loadbalancer.datastores.ProviderMetaData;
import com.islamhassan.iptiq.loadbalancer.datastores.ProviderMetaDataRepository;
import com.islamhassan.iptiq.loadbalancer.exceptions.MaxProvidersCountReachedException;
import com.islamhassan.iptiq.loadbalancer.exceptions.NoProvidersAvailableException;
import com.islamhassan.iptiq.loadbalancer.providers.Provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BaseLoadBalancer<T> {
    public static final int DEFAULT_MAX_PROVIDERS_COUNT = 10;
    public static final int DEFAULT_HEART_BEAT_PERIOD_SECONDS = 60;

    protected int maxProvidersCount;
    private final long heartBeatPeriodSeconds;

    protected ConcurrentHashMap<String, Provider<T>> providers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService heartBeatExecutor;
    private final ProviderMetaDataRepository metaDataRepository;

    public BaseLoadBalancer(ProviderMetaDataRepository repo, int maxProvidersCount, long heartBeatPeriodSeconds) {
        this.maxProvidersCount = maxProvidersCount;
        this.heartBeatPeriodSeconds = heartBeatPeriodSeconds;

        this.heartBeatExecutor = Executors.newSingleThreadScheduledExecutor();
        this.metaDataRepository = repo;
    }

    public BaseLoadBalancer(ProviderMetaDataRepository repo) {
        this(repo, DEFAULT_MAX_PROVIDERS_COUNT, DEFAULT_HEART_BEAT_PERIOD_SECONDS);
    }

    public void startHearBeatChecker() {
        heartBeatExecutor.scheduleAtFixedRate(() -> {
            providers.values().parallelStream().forEach((Provider<T> p)->{
                if (!p.check()) {
                    disableProvider(p.getId());
                }
            });
        }, 0, heartBeatPeriodSeconds, TimeUnit.SECONDS);
    }

    public void stopHeartBeatChecker() {
        heartBeatExecutor.shutdown();
    }

    public void registerProvider(Provider<T> provider) {
        if (providers.size()  >= maxProvidersCount) {
            throw new MaxProvidersCountReachedException("Max providers count: " + maxProvidersCount);
        }

        providers.put(provider.getId(), provider);
        metaDataRepository.addProvider(provider.getId(), new ProviderMetaData(true));
        onProviderEnabled(provider);
    }

    public void enableProvider(String id) {
        metaDataRepository.enableProvider(id);
        onProviderEnabled(providers.get(id));
    }

    public void disableProvider(String id) {
        metaDataRepository.disableProvider(id);
        onProviderDisabled(providers.get(id));
    }

    public boolean isProviderEnabled(String id) {
        return metaDataRepository.isProviderEnabled(id);
    }

    public int getProvidersCount() {
        return providers.size();
    }

    public long getEnabledProvidersCount() {
        return metaDataRepository.getEnabledProvidersCount();
    }

    public int getMaxProvidersCount() {
        return maxProvidersCount;
    }

    public T get() {
        if (getEnabledProvidersCount() == 0) {
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
