package org.grizz.model;

import java.util.HashMap;
import java.util.Map;

public class Statistics {
    private Map<String, Object> stats = new HashMap<>();

    public void put(Map<String, Object> partialStats) {
        stats.putAll(partialStats);
    }

    public Map<String, Object> getStats() {
        return stats;
    }
}
