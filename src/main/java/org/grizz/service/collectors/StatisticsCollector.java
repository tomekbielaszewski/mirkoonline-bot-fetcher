package org.grizz.service.collectors;

import org.grizz.config.Configuration;
import pl.grizwold.microblog.model.Entry;

import java.util.Map;

public interface StatisticsCollector {
    void collect(Entry entry, Configuration configuration);

    Map<String, Object> getStats();
}
