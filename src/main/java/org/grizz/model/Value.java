package org.grizz.model;

import java.util.HashMap;
import java.util.Map;

public class Value {
    public static Map<String, Object> of(String key, Object value) {
        HashMap wrapped = new HashMap();
        wrapped.put(key, value);
        return wrapped;
    }
}
