package com.islamhassan.iptiq.loadbalancer;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomLoadBalancerTest {
    @Test
    public void whenGetIsCalled_returnResultFromARandomProvider() {
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
    public void whenGetIsCalled_useEnabledProvidersOnly() {
        var enabledId = "1";
        var disabledId = "2";
        var enabled = new ProviderExample(enabledId);
        var disabled = new ProviderExample(disabledId);
        var lb = new RandomLoadBalancer<String>();
        lb.registerProvider(enabled);
        lb.registerProvider(disabled);
        lb.disableProvider(disabledId);

        assertFalse(lb.isProviderEnabled(disabledId));
        assertEquals(1, lb.getEnabledProvidersCount());

        for (int i = 0; i < 10; i++) {
            var res = lb.get();
            assertEquals(enabledId, res);
        }
    }
}
