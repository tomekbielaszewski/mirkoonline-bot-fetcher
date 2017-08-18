package org.grizz.service;

import lombok.extern.slf4j.Slf4j;
import org.grizz.config.Configuration;
import org.grizz.model.Statistics;
import org.grizz.model.repo.EntryRepository;
import org.grizz.service.collectors.StatisticsCollector;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.grizwold.microblog.model.Entry;

import java.util.List;

@Slf4j
@Service
public class MirkoonlineBot {
    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private ResultPoster resultPoster;

    @Autowired
    private List<StatisticsCollector> collectors;

    public List<Entry> getEntries(int scanEntriesSince) {
        DateTime now = DateTime.now();
        DateTime since = now.minusHours(scanEntriesSince);

        log.info("Getting mikroblog entries since {}...", since);
        List<Entry> entries = entryRepository.findByDateAddedGreaterThan(since);

        log.info("Fetched {} entries.", entries.size());
        return entries;
    }

    public Statistics collectStatistics(List<Entry> entries, Configuration configuration) {
        Statistics statistics = new Statistics();
        collectors.forEach(collector -> collector.collect(entries, statistics, configuration));
        return statistics;
    }

    public void postResults(Statistics statistics) {
        log.info("Posting new stats");
        resultPoster.post(statistics);
    }
}
