package org.grizz.service;

import org.grizz.model.UserActivity;

import java.util.List;

/**
 * Created by Grizz on 2015-08-26.
 */
public interface EntryFetcher {
    List<UserActivity> getPage(int i);
}
