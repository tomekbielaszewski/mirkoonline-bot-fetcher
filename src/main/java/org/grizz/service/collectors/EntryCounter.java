package org.grizz.service.collectors;

import org.grizz.config.Configuration;
import org.grizz.model.Statistics;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;

import java.util.List;

@Component
public class EntryCounter implements StatisticsCollector {
    @Override
    public void collect(List<Entry> entries, Statistics statistics, Configuration configuration) {
        DateTime timeOffset = DateTime.now().minusMinutes(configuration.getCountLastMinutes());

        long entryCount = entries.stream()
                .filter(e -> e.getDateAdded().isAfter(timeOffset))
                .count();

        statistics.put("entry_count", entryCount);
    }
}
