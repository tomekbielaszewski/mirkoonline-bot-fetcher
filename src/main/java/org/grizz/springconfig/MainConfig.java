package org.grizz.springconfig;

import org.grizz.model.UserActivity;
import org.grizz.service.MirkoonlineBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

/**
 * Created by Grizz on 2014-07-23.
 */
@Configuration
@EnableScheduling
@ComponentScan("org.grizz")
public class MainConfig {
    private static final long MINUTE = 60 * 1000;
    private static final long TIME_OFFSET = 15 * MINUTE;

    @Autowired
    private MirkoonlineBot mirkoonlineBot;

    @Scheduled(fixedDelay = 8 * MINUTE)
    public void periodicallyRun() {
        long since = new Date().getTime() - TIME_OFFSET;
        List<UserActivity> activities = mirkoonlineBot.getActivities();
        List<UserActivity> filteredActivities = mirkoonlineBot.getFilteredActivities(activities, since); //get activities from last x minutes
        List<UserActivity> uniqueActivities = mirkoonlineBot.filterDuplicatedUsernames(filteredActivities);
        mirkoonlineBot.postResults(uniqueActivities);
    }
}
