package org.grizz.service.collectors;

import com.google.common.collect.Maps;
import org.grizz.config.Configuration;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;

import java.util.Map;

@Component
public class CommentCounter implements StatisticsCollector {
    private Map<String, Object> stats = Maps.newHashMap();
    private int commentCount = 0;

    @Override
    public void collect(Entry entry, Configuration configuration) {
        DateTime timeOffset = DateTime.now().minusMinutes(configuration.getCountLastMinutes());

        commentCount += entry.getComments().stream()
                .filter(c -> c.getDateAdded().isAfter(timeOffset))
                .count();
    }

    @Override
    public Map<String, Object> getStats() {
        stats.put("comment_count", commentCount);
        return stats;
    }
}
