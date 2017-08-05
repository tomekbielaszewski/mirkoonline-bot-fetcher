package org.grizz.service.collectors;

import org.grizz.model.Configuration;
import org.grizz.model.Statistics;
import org.grizz.service.collectors.helpers.Ranking;
import org.grizz.service.collectors.helpers.SummingRanking;
import pl.grizwold.microblog.model.Entry;

import java.util.List;

public class TagUsageCounter implements StatisticsCollector {
    private Ranking ranking = new SummingRanking();

    @Override
    public void collect(List<Entry> entries, Statistics statistics, Configuration configuration) {
        entries.forEach(e -> {
            e.getTags().forEach(t -> ranking.add(t.getName()));
            e.getComments().forEach(c -> c.getTags().forEach(t -> ranking.add(t.getName())));
        });

        statistics.put("tag_usage", ranking.asList());
    }
}
