package org.grizz.springconfig;

import org.grizz.model.UserActivity;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Created by Grizz on 2014-07-23.
 */
@Configuration
@EnableScheduling
public class MainConfig {

    @Scheduled(fixedDelay = 8 * 60 * 1000)
    public void run() {
        List<UserActivity> activities = getActivities();
        List<UserActivity> filteredActivities = getFilterActivities(activities, 0l); //get activities from last x minutes

    }

    private List<UserActivity> getFilterActivities(List<UserActivity> activities, long l) {
        return null;
    }

    private List<UserActivity> getActivities() {
        return null;
    }
}
