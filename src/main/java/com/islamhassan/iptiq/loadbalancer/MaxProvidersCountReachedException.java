package com.islamhassan.iptiq.loadbalancer;

public class MaxProvidersCountReachedException extends RuntimeException {
    public MaxProvidersCountReachedException() {
    }

    public MaxProvidersCountReachedException(String message) {
        super(message);
    }
}
