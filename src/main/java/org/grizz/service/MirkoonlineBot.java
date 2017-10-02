package org.grizz.service;

import lombok.extern.slf4j.Slf4j;
import org.grizz.config.Configuration;
import org.grizz.model.Statistics;
import org.grizz.model.repo.EntryRepository;
import org.grizz.service.collectors.StatisticsCollector;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.grizwold.microblog.model.Entry;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class MirkoonlineBot {
    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private ResultPoster resultPoster;

    @Autowired
    private List<StatisticsCollector> collectors;

    @Transactional(readOnly = true)
    public Stream<Entry> getEntries(int scanEntriesSince) {
        DateTime now = DateTime.now();
        DateTime since = now.minusHours(scanEntriesSince);

        log.info("Opening mikroblog entries stream since {}", since);
        Stream<Entry> entries = entryRepository.findYoungerThan(since);
        return entries;
    }

    public Statistics collectStatistics(Stream<Entry> entries, Configuration configuration) {
        Statistics statistics = new Statistics();
        entries.forEach(e -> collectors.forEach(collector -> collector.collect(e, configuration)));
        collectors.forEach(collector -> statistics.put(collector.getStats()));
        return statistics;
    }

    public void postResults(Statistics statistics) {
        log.info("Posting new stats");
        resultPoster.post(statistics);
    }
}
