package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class JackSeagull extends Dialogue {
	
	public int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Greetings!*hic*" );
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch(stage) {
		case -1:
			stage = 0;
			sendPlayerDialogue(9827, "What is this place?" );
		break;
		case 0:
			stage = -2;
			sendNPCDialogue(npcId, 9827, "This is the *hic* Pirate Bar. Only adventurers who have *hic* ",
					"braved the seas of " + Constants.SERVER_NAME,
					"may dine here! *hic*" );
		break;
		default:
			end();
		break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
