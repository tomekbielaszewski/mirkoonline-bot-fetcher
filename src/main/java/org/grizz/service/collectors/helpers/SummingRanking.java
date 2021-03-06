package org.grizz.service.collectors.helpers;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SummingRanking implements Ranking {
    private Map<Object, Integer> ranking = Maps.newHashMap();

    @Override
    public void add(Object obj) {
        add(obj, 1);
    }

    @Override
    public void add(Object obj, int value) {
        if (ranking.containsKey(obj)) {
            Integer oldValue = ranking.get(obj);
            value += oldValue;
        }
        ranking.put(obj, value);
    }

    @Override
    public List<RankedObject> asList() {
        return ranking.entrySet().stream()
                .map(e -> new RankedObject(e.getKey(), e.getValue()))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Map<Object, Integer> asMap() {
        return ranking.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum,
                        LinkedHashMap::new
                ));
    }
}
