package org.grizz.service;

import org.grizz.model.UserActivity;

import java.util.List;

/**
 * Created by Grizz on 2015-08-26.
 */
public interface MirkoonlineBot {
    List<UserActivity> getActivities();

    List<UserActivity> getFilteredActivities(List<UserActivity> activities, long since);

    List<UserActivity> filterDuplicatedUsernames(List<UserActivity> filteredActivities);

    void postResults(List<UserActivity> activities);
}
