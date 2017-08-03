package org.grizz.service.collectors;

import org.grizz.model.Configuration;
import org.grizz.model.Statistics;
import pl.grizwold.microblog.model.Entry;

import java.util.List;

public interface StatisticsCollector {
    void collect(List<Entry> entries, Statistics statistics, Configuration configuration);
}
