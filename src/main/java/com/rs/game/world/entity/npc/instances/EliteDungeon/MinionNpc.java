package com.rs.game.world.entity.npc.instances.EliteDungeon;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Animation;

/**
 * @author ReverendDread
 * Created 2/18/2021 at 3:38 PM
 * @project 718---Server
 */
public class MinionNpc extends NPC {

    private static final long serialVersionUID = -3887004698386195825L;

    public MinionNpc(int id, Position tile) {
        super(id, tile, -1, 0, true, true);
        setForceMultiArea(true);
        setAtMultiArea(true);

    }

}

