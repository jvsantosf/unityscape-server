package com.rs.game.world.entity.player.cutscenes.actions;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.cutscenes.Cutscene;

public class MoveNPCAction extends CutsceneAction {

	private int x, y, plane, movementType;

	public MoveNPCAction(int cachedObjectIndex, int x, int y, boolean run,
			int actionDelay) {
		this(cachedObjectIndex, x, y, -1, run ? Player.RUN_MOVE_TYPE
				: Player.WALK_MOVE_TYPE, actionDelay);
	}

	public MoveNPCAction(int cachedObjectIndex, int x, int y, int plane,
			int movementType, int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.x = x;
		this.y = y;
		this.plane = plane;
		this.movementType = movementType;
	}

	@Override
	public void process(Player player, Object[] cache) {
		NPC npc = (NPC) cache[getCachedObjectIndex()];
		Cutscene scene = (Cutscene) cache[0];
		if (movementType == Player.TELE_MOVE_TYPE) {
			npc.setNextPosition(new Position(scene.getBaseX() + x, scene
					.getBaseY() + y, plane));
			return;
		}
		npc.setRun(movementType == Player.RUN_MOVE_TYPE);
		npc.addWalkSteps(scene.getBaseX() + x, scene.getBaseY() + y);
	}

}
