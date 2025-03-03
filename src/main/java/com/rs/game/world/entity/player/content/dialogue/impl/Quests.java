package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.dialogue.impl.CooksAssistant;
import com.rs.game.world.entity.player.content.quests.impl.ImpCatcher;

/**
 * @author Justin
 */


public class Quests extends Dialogue {

	public Quests() {
	}

	@Override
	public void start() {

		stage = 1;
		sendOptionsDialogue(Constants.SERVER_NAME + " Quests", "Dwarf Cannon",
				"Cooks Assistant", "Restless Ghost", "Smoking Kills",
				"More...");
			

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == OPTION_1) { //Dwarf Cannon
				player.getInterfaceManager().sendDwarfCannon();
			} else if (componentId == OPTION_2) {//Cooks Assistant
				if (player.startedCooksAssistant == false) {
					CooksAssistant.handleQuestStartInterface(player);
				}
				if (player.inProgressCooksAssistant == true) {
					CooksAssistant.handleProgressQuestInterface(player);
				}
				if (player.completedCooksAssistantQuest == true) {
					CooksAssistant.handleQuestCompletionTabInterface(player);
				}
			} else if (componentId == OPTION_3) {//Restless Ghost
				if (player.RG <= 1) {
					player.getInterfaceManager().sendRestlessStart(player);
				} else if (player.RG >= 2 && player.RG <= 5) {
					player.getInterfaceManager().handleRestlessProgress(player);
				} else {
					player.getInterfaceManager().handleRestlessCompletedInterface(player);
				}
			} else if (componentId == OPTION_4) {//Smoking Kills
				
				player.getInterfaceManager().sendSmokingKills();
				
				
				/*player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();*/
				player.getControlerManager().removeControlerWithoutCheck();
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue(Constants.SERVER_NAME + " Quests",
						"Lost City", "Vampyre Slayer", "Dragon Slayer", "Ernest the Chicken", "More");
				stage = 2;
			}
		} else if (stage == 2) {
				if (componentId == OPTION_1) { //Lost City
					player.getInterfaceManager().sendLostCity();
				} else if (componentId == OPTION_2) { //Vampyre Slayer
					player.getInterfaceManager().sendVampyreSlayer();
				} else if (componentId == OPTION_3) { //Dragon Slayer
					player.getInterfaceManager().sendDragonSlayer();
				} else if (componentId == OPTION_4) { //Ernest the Chicken
					player.getInterfaceManager().sendErnestChicken();
				} else if (componentId == OPTION_5) {
					sendOptionsDialogue(Constants.SERVER_NAME + " Quests",
							"Imp Catcher",
							"Rune Mysteries",
							"Gertrude's Cat");
					stage = 3;
				}
		} else if (stage == 3) {
			if (componentId == OPTION_1) { //Imp Catcher
				if (player.startedImpCatcher == false) {
					ImpCatcher.handleQuestStartInterface(player);
				}
				if (player.inProgressImpCatcher == true) {
					ImpCatcher.handleProgressQuestInterface(player);
				}
				if (player.completedImpCatcher == true) {
					ImpCatcher.handleQuestCompletionTabInterface(player);
				}
			} else if (componentId == OPTION_2) { //Rune Mysteries
				player.getInterfaceManager().sendRuneMysteries();
			} else if (componentId == OPTION_3) {//Gertrude's Cat 
				player.getInterfaceManager().sendGertrudesCat();
			} else if (componentId == OPTION_4) { 
				
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue(Constants.SERVER_NAME + " Quests",
						"No...");
				stage = 2;
			}
			}
	}
			@Override
			public void finish() {
			}
	

}
