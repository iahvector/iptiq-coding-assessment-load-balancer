package com.islamhassan.iptiq.loadbalancer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseLoadBalancerTest {
    public static class BaseLoadBalancerConcrete<T> extends BaseLoadBalancer<T> {

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
        var lb = new BaseLoadBalancerConcrete<String>();

        assertThrows(NoProvidersAvailableException.class, lb::get);
    }

    @Test
    public void whenProviderRegistered_providerIsEnabled() {
        var id = "1";
        var lb = new BaseLoadBalancerConcrete<String>();
        lb.registerProvider(new ProviderExample(id));
        assertTrue(lb.isProviderEnabled(id));
        assertEquals(1, lb.getEnabledProvidersCount());
    }

    @Test
    public void whenNoEnabledProviders_throwException() {
        var id = "1";
        var lb = new BaseLoadBalancerConcrete<String>();
        lb.registerProvider(new ProviderExample(id));
        lb.disableProvider(id);
        assertEquals(1, lb.getProvidersCount());
        assertEquals(0, lb.getEnabledProvidersCount());
        assertThrows(NoProvidersAvailableException.class, lb::get);
    }

    @Test
    public void testProviderReenabled() {
        var id = "1";
        var lb = new BaseLoadBalancerConcrete<String>();
        lb.registerProvider(new ProviderExample(id));
        lb.disableProvider(id);

        assertThrows(NoProvidersAvailableException.class, lb::get);
        lb.enableProvider(id);
        assertTrue(lb.isProviderEnabled(id));
    }

    @Test
    public void whenMoreProvidersThanMaxCountRegistered_throwException() {
        var lb = new BaseLoadBalancerConcrete<String>();
        for (int i = 0; i < lb.getMaxProvidersCount(); i++) {
            lb.registerProvider(new ProviderExample(Integer.toString(i)));
        }

        assertThrows(MaxProvidersCountReachedException.class,
                () -> lb.registerProvider(new ProviderExample(Integer.toString(lb.getMaxProvidersCount())))
        );
    }
}