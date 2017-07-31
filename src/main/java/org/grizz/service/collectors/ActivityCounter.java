package org.grizz.service.collectors;

import org.grizz.model.Statistics;
import pl.grizwold.microblog.model.Entry;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActivityCounter implements StatisticsCollector {
    @Override
    public void collect(List<Entry> entries, Statistics statistics) {
        Stream<String> entriesAuthors = getEntriesAuthors(entries);
        Stream<String> entryVotesAuthors = getEntryVotesAuthors(entries);
        Stream<String> entryCommentsAuthors = getEntryCommentsAuthors(entries);
        Stream<String> entryCommentVotesAuthors = getEntryCommentVotesAuthors(entries);

        long count = (long) Stream.of(
                entriesAuthors,
                entryVotesAuthors,
                entryCommentsAuthors,
                entryCommentVotesAuthors)
                .collect(Collectors.toSet())
                .size();

        statistics.put("count", count);
    }

    private Stream<String> getEntryCommentVotesAuthors(List<Entry> entries) {
        return entries.stream()
                .flatMap(e -> e
                        .getComments().stream()
                        .flatMap(c -> c
                                .getVoters().stream()
                                .map(v -> v.getAuthor())));
    }

    private Stream<String> getEntryCommentsAuthors(List<Entry> entries) {
        return entries.stream()
                .flatMap(e -> e
                        .getComments().stream()
                        .map(c -> c.getAuthor()));
    }

    private Stream<String> getEntryVotesAuthors(List<Entry> entries) {
        return entries.stream()
                .flatMap(e -> e
                        .getVoters().stream()
                        .map(v -> v.getAuthor()));
    }

    private Stream<String> getEntriesAuthors(List<Entry> entries) {
        return entries.stream()
                .map(e -> e.getAuthor());
    }
}
