package com.islamhassan.iptiq.loadbalancer;

import com.islamhassan.iptiq.loadbalancer.datastores.InMemoryProviderMetaDataStore;
import com.islamhassan.iptiq.loadbalancer.providers.ProviderExample;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class RoundRobinLoadBalancerTest {
    @Test
    public void whenGetIsCalled_returnResultsInRoundRobinOrder() {
        var size = 3;
        var iterations = 3;
        var expected = new ArrayList<String>();
        var actual = new ArrayList<String>();

        for (int i = 0; i < iterations; i++) {
            for (int s = 0; s < size; s++) {
                expected.add(Integer.toString(s));
            }
        }

        var lb = new RoundRobinLoadBalancer<String>(new InMemoryProviderMetaDataStore());
        for (int s = 0; s < size; s++) {
            lb.registerProvider(new ProviderExample(Integer.toString(s)));
        }

        for (int i = 0; i < iterations * size; i++) {
            actual.add(lb.get());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void whenGetIsCalled_useEnabledProvidersOnly() {
        var enabledId = "1";
        var disabledId = "2";
        var enabled = new ProviderExample(enabledId);
        var disabled = new ProviderExample(disabledId);
        var lb = new RoundRobinLoadBalancer<String>(new InMemoryProviderMetaDataStore());
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