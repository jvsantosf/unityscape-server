package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.construction.FirePlace;

/**
 * @author Justin
 */


public class Fireplaces extends Dialogue {
	
	WorldObject object;
	int[] boundChuncks;

	public Fireplaces() {
	}

	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		boundChuncks = (int[]) parameters[1];
		stage = 1;
		sendOptionsDialogue("Build What?", "Clay Fireplace", "Stone Fireplace", "Marble Fireplace", "None");
	}

	@Override
	public void run(int interfaceId, int componentId) {
	 if(stage == 1) {
		if(componentId == OPTION_1) {
			FirePlace.CheckClayFirePlace(player, object, boundChuncks);
			player.getInterfaceManager().closeChatBoxInterface();
		} else if(componentId == OPTION_2) {
			FirePlace.CheckStoneFirePlace(player, object, boundChuncks);
			player.getInterfaceManager().closeChatBoxInterface();
		} else if(componentId == OPTION_3) {
			FirePlace.CheckMarbleFirePlace(player, object, boundChuncks);
			player.getInterfaceManager().closeChatBoxInterface();
		}
	 }
		
	}

	@Override
	public void finish() {
		
	}
	
}