package org.grizz.model;

import lombok.Value;

@Value
public class Configuration {
    private int countActivitiesSinceGivenAmountOfMinutes;
    private int scanEntriesSinceGivenAmountOfHours;
}
