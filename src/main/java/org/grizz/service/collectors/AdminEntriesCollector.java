package org.grizz.service.collectors;

import lombok.Value;
import org.grizz.config.Configuration;
import org.grizz.model.Statistics;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;
import pl.grizwold.microblog.model.UserGroup;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminEntriesCollector implements StatisticsCollector {
    @Override
    public void collect(List<Entry> entries, Statistics statistics, Configuration configuration) {
        List<AdminEntry> adminEntries = entries.stream()
                .filter(e -> UserGroup.BLACK.equals(e.getAuthorGroup()))
                .map(e -> new AdminEntry(e.getId(), e.getAuthor()))
                .collect(Collectors.toList());

        statistics.put("admin_entries", adminEntries);
    }

    @Value
    static class AdminEntry {
        private Long id;
        private String author;
    }
}
