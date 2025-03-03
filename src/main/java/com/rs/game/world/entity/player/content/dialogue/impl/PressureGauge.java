package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.updating.impl.Animation;

public class PressureGauge extends Dialogue {


	@Override
	public void start() {
		sendPlayerDialogue(9827, "There seems to be a pressure gauge in here...");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch(stage) {
		case -1:
			sendPlayerDialogue(9827, "...and a lot of dead fish.");
			stage = 1;
			break;
		case 1:
			end();
			player.getInventory().deleteItem(274, 1);
			player.animate(new Animation(833));
			player.getInventory().addItem(271, 1);
			player.getPackets().sendGameMessage("You take the Pressure Gauge.");
		}
	}

	@Override
	public void finish() {
	}

}
