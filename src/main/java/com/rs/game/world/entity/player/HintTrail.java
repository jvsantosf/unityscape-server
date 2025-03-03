package com.rs.game.world.entity.player;

import com.rs.game.map.Position;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HintTrail {

    private Position start;
    private int index = 0;
    private int modelId;
    private int[] stepsX, stepsY;
    private int size;
}
