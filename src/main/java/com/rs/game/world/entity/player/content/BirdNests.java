package com.rs.game.world.entity.player.content;

import com.rs.game.item.Item;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.utility.Utils;

/*
 * @author Gershon <rune-server.org> Credits to Optimum for most of it.
 * 
 * Aksel <http://www.rune-server.org/members/aksel/> for conversion to matrix.
 * 
 */
public class BirdNests {

	public static final int[] BIRD_NEST_IDS = { 5070, 5071, 5072, 5073, 5074 };
	public static final int[] SEED_REWARDS = { 5312, 5313, 5314, 5315, 5316,
			5283, 5284, 5285, 5286, 5287, 5288, 5289, 5290, 5317 };
	public static final int[] RING_REWARDS = { 1635, 1637, 1639, 1641, 1643 };
	public static final int EMPTY = 5075;
	public static final int RED = 5076;
	public static final int BLUE = 5077;
	public static final int GREEN = 5078;
	public static final int AMOUNT = 1;

	public static boolean isNest(final int itemId) {
		for (int nest : BIRD_NEST_IDS) {
			if (nest == itemId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Generates the random drop and creates a ground item where the player is
	 * standing
	 */
	public static void dropNest(Player player) {
		if (Utils.random(100) <= (player.getEquipment().wearingSkillCape(Skills.WOODCUTTING) ? 10 : 1)) {
			int r = Utils.random(1000);
			if (r >= 0 && r <= 640) {
				World.addGroundItem(new Item(5073), player.getTile());
			} else if (r >= 641 && r <= 960) {
				World.addGroundItem(new Item(5074), player.getTile());
			} else if (r >= 961) {
				int random = Utils.random(2);
				if (random == 1) {
					World.addGroundItem(new Item(5072), player.getTile());
				} else if (random == 2) {
					World.addGroundItem(new Item(5071), player.getTile());
				} else {
					World.addGroundItem(new Item(5070), player.getTile());
				}
			}
			player.nestsfallen++;
			player.out("<col=ff0000>A bird's nest falls out of the tree");
		}
	}

	/**
	 * Searches the nest.
	 */
	public static final void searchNest(Player player, int itemId) {
		if (!player.getInventory().hasFreeSlots()) {
			player.out("Not enough space in your inventory.");
			return;
		} else {
			eggNest(player, itemId);
			seedNest(player, itemId);
			ringNest(player, itemId);
			player.getInventory().deleteItem(itemId, 1);
			player.getInventory().addItem(EMPTY, AMOUNT);
		}

	}

	/**
	 * Determines what loot you get from ring bird nests
	 */
	public static final void ringNest(Player player, int itemId) {
		if (itemId == 5074) {
			int random = Utils.random(1000);
			if (random >= 0 && random <= 340) {
				player.getInventory().addItem(RING_REWARDS[0], AMOUNT);
			} else if (random >= 341 && random <= 750) {
				player.getInventory().addItem(RING_REWARDS[1], AMOUNT);
			} else if (random >= 751 && random <= 910) {
				player.getInventory().addItem(RING_REWARDS[2], AMOUNT);
			} else if (random >= 911 && random <= 989) {
				player.getInventory().addItem(RING_REWARDS[3], AMOUNT);
			} else if (random >= 990) {
				player.getInventory().addItem(RING_REWARDS[4], AMOUNT);
			}
		}
	}

	/**
	 * Determines what loot you get from seed bird nests
	 */
	public static final void seedNest(Player player, int itemId) {
		if (itemId == 5073) {
			int random = Utils.random(1000);
			player.out("Random = " + random);
			if (random >= 0 && random <= 220) {
				player.getInventory().addItem(SEED_REWARDS[0], AMOUNT);
			} else if (random >= 221 && random <= 350) {
				player.getInventory().addItem(SEED_REWARDS[1], AMOUNT);
			} else if (random >= 351 && random <= 400) {
				player.getInventory().addItem(SEED_REWARDS[2], AMOUNT);
			} else if (random >= 401 && random <= 430) {
				player.getInventory().addItem(SEED_REWARDS[3], AMOUNT);
			} else if (random >= 431 && random <= 440) {
				player.getInventory().addItem(SEED_REWARDS[4], AMOUNT);
			} else if (random >= 441 && random <= 600) {
				player.getInventory().addItem(SEED_REWARDS[5], AMOUNT);
			} else if (random >= 601 && random <= 700) {
				player.getInventory().addItem(SEED_REWARDS[6], AMOUNT);
			} else if (random >= 701 && random <= 790) {
				player.getInventory().addItem(SEED_REWARDS[7], AMOUNT);
			} else if (random >= 791 && random <= 850) {
				player.getInventory().addItem(SEED_REWARDS[8], AMOUNT);
			} else if (random >= 851 && random <= 900) {
				player.getInventory().addItem(SEED_REWARDS[9], AMOUNT);
			} else if (random >= 901 && random <= 930) {
				player.getInventory().addItem(SEED_REWARDS[10], AMOUNT);
			} else if (random >= 931 && random <= 950) {
				player.getInventory().addItem(SEED_REWARDS[11], AMOUNT);
			} else if (random >= 951 && random <= 970) {
				player.getInventory().addItem(SEED_REWARDS[12], AMOUNT);
			} else if (random >= 971 && random <= 980) {
				player.getInventory().addItem(SEED_REWARDS[13], AMOUNT);
			} else {
				player.getInventory().addItem(SEED_REWARDS[0], AMOUNT);
			}
		}
	}

	/**
	 * Egg nests
	 */
	public static final void eggNest(Player player, int itemId) {
		if (itemId == 5070) {
			player.getInventory().addItem(RED, AMOUNT);
		}
		if (itemId == 5071) {
			player.getInventory().addItem(GREEN, AMOUNT);
		}
		if (itemId == 5072) {
			player.getInventory().addItem(BLUE, AMOUNT);
		}
	}
}