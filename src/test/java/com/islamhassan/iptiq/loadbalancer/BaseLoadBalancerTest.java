package com.islamhassan.iptiq.loadbalancer;

import com.islamhassan.iptiq.loadbalancer.datastores.InMemoryProviderMetaDataStore;
import com.islamhassan.iptiq.loadbalancer.datastores.ProviderMetaDataRepository;
import com.islamhassan.iptiq.loadbalancer.exceptions.MaxProvidersCountReachedException;
import com.islamhassan.iptiq.loadbalancer.exceptions.NoProvidersAvailableException;
import com.islamhassan.iptiq.loadbalancer.providers.Provider;
import com.islamhassan.iptiq.loadbalancer.providers.ProviderExample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseLoadBalancerTest {
    public static class BaseLoadBalancerConcrete<T> extends BaseLoadBalancer<T> {

        public BaseLoadBalancerConcrete(ProviderMetaDataRepository repo, int maxProvidersCount, long heartBeatPeriodSeconds) {
            super(repo, maxProvidersCount, heartBeatPeriodSeconds);
        }

        public BaseLoadBalancerConcrete(ProviderMetaDataRepository repo) {
            super(repo);
        }

        @Override
        protected Provider<T> getNextProvider() {
            return null;
        }

        @Override
        protected void onProviderEnabled(Provider<T> provider) {
            // Do Nothing
        }

        @Override
        protected void onProviderDisabled(Provider<T> provider) {
            // Do Nothing
        }
    }

    @Test
    public void whenNoProviders_throwException() {
        var lb = new BaseLoadBalancerConcrete<String>(new InMemoryProviderMetaDataStore());

        assertThrows(NoProvidersAvailableException.class, lb::get);
    }

    @Test
    public void whenProviderRegistered_providerIsEnabled() {
        var id = "1";
        var lb = new BaseLoadBalancerConcrete<String>(new InMemoryProviderMetaDataStore());
        lb.registerProvider(new ProviderExample(id));
        assertTrue(lb.isProviderEnabled(id));
        assertEquals(1, lb.getEnabledProvidersCount());
    }

    @Test
    public void whenNoEnabledProviders_throwException() {
        var id = "1";
        var lb = new BaseLoadBalancerConcrete<String>(new InMemoryProviderMetaDataStore());
        lb.registerProvider(new ProviderExample(id));
        lb.disableProvider(id);
        assertEquals(1, lb.getProvidersCount());
        assertEquals(0, lb.getEnabledProvidersCount());
        assertThrows(NoProvidersAvailableException.class, lb::get);
    }

    @Test
    public void testProviderReenabled() {
        var id = "1";
        var lb = new BaseLoadBalancerConcrete<String>(new InMemoryProviderMetaDataStore());
        lb.registerProvider(new ProviderExample(id));
        lb.disableProvider(id);

        assertThrows(NoProvidersAvailableException.class, lb::get);
        lb.enableProvider(id);
        assertTrue(lb.isProviderEnabled(id));
    }

    @Test
    public void whenMoreProvidersThanMaxCountRegistered_throwException() {
        var lb = new BaseLoadBalancerConcrete<String>(new InMemoryProviderMetaDataStore());
        for (int i = 0; i < lb.getMaxProvidersCount(); i++) {
            lb.registerProvider(new ProviderExample(Integer.toString(i)));
        }

        assertThrows(MaxProvidersCountReachedException.class,
                () -> lb.registerProvider(new ProviderExample(Integer.toString(lb.getMaxProvidersCount())))
        );
    }

    @Test
    public void whenHeartBeatCheckFails_providerIsDisabled() {
        var failingProviderId = "1";
        var workingProviderId = "2";
        var failingProvider = new ProviderExample(failingProviderId);
        var workingProvider = new ProviderExample(workingProviderId);
        var heartBeatPeriodSeconds = 1;
        var lb = new BaseLoadBalancerConcrete<String>(new InMemoryProviderMetaDataStore(), 10, heartBeatPeriodSeconds);
        lb.registerProvider(failingProvider);
        lb.registerProvider(workingProvider);
        lb.startHearBeatChecker();
        var waitTime = (heartBeatPeriodSeconds * 1000) + 100;

        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertTrue(lb.isProviderEnabled(failingProviderId));
        assertTrue(lb.isProviderEnabled(workingProviderId));

        failingProvider.setReady(false);

        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertFalse(lb.isProviderEnabled(failingProviderId));
        assertTrue(lb.isProviderEnabled(workingProviderId));
        assertEquals(1, lb.getEnabledProvidersCount());

        lb.stopHeartBeatChecker();
    }
}