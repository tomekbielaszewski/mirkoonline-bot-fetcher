package org.grizz.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.grizz.model.UserActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class EntryProvider {
    @Autowired
    private EntryFetcher entryFetcher;

    public List<UserActivity> getPages(int amountOfPages) {
        List<UserActivity> activities = IntStream.range(0, amountOfPages)
                .parallel()
                .mapToObj(page -> entryFetcher.getPage(page))
                .flatMap(activitiesPage -> activitiesPage.stream())
                .collect(Collectors.toList());

        return activities;
    }
}
