package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

public class KaramjaSpiritsBar extends Dialogue {
	
	private int npcId = 568;

	@Override
	public void start() {
		sendEntityDialogue(
				SEND_2_TEXT_CHAT,
				new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Welcome to the Karamja Spirits Bar, Where the Ape Bite Liqueur was created!"},
				IS_NPC, npcId, 9827);
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue("Karamja Spirits Bar",
					"I'd like to try your signature drink",
					"Could I have a regular beer?",
					"Do you get much business over here?",
					"Good bye");
			stage = 1;
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				sendNPCDialogue(
						npcId,
						9827,
						"Sure thing! One Ape Bite Liqueur coming up!");
				stage = 2;
			} else if (componentId == OPTION_2) {
				sendNPCDialogue(
						npcId,
						9827,
						"I advise you to try our special island rum instead, on the house!");
				stage = 3;
			} else if (componentId == OPTION_3) {
				sendNPCDialogue(
						npcId,
						9827,
						"Tourists come in here all the time, usually at night, to enjoy our their vacations!");
				stage = -1;
			} else if (componentId == OPTION_4) {
				
				end();
			}
		} else if (stage == 2) {
			end();
			player.animate(new Animation(1327));
			player.applyHit(new Hit(player, 290, HitLook.REGULAR_DAMAGE));
			player.setNextForceTalk(new ForceTalk("OOh!!"));
			player.getPackets().sendGameMessage("The Bartender signs your BarCrawl Card.");
			player.KaramjaSpiritsBar = 1;
		} else if (stage == 3) {
			sendPlayerDialogue(9827, "Thanks!");
			player.getInventory().addItem(431, 1);
			stage = -1;
		}
		 
	}

	@Override
	public void finish() {

	}
}