package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;

@SuppressWarnings("serial")
public final class SandCrab extends NPC {
		
	public SandCrab(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setForceMultiArea(true);
	}
	
	@Override
	public void processNPC() {
		checkPlayersInArea();
		if (getCombat().getTarget() == null) {
			if (getSpawnPosition() == getPosition()) {
				transformIntoNPC(25936);	
			} else if (getId() == 25935 && (getSpawnPosition() != getPosition())) {
				forceWalkRespawnTile();
			}
		}
		super.processNPC();
	}

	@Override
	public void finish() {
		transformIntoNPC(25936);
		super.finish();
	}
	
	/**
	 * Checks for players in the area of the rock crab.
	 * @return
	 */
	public void checkPlayersInArea() {
		for (Player player : World.getPlayers()) {
			if (!player.withinDistance(this, 2))
				continue;
			transformIntoNPC(25935);
			setTarget(player);
		}
	}
	
}