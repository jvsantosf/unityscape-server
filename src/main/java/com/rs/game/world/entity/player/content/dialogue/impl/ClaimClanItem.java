package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class ClaimClanItem extends Dialogue {

    @Override
    public void start() {
	int npcId = (Integer) this.parameters[0];
	int itemId = (Integer) this.parameters[1];
	if(player.getClanManager() == null) {
	    sendNPCDialogue(npcId, 9827, "Talk to me once you have a clan.");
	    return;
	}
	sendNPCDialogue(npcId, 9827, "Enjoy your "+ItemDefinitions.getItemDefinitions(itemId).getName().toLowerCase()+"!");
	player.getInventory().addItem(new Item(itemId));
    }

    @Override
    public void run(int interfaceId, int componentId) {
	end();
    }

    @Override
    public void finish() {

    }
}
