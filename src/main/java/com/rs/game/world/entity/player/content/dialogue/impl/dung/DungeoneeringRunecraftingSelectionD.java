package com.rs.game.world.entity.player.content.dialogue.impl.dung;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.dungeoneering.skills.runecrafting.DungeoneeringRunecraftingData;

public class DungeoneeringRunecraftingSelectionD extends Dialogue {

	int option;
	@Override
	public void start() {
		option = (int) parameters[0];
		if (option == 0)
			sendOptionsDialogue("Select an Option", "Elemental runes.", "Combat runes.", "Other runes.", "Staves.");
		else if (option == 1)
			sendOptionsDialogue("Select an Option", "Elemental runes.", "Combat runes.", "Other runes.");
		else
			player.getDialogueManager().startDialogue("DungeoneeringRunecraftingD", DungeoneeringRunecraftingData.getSpecificRunes(3));
	}

	@Override
	public void run(int interfaceId, int componentId) {	
		end();
		switch(componentId) {
		case OPTION_1:
			player.getDialogueManager().startDialogue("DungeoneeringRunecraftingD", DungeoneeringRunecraftingData.getSpecificRunes(0));
			break;
		case OPTION_2:
			player.getDialogueManager().startDialogue("DungeoneeringRunecraftingD", DungeoneeringRunecraftingData.getSpecificRunes(1));
			break;
		case OPTION_3:
			player.getDialogueManager().startDialogue("DungeoneeringRunecraftingD", DungeoneeringRunecraftingData.getSpecificRunes(2));
			break;
		default:
			player.getDialogueManager().startDialogue("DungeoneeringRunecraftingD", DungeoneeringRunecraftingData.getSpecificRunes(3));
			break;
		}
	}

	@Override
	public void finish() {
		
	}

}
