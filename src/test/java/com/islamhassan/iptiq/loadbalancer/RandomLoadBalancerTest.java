package com.islamhassan.iptiq.loadbalancer;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLoadBalancerTest {
    @Test
    void whenGetIsCalled_returnResultFromARandomProvider() {
        var lb = new RandomLoadBalancer<String>();
        for (int i = 0; i < 10; i++) {
            lb.registerProvider(new ProviderExample(Integer.toString(i)));
        }

        var res = new HashSet<String>();
        for (int i = 0; i < 10; i++) {
            res.add(lb.get());
        }
        assertTrue(res.size() > 1);
    }

    @Test
    void whenNoProviders_throwException() {
        var lb = new RandomLoadBalancer<String>();

        assertThrows(NoProvidersRegisteredException.class, lb::get);
    }

    @Test
    void whenMoreProvidersThanMaxCountRegistered_throwException() {
        var lb = new RandomLoadBalancer<String>();
        for (int i = 0; i < lb.getMaxProvidersCount(); i++) {
            lb.registerProvider(new ProviderExample(Integer.toString(i)));
        }

        assertThrows(MaxProvidersCountReachedException.class,
                () -> lb.registerProvider(new ProviderExample(Integer.toString(lb.getMaxProvidersCount())))
        );
    }
}
