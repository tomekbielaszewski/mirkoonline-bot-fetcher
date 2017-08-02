package org.grizz.service.collectors;

import org.grizz.model.Statistics;
import org.joda.time.DateTime;
import pl.grizwold.microblog.model.Entry;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActivityCounter implements StatisticsCollector {

    @Override
    public void collect(List<Entry> entries, Statistics statistics) {
        Stream<DatedAuthor> entriesAuthors = getEntriesAuthors(entries);
        Stream<DatedAuthor> entryVotesAuthors = getEntryVotesAuthors(entries);
        Stream<DatedAuthor> entryCommentsAuthors = getEntryCommentsAuthors(entries);
        Stream<DatedAuthor> entryCommentVotesAuthors = getEntryCommentVotesAuthors(entries);

        DateTime time15MinutesAgo = DateTime.now().minusMinutes(15);

        Set<String> activeUsers = Stream.of(
                entriesAuthors,
                entryVotesAuthors,
                entryCommentsAuthors,
                entryCommentVotesAuthors)
                .flatMap(Function.identity())
                .filter(datedAuthor -> datedAuthor.date.isAfter(time15MinutesAgo))
                .map(datedAuthor -> datedAuthor.author)
                .collect(Collectors.toSet());

        statistics.put("count", activeUsers.size());
        statistics.put("active", activeUsers);
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
