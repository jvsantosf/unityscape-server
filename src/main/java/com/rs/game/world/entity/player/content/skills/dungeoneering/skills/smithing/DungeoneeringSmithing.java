package com.rs.game.world.entity.player.content.skills.dungeoneering.skills.smithing;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;


public class DungeoneeringSmithing extends Action {

	public static final int DUNGEONEERING_HAMMER = 17883;
	private static final int[] DUNGEONEERING_NOVITE = { 17885, 16757, 16339, 16273, 16361, 16295, 17019, 16935, 16383, 16691, 15753, 17341, 16713, 16669, 16647, 17063, 16405, 16889, 17239 };
	private static final int[] COMPONENTS = new int[] { 22, 23, 24, 29, 34, 39, 44, 49, 54, 59, 64, 69, 74, 79, 84, 89, 94, 99, 104 };
	private static final int[] ALL_COMPONENTS = new int[] { 116, 112, 28, 33, 38, 43, 48, 53, 58, 63, 68, 73, 78, 83, 88, 93, 98, 103, 108 };
	private static final int[] BARS = new int[] { 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5 };
	private static final int[] LEVELS = new int[] { 0, 0, 1, 1, 2, 2, 3, 3, 4, 5, 5, 6, 6, 7, 7, 7, 8, 8, 9 };
	private static final String[] TYPES = new String[] { "Arrowheads", "Dagger", "Boots", "Gauntlets", "Hatchet", "Pickaxe", "Warhammer", "Rapier", "Longsword", "Full helm", "Battleaxe", "Kiteshield", "Chainbody", "Platelegs", "Plateskirt", "Spear", "Maul", "2H Sword", "Platebody" };
	private static final double[] EXPERIENCE = new double[] { 10, 10, 10.5, 10.5, 11, 11, 23, 23, 24, 25, 25, 39, 39, 40.5, 40.5, 54, 56, 56, 72.5 };
	
	public static void sendInterface(Player player, int bar) {
		if (bar < 17650 || bar > 17668) 
			return;
		int type = (bar - 17650) / 2;
		player.getTemporaryAttributtes().put("dgmetal", type);
		sendItems(player, type);
		player.getInterfaceManager().sendInterface(934);
		player.getPackets().sendIComponentText(934, 20, ItemDefinitions.getItemDefinitions(bar).getName().replace(" bar", " Smithing"));
		int count = 0;
		for (int c : ALL_COMPONENTS) {
			String bars = (player.getInventory().getAmountOf(bar) >= BARS[count] ? "<col=00ff00>" : "") + (player.getSkills().getLevel(Skills.SMITHING) < ((10 * (bar - 17650) / 2) + LEVELS[count]) ? "<col=ff0000>" : "") + BARS[count] + " Bar" + (BARS[count] > 1 ? "s" : "");
			player.getPackets().sendIComponentText(934, c - 1, bars);
			player.getPackets().sendIComponentText(934, c - 2, (player.getInventory().getAmountOf(bar) >= BARS[count] && player.getSkills().getLevel(Skills.SMITHING) >= ((10 * (bar - 17650) / 2) + LEVELS[count]) ? "<col=ffffff>" : "") + TYPES[count]);
			player.getPackets().sendHideIComponent(934, c, true);
			count++;
		}
	}
	
	public static int getBestBar(Player player) {
		int barId = 0;
		for (int i = 17650; i < 17670; i += 2) {
			if (player.getInventory().containsItem(i, 1) && player.getSkills().getLevel(Skills.SMITHING) >= ((i - 17650) / 2) * 10)
				barId = i;
		}
		return barId;
	}
		
	private static void sendItems(Player player, int tier) {
		int count = 0;
		for (int smithables : DUNGEONEERING_NOVITE) {
			Item[] items = new Item[] { count == 0 ? new Item(smithables + (tier * 5), 20) : new Item(smithables + ((count == 1 || count == 15) ? (8 * tier) : (2 * tier))) };
			if (count < 5) {
				player.getPackets().sendItems(164 + count, items);
				player.getPackets().sendInterSetItemsOptionsScript(934, COMPONENTS[count], 164 + count, 1, 0, "Make-1", "Make-5", "Make-10", "Make-All", "Make-X");
				player.getPackets().sendUnlockIComponentOptionSlots(934, COMPONENTS[count], 0, 500, 0, 1, 2, 3, 4);
			} else {
				player.getPackets().sendItems(167 + ((count - 4) * 5), items);
				player.getPackets().sendInterSetItemsOptionsScript(934, COMPONENTS[count], 167 + ((count - 4) * 5), 1, 0, "Make-1", "Make-5", "Make-10", "Make-All", "Make-X");
				player.getPackets().sendUnlockIComponentOptionSlots(934, COMPONENTS[count], 0, 500, 0, 1, 2, 3, 4);
			}
			count++;
		}
	}
	
	private int option, level, amount, itemId, barId;
	private Position object;
	
	public DungeoneeringSmithing(int metalType, int componentId, int amount, int itemId) {
		this.amount = amount;
		this.itemId = itemId;
		this.barId = (metalType * 2) + 17650;
		option = componentId < 25 ? componentId - 22 : ((componentId - 24) / 5) + 2;
		level = ((10 * metalType) + LEVELS[option]);
	}

	@Override
	public boolean process(Player player) {
		if (amount <= 0)
			return false;
		if (!player.getInventory().containsItem(barId, BARS[option])) {
			player.sendMessage("You've ran out of " + ItemDefinitions.getItemDefinitions(barId).getName() + "s.");
			return false;
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < level) {
			player.sendMessage("You need at least level " + level + " Smithing to work with a bar of " + ItemDefinitions.getItemDefinitions(barId).getName().replace(" bar", "."));
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		amount--;
		
			player.animate(new Animation(898));
		if (object != null)
			World.sendGraphics(player, new Graphics(2123), object);
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(barId);
		
			player.getInventory().deleteItem(barId, BARS[option]);
		if (player.getInventory().hasFreeSlots())
			player.getInventory().addItem(itemId, option == 0 ? 20 : 1);
		else
			World.addGroundItem(new Item(itemId, option == 0 ? 20 : 1), new Position(player), player, true, 180);
		player.getSkills().addXp(Skills.SMITHING, EXPERIENCE[option] + (((barId - 17650)) / 2) * 9);
		return 3;
	}

	@Override
	public boolean start(Player player) {
		player.getInterfaceManager().closeScreenInterface();
		if (player.getTemporaryAttributtes().get("dganvil") != null)
			this.object = (Position) player.getTemporaryAttributtes().remove("dganvil");
		if (!player.getInventory().containsItem(DUNGEONEERING_HAMMER, 1)) {
			player.getDialogueManager().startDialogue("SimpleMessage","You need a hammer in order to work with a bar of " + ItemDefinitions.getItemDefinitions(barId).getName().replace("Bar ", "") + ".");
			return false;
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < level) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a Smithing level of "
					+ level + " to create this.");
			return false;
		}
		if (!player.getInventory().containsItem(barId, BARS[option])) {
			player.sendMessage("You need at least " + BARS[option] + ItemDefinitions.getItemDefinitions(barId).getName() + (BARS[option] > 1 ? "s" : "") + " to create that.");
			return false;
		}
		return true;
	}

	@Override
	public void stop(Player player) {}

}
