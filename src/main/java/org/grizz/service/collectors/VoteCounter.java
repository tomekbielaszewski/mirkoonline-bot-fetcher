package org.grizz.service.collectors;

import org.grizz.config.Configuration;
import org.grizz.model.Statistics;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class VoteCounter implements StatisticsCollector {
    @Override
    public void collect(List<Entry> entries, Statistics statistics, Configuration configuration) {
        DateTime timeOffset = DateTime.now().minusMinutes(configuration.getCountLastMinutes());

        long voteCount = Stream.of(
                entries.stream().flatMap(e -> e.getVoters().stream()),
                entries.stream().flatMap(e -> e.getComments().stream()).flatMap(c -> c.getVoters().stream())
        )
                .flatMap(Function.identity())
                .filter(v -> v.getDate().isAfter(timeOffset))
                .count();

        statistics.put("vote_count", voteCount);
    }
}
