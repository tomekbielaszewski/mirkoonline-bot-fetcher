package org.grizz.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.model.UserActivity;
import org.grizz.service.EntryFetcher;
import org.grizz.service.EntryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Grizz on 2015-08-26.
 */
@Slf4j
@Service
public class EntryProviderImpl implements EntryProvider {
    @Autowired
    private EntryFetcher entryFetcher;

    @Override
    public List<UserActivity> getPages(int amountOfPages) {
        List<UserActivity> activities = Lists.newArrayList();

        for (int i = 0; i < amountOfPages; i++) {
            if (i % 10 == 0) {
                log.info("Getting {}th page of mikroblog...", i);
            }
            List<UserActivity> pageEntries = entryFetcher.page(i);
            activities.addAll(pageEntries);
        }

        return activities;
    }
}
