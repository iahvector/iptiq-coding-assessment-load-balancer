package com.islamhassan.iptiq.loadbalancer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

        var lb = new RoundRobinLoadBalancer<String>();
        for (int s = 0; s < size; s++) {
            lb.registerProvider(new ProviderExample(Integer.toString(s)));
        }

        for (int i = 0; i < iterations * size; i++) {
            actual.add(lb.get());
        }

        assertEquals(expected, actual);
    }
}