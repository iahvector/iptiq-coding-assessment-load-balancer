package com.islamhassan.iptiq.loadbalancer.datastores;

public class ProviderMetaData {
    private boolean isEnabled;
    private int consecutiveChecksCount;

    public ProviderMetaData(boolean isEnabled, int consecutiveChecksCount) {
        this.isEnabled = isEnabled;
        this.consecutiveChecksCount = consecutiveChecksCount;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getConsecutiveChecksCount() {
        return consecutiveChecksCount;
    }

    public void setConsecutiveChecksCount(int consecutiveChecksCount) {
        this.consecutiveChecksCount = consecutiveChecksCount;
    }
}
