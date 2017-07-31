package org.grizz.service.collectors;

import org.grizz.model.Entry;
import org.grizz.model.Statistics;

import java.util.List;

public interface StatisticsCollector {
    void collect(List<Entry> entries, Statistics statistics);
}
