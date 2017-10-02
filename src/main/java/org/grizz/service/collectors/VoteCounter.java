package org.grizz.service.collectors;

import com.beust.jcommander.internal.Maps;
import org.grizz.config.Configuration;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class VoteCounter implements StatisticsCollector {
    private Map<String, Object> stats = Maps.newHashMap();
    private int voteCount;

    @Override
    public void collect(Entry entry, Configuration configuration) {
        DateTime timeOffset = DateTime.now().minusMinutes(configuration.getCountLastMinutes());

        voteCount += Stream.of(
                entry.getVoters().stream(),
                entry.getComments().stream().flatMap(c -> c.getVoters().stream())
        )
                .flatMap(Function.identity())
                .filter(v -> v.getDate().isAfter(timeOffset))
                .count();
    }

    @Override
    public Map<String, Object> getStats() {
        stats.put("vote_count", voteCount);
        return stats;
    }
}
