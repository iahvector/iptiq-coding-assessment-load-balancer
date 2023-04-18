package com.islamhassan.iptiq.loadbalancer;

public interface Provider<T> {
    T get();
    String getId();
}
