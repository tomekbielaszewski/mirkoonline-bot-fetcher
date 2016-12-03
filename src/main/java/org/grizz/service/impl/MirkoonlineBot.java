package org.grizz.service.impl;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.grizz.model.UserActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<UserActivity> getActivities() {
        log.info("Getting {} pages of mikroblog entries...", amountOfPages);
        List<UserActivity> rawActivities = entryProvider.getPages(amountOfPages);
        log.info("Downloaded {} of entries. Now flattening activites...", rawActivities.size());
        List<UserActivity> activities = rawActivities.stream()
                .flatMap(a ->
                        Stream.concat(a.getVoters().stream(),
                                a.getComments().stream()
                                        .flatMap(comment -> comment.getVoters().stream())))
                .collect(Collectors.toList());
        log.info("Activities flattened. Amount {}", activities.size());

        return activities;
    }

    public List<UserActivity> getFilteredActivities(List<UserActivity> activities, long since) {
        Date from = new Date(since - 1);
        log.info("Filtering all activities since {}", formatter.format(from));
        List<UserActivity> filteredActivities = activities.stream()
                .filter(a -> a.getActivity().after(from))
                .collect(Collectors.toList());
        log.info("{} left after filtering", filteredActivities.size());
        return filteredActivities;
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
        resultPoster.post(activities.size());
    }
}
