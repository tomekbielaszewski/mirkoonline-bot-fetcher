package org.grizz.service.collectors;

import com.google.common.collect.Maps;
import org.grizz.config.Configuration;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;

import java.util.Map;
import java.util.Optional;

@Component
public class EntryCounter implements StatisticsCollector {
    private Map<String, Object> stats = Maps.newHashMap();
    private int entryCount = 0;

    @Override
    public void collect(Entry entry, Configuration configuration) {
        DateTime timeOffset = DateTime.now().minusMinutes(configuration.getCountLastMinutes());

        Optional.of(entry)
                .filter(e -> e.getDateAdded().isAfter(timeOffset))
                .ifPresent(e -> entryCount++);
    }

    @Override
    public Map<String, Object> getStats() {
        stats.put("entry_count", entryCount);
        return stats;
    }
}
