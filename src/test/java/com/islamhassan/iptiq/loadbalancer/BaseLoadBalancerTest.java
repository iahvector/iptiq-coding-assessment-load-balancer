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
        protected void onProviderRegistered(Provider<T> provider) {
            // Do Nothing
        }
    }

    @Test
    public void whenNoProviders_throwException() {
        var lb = new BaseLoadBalancerConcrete<String>();

        assertThrows(NoProvidersRegisteredException.class, lb::get);
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