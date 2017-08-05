package org.grizz.service.collectors;

import lombok.ToString;
import org.grizz.model.Configuration;
import org.grizz.model.Statistics;
import org.grizz.service.collectors.helpers.Ranking;
import org.grizz.service.collectors.helpers.SummingRanking;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class ActivityCounter implements StatisticsCollector {
    private Ranking ranking = new SummingRanking();

    @Override
    public void collect(List<Entry> entries, Statistics statistics, Configuration configuration) {
        Stream<DatedAuthor> entriesAuthors = getEntriesAuthors(entries);
        Stream<DatedAuthor> entryVotesAuthors = getEntryVotesAuthors(entries);
        Stream<DatedAuthor> entryCommentsAuthors = getEntryCommentsAuthors(entries);
        Stream<DatedAuthor> entryCommentVotesAuthors = getEntryCommentVotesAuthors(entries);

        DateTime timeOffset = DateTime.now().minusMinutes(configuration.getCountActivitiesSinceGivenAmountOfMinutes());

        Stream.of(
                entriesAuthors,
                entryVotesAuthors,
                entryCommentsAuthors,
                entryCommentVotesAuthors)
                .flatMap(Function.identity())
                .filter(datedAuthor -> isAfter(timeOffset, datedAuthor))
                .map(datedAuthor -> datedAuthor.author)
                .forEach(ranking::add);

        Map<Object, Integer> activeUsers = ranking.asMap();

        statistics.put("active_users", activeUsers);
        statistics.put("mirkoonline", activeUsers.size());
    }

    private boolean isAfter(DateTime timeOffset, DatedAuthor datedAuthor) {
        return datedAuthor.date.isAfter(timeOffset);
    }

    private Stream<DatedAuthor> getEntryCommentVotesAuthors(List<Entry> entries) {
        return entries.stream()
                .flatMap(e -> e
                        .getComments().stream()
                        .flatMap(c -> c
                                .getVoters().stream()
                                .map(v -> new DatedAuthor(v.getAuthor(), v.getDate()))));
    }

    private Stream<DatedAuthor> getEntryCommentsAuthors(List<Entry> entries) {
        return entries.stream()
                .flatMap(e -> e
                        .getComments().stream()
                        .map(c -> new DatedAuthor(c.getAuthor(), c.getDateAdded())));
    }

    private Stream<DatedAuthor> getEntryVotesAuthors(List<Entry> entries) {
        return entries.stream()
                .flatMap(e -> e
                        .getVoters().stream()
                        .map(v -> new DatedAuthor(v.getAuthor(), v.getDate())));
    }

    private Stream<DatedAuthor> getEntriesAuthors(List<Entry> entries) {
        return entries.stream()
                .map(e -> new DatedAuthor(e.getAuthor(), e.getDateAdded()));
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
