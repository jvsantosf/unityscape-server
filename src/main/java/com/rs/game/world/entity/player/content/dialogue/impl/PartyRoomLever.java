package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.PartyRoom;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class PartyRoomLever extends Dialogue {
	
	@Override
	public void start() {
		sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Balloon Bonanza (1000 coins).", "Nightly Dance (500 coins).", "No action.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(componentId == 2) {
			PartyRoom.purchase(player, true);
		} else if(componentId == 3) {
			PartyRoom.purchase(player, false);
		}
		end();
	}

	@Override
	public void finish() {
		
	}
}
