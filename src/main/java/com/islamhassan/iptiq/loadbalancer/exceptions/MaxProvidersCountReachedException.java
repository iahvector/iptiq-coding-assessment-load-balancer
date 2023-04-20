package com.islamhassan.iptiq.loadbalancer.exceptions;

public class MaxProvidersCountReachedException extends RuntimeException {
    public MaxProvidersCountReachedException() {
    }

    public MaxProvidersCountReachedException(String message) {
        super(message);
    }
}
