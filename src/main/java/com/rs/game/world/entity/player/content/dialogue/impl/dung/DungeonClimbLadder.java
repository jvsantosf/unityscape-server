package com.rs.game.world.entity.player.content.dialogue.impl.dung;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.controller.impl.DungeonController;

public class DungeonClimbLadder extends Dialogue {

	private DungeonController dungeon;

	@Override
	public void start() {
		dungeon = (DungeonController) parameters[0];
		sendOptionsDialogue("Are you sure you wish to proceed and take your party with you?", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1)
			dungeon.voteToMoveOn();
		end();
	}

	@Override
	public void finish() {

	}

}
