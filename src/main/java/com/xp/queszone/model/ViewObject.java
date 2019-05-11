package com.xp.queszone.model;

import java.util.HashMap;
import java.util.Map;

public class ViewObject {

    private Map<String,Object> objects = new HashMap<>();

    public void set(String key, Object object) {
        objects.put(key,object);
    }

    public Object get(String key) {
        return objects.get(key);
    }
}
