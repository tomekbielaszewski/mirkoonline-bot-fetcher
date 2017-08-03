package org.grizz.service.collectors;

import org.grizz.model.Configuration;
import org.grizz.model.Counter;
import org.grizz.model.Statistics;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ActivityCounter implements StatisticsCollector {

    @Override
    public void collect(List<Entry> entries, Statistics statistics, Configuration configuration) {
        Stream<DatedAuthor> entriesAuthors = getEntriesAuthors(entries);
        Stream<DatedAuthor> entryVotesAuthors = getEntryVotesAuthors(entries);
        Stream<DatedAuthor> entryCommentsAuthors = getEntryCommentsAuthors(entries);
        Stream<DatedAuthor> entryCommentVotesAuthors = getEntryCommentVotesAuthors(entries);

        DateTime timeOffset = DateTime.now().minusMinutes(configuration.getCountActivitiesSinceGivenAmountOfMinutes());

        Set<String> activeUsers = Stream.of(
                entriesAuthors,
                entryVotesAuthors,
                entryCommentsAuthors,
                entryCommentVotesAuthors)
                .flatMap(Function.identity())
                .filter(datedAuthor -> datedAuthor.date.isAfter(timeOffset))
                .map(datedAuthor -> datedAuthor.author)
                .collect(Collectors.toSet());

        if (!activeUsers.isEmpty()) {
            statistics.put("active_users", activeUsers);
        }
        statistics.put("mirkoonline", Counter.of(activeUsers.size()));
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

    private class DatedAuthor {
        String author;
        DateTime date;

        DatedAuthor(String author, DateTime date) {
            this.author = author;
            this.date = date;
        }
    }
}
