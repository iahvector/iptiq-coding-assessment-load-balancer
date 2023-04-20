package com.islamhassan.iptiq.loadbalancer.exceptions;

public class NoProvidersAvailableException extends RuntimeException{
    public NoProvidersAvailableException(String message) {
        super(message);
    }

    public NoProvidersAvailableException() {
    }
}
