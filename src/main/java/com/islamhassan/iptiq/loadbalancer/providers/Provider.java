package com.islamhassan.iptiq.loadbalancer.providers;

public interface Provider<T> {
    T get();
    String getId();
    boolean check();
}
