package org.grizz.service.collectors;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Lists;
import lombok.Value;
import org.grizz.config.Configuration;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;
import pl.grizwold.microblog.model.UserGroup;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AdminEntriesCollector implements StatisticsCollector {
    private List<AdminEntry> adminEntries = Lists.newArrayList();
    private Map<String, Object> stats = Maps.newHashMap();

    @Override
    public void collect(Entry entry, Configuration configuration) {
        Optional.of(entry)
                .filter(e -> UserGroup.BLACK.equals(e.getAuthorGroup()))
                .map(e -> new AdminEntry(e.getId(), e.getAuthor()))
                .ifPresent(e -> adminEntries.add(e));
    }

    @Override
    public Map<String, Object> getStats() {
        stats.put("admin_entries", adminEntries);
        return stats;
    }

    @Value
    static class AdminEntry {
        private Long id;
        private String author;
    }
}
