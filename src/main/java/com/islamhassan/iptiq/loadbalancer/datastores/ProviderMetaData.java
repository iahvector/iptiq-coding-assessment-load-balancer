package com.islamhassan.iptiq.loadbalancer.datastores;

public class ProviderMetaData {
    private boolean isEnabled;

    public ProviderMetaData(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
