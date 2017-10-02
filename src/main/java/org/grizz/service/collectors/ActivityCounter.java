package org.grizz.service.collectors;

import com.google.common.collect.Maps;
import lombok.ToString;
import org.grizz.config.Configuration;
import org.grizz.service.collectors.helpers.Ranking;
import org.grizz.service.collectors.helpers.SummingRanking;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class ActivityCounter implements StatisticsCollector {
    private Ranking ranking = new SummingRanking();
    private Map<String, Object> stats = Maps.newHashMap();

    @Override
    public void collect(Entry entry, Configuration configuration) {
        DateTime timeOffset = DateTime.now().minusMinutes(configuration.getCountLastMinutes());

        Stream<DatedAuthor> entriesAuthors = getEntryAuthor(entry);
        Stream<DatedAuthor> entryVotesAuthors = getEntryVotesAuthors(entry);
        Stream<DatedAuthor> entryCommentsAuthors = getEntryCommentsAuthors(entry);
        Stream<DatedAuthor> entryCommentVotesAuthors = getEntryCommentVotesAuthors(entry);

        Stream.of(
                entriesAuthors,
                entryVotesAuthors,
                entryCommentsAuthors,
                entryCommentVotesAuthors)
                .flatMap(Function.identity())
                .filter(datedAuthor -> isAfter(timeOffset, datedAuthor))
                .map(datedAuthor -> datedAuthor.author)
                .forEach(ranking::add);
    }

    @Override
    public Map<String, Object> getStats() {
        Map<Object, Integer> activeUsers = ranking.asMap();
        stats.put("active_users", activeUsers);
        stats.put("mirkoonline", activeUsers.size());
        return stats;
    }

    private boolean isAfter(DateTime timeOffset, DatedAuthor datedAuthor) {
        return datedAuthor.date.isAfter(timeOffset);
    }

    private Stream<DatedAuthor> getEntryCommentVotesAuthors(Entry entry) {
        return entry.getComments().stream()
                .flatMap(c -> c
                        .getVoters().stream()
                        .map(v -> new DatedAuthor(v.getAuthor(), v.getDate())));
    }

    private Stream<DatedAuthor> getEntryCommentsAuthors(Entry entry) {
        return entry.getComments().stream()
                .map(c -> new DatedAuthor(c.getAuthor(), c.getDateAdded()));
    }

    private Stream<DatedAuthor> getEntryVotesAuthors(Entry entry) {
        return entry.getVoters().stream()
                .map(v -> new DatedAuthor(v.getAuthor(), v.getDate()));
    }

    private Stream<DatedAuthor> getEntryAuthor(Entry entry) {
        return Stream.of(new DatedAuthor(entry.getAuthor(), entry.getDateAdded()));
    }

    @ToString
    private class DatedAuthor {
        String author;
        DateTime date;

        DatedAuthor(String author, DateTime date) {
            this.author = author;
            this.date = date;
        }
    }
}
