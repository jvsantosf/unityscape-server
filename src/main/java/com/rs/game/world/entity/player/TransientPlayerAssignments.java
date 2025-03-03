package com.rs.game.world.entity.player;

import com.google.common.collect.Maps;

import java.util.Map;

public class TransientPlayerAssignments {

    public transient Map<String, Object> assignments = Maps.newConcurrentMap();

    public Object assign(String key, Object value) {
        return assignments.put(key, value);
    }

    public void unassign(String key) {
        assignments.remove(key);
    }

    public Object get(String key) {
        return assignments.get(key);
    }

}
