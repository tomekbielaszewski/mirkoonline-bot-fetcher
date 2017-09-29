package org.grizz.service.collectors;

import org.grizz.config.Configuration;
import org.grizz.model.Statistics;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;

import java.util.List;
import java.util.function.Function;

@Component
public class CommentCounter implements StatisticsCollector {
    @Override
    public void collect(List<Entry> entries, Statistics statistics, Configuration configuration) {
        DateTime timeOffset = DateTime.now().minusMinutes(configuration.getCountLastMinutes());

        long commentCount = entries.stream()
                .map(e -> e.getComments().stream())
                .flatMap(Function.identity())
                .filter(c -> c.getDateAdded().isAfter(timeOffset))
                .count();

        statistics.put("comment_count", commentCount);
    }
}
