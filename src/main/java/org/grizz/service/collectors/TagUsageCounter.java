package org.grizz.service.collectors;

import com.beust.jcommander.internal.Maps;
import org.grizz.config.Configuration;
import org.grizz.service.collectors.helpers.Ranking;
import org.grizz.service.collectors.helpers.SummingRanking;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import pl.grizwold.microblog.model.Entry;

import java.util.Map;
import java.util.Optional;

@Service
public class TagUsageCounter implements StatisticsCollector {
    private Map<String, Object> stats = Maps.newHashMap();
    private Ranking ranking = new SummingRanking();

    @Override
    public void collect(Entry entry, Configuration configuration) {
        DateTime timeOffset = DateTime.now().minusMinutes(configuration.getCountLastMinutes());

        Optional.of(entry)
                .filter(e -> e.getDateAdded().isAfter(timeOffset))
                .ifPresent(e -> e.getTags()
                        .forEach(t -> ranking.add(t.getName())));
        entry.getComments().stream()
                .filter(c -> c.getDateAdded().isAfter(timeOffset))
                .forEach(c -> c.getTags()
                        .forEach(t -> ranking.add(t.getName())));
    }

    @Override
    public Map<String, Object> getStats() {
        stats.put("tag_usage", ranking.asMap());
        return stats;
    }
}
