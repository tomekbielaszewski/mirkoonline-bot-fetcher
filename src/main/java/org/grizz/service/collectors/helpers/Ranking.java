package org.grizz.service.collectors.helpers;

import java.util.List;

public interface Ranking {
    void add(Object obj);

    void add(Object obj, int value);

    List<RankedObject> asList();
}
