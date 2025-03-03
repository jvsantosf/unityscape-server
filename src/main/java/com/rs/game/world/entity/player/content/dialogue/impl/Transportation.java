package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.magic.Magic;

public class Transportation extends Dialogue {

	// Ring of duelling
	// Combat bracelet

	public static int EMOTE = 9603, GFX = 1684;

	@Override
	public void start() {
		sendOptionsDialogue("Where would you like to teleport to",
				(String) parameters[0], (String) parameters[2],
				(String) parameters[4], (String) parameters[6], "Nowhere");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		boolean teleported = false;
		if (componentId == OPTION_1)
			teleported = Magic.sendItemTeleportSpell(player, true, EMOTE, GFX,
					4, (Position) parameters[1], new Item( (int) parameters[8]));
		else if (componentId == OPTION_2)
			teleported = Magic.sendItemTeleportSpell(player, true, EMOTE, GFX,
					4, (Position) parameters[3], new Item( (int) parameters[8]));
		else if (componentId == OPTION_3)
			teleported = Magic.sendItemTeleportSpell(player, true, EMOTE, GFX,
					4, (Position) parameters[5], new Item( (int) parameters[8]));
		else if (componentId == OPTION_4)
			teleported = Magic.sendItemTeleportSpell(player, true, EMOTE, GFX,
					4, (Position) parameters[7], new Item( (int) parameters[8]));
		if (!teleported) {
			end();
			return;
		}
		Item item = player.getInventory().getItems()
				.lookup((Integer) parameters[8]);
		if ((item.getId() >= 3853 && item.getId() <= 3865)
				|| (item.getId() >= 10354 && item.getId() <= 10361)
				|| (item.getId() >= 11105 && item.getId() <= 11125)
				|| (item.getId() >= 2552 && item.getId() <= 2567)) {
			item.setId(item.getId() + 2);
		} else if (item.getId() == 3867 || item.getId() == 10362) {
			item.setId(-1);
		} else if (item.getId() >= 13281 && item.getId() <= 13288) {
			item.setId(item.getId() + 1);
		} else {
			item.setId(item.getId() - 2);
		}
		player.getInventory().refresh(
				player.getInventory().getItems().getThisItemSlot(item));
		end();
	}

	@Override
	public void finish() {
	}

}
