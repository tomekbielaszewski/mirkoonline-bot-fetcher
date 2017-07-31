package org.grizz.service.collectors;

import org.grizz.model.Entry;
import org.grizz.model.Statistics;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActivityCounter implements StatisticsCollector {
    @Override
    public void collect(List<Entry> entries, Statistics statistics) {
        long count = Stream.of(
                entries.stream().map(e -> e.getAuthor()),
                entries.stream().flatMap(e -> e.getVoters().stream()).map(v -> v.getAuthor()),
                entries.stream().flatMap(e -> e.getComments().stream()).map(c -> c.getAuthor()),
                entries.stream().flatMap(e -> e.getComments().stream()).flatMap(c -> c.getVoters().stream()).map(v -> v.getAuthor())
        ).flatMap(Function.identity())
                .collect(Collectors.toSet())
                .stream().count();
        statistics.put("count", count);
    }
}
