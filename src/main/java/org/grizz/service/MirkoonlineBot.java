package org.grizz.service;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.grizz.model.Statistics;
import org.grizz.model.UserActivity;
import org.grizz.service.collectors.StatisticsCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.grizwold.microblog.model.Entry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MirkoonlineBot {
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    @Value("${mirko.amount.of.pages.to.fetch}")
    private Integer amountOfPages;

    @Autowired
    private EntryProvider entryProvider;

    @Autowired
    private ResultPoster resultPoster;

    @Autowired
    private List<StatisticsCollector> collectors;

    private Statistics statistics;

    public List<Entry> getEntries() {
        statistics = new Statistics();
        log.info("Getting {} pages of mikroblog entries...", amountOfPages);
        List<Entry> entries = entryProvider.getPages(amountOfPages);
        log.info("Downloaded {} of entries.", entries.size());
        return entries;
    }

    public void collectStatistics(List<Entry> entries, long since) {
        Date from = new Date(since - 1);
        log.info("Filtering all activities since {}", formatter.format(from));
        List<Entry> filteredActivities = entries.stream()
                .filter(a -> a.getActivity().after(from))
                .collect(Collectors.toList());
        //todo filter out ALL outdated activities - from bottom to the top. Comment votes, comments, entry votes, entries
        //todo what if outdated entry has votes which fit in time period?
        collectors.forEach(c -> c.collect(filteredActivities, statistics));

    }

    public List<UserActivity> filterDuplicatedUsernames(List<UserActivity> filteredActivities) {
        Set<String> nickDuplicateCheck = Sets.newHashSet();
        log.info("Filtering nickname duplicates");
        List<UserActivity> activitiesWithoutDuplicates = filteredActivities.stream()
                .filter(a -> nickDuplicateCheck.add(a.getNick()))
                .collect(Collectors.toList());
        log.info("{} left after filtering", activitiesWithoutDuplicates.size());
        return activitiesWithoutDuplicates;
    }

    public void postResults(List<UserActivity> activities) {
        log.info("Posting result: {}", activities.size());

        Statistics statistics = new Statistics();
        statistics.put("counter", activities.size());
        resultPoster.post(statistics);
    }
}
