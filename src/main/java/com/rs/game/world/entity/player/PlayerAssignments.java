package com.rs.game.world.entity.player;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

@Data
@NoArgsConstructor
public class PlayerAssignments implements Serializable {

    @Serial
    private static final long serialVersionUID = -3135388675813882470L;

    public Map<String, Object> assignments = Maps.newConcurrentMap();

    public void assign(String key, Object value) {
        assignments.put(key, value);
    }

    public void unassign(String key) {
        assignments.remove(key);
    }

    public Object get(String key) {
        return assignments.get(key);
    }
}
