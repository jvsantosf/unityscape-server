package com.rs.game.world.entity.player.actions.skilling.summoning;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;

/**
 * 1/16/2014
 * @author/Editor Regicidal
 * 
 */
public class SummoningScroll {

	private static final int INTERFACE = 666;

	public static void sendInterface(Player player) {
		player.getInterfaceManager().sendInterface(INTERFACE);
		Object options[] = { Integer.valueOf(78), Integer.valueOf(1),
				"List<col=FF9040>", "Transform-X<col=FF9040>",
				"Transform-All<col=FF9040>", "Transform-10<col=FF9040>",
				"Transform-5<col=FF9040>", "Transform<col=FF9040>",
				Integer.valueOf(10), Integer.valueOf(8),
				Integer.valueOf(INTERFACE << 16 | 0x10) };
		player.getPackets().sendRunScript(765, options);
		player.getPackets().sendIComponentSettings(INTERFACE, 16, 0, 462, 190);
	}

	private enum Scrolls {

		WOLF(12425, 0.1, 12047, 1), DREADFOWL(12445, 0.1, 12043, 4), FETCH_CASKET(
				19621, 0.1, 19622, 4), EGG_SPAWN(12428, 0.2, 12059, 10), SLIME_SPRAY(
				12459, 0.2, 12019, 13), GRANITE_CRAB(12533, 0.2, 12009, 16), PESTER_SCROLL(
				12838, 0.5, 12778, 17), ELECTRIC_LASH(12460, 0.4, 12049, 18), VENOM_SHOT(
				12432, 0.9, 12055, 19), FIREBALL(12839, 1.1, 12808, 22), CHEESE_FEAST(
				12430, 2.3, 12067, 23), SANDSTORM(12446, 2.5, 12063, 25), COMPOST_GENERATE(
				12440, 0.6, 12091, 28), EXPLODE(12834, 2.9, 12800, 29), VAMPYRE(
				12477, 1.5, 12052, 31), INSANE_FEROCITY(12433, 1.6, 12065, 32), MULTICHOP(
				12429, 0.7, 12021, 33), CALL_TO_ARMS(12443, 0.7, 12818, 34), CALL_TO_ARMS1(
				12443, 0.7, 12814, 34), CALL_TO_ARMS2(12443, 0.7, 12780, 34), CALL_TO_ARMS3(
				12443, 0.7, 12798, 34), BRONZE_BULL(12461, 3.6, 12073, 36), UNBURDEN(
				12431, 0.6, 12087, 40), HERBCALL(12422, 0.8, 12071, 41), EVIL_FLAMES(
				12448, 2.1, 12051, 42), IRON_BULL(12462, 4.6, 12075, 46), IMMENSE_HEAT(
				12829, 2.3, 12816, 46), THIEVING_FINGERS(12426, 0.9, 12041, 47), BLOOD_DRAIN(
				12444, 2.4, 12061, 49), TIRELESS_RUN(12441, 0.8, 12007, 52), ABYSSAL_DRAIN(
				12454, 1.1, 12035, 54), DISSOLVE(12453, 5.5, 12027, 55), FISH_RAIN(
				12424, 1.1, 12531, 56), STEEL_BULL(12463, 5.6, 12077, 56), AMBUSH(
				12836, 5.7, 12812, 57), RENDING(12840, 5.7, 12784, 57), GOAD(
				12835, 5.7, 12810, 57), DOOMSPHERE(12455, 5.8, 12023, 58), DUST_CLOUD(
				12468, 3, 12085, 61), ABYSSAL_STEALTH(12427, 1.9, 12037, 62), OPHIDIAN(
				12436, 3.1, 12051, 63), POISONOUS_BLAST(12467, 3.2, 12045, 64), MITHRIL_BULL(
				12464, 6.6, 12079, 66), TOAD_BARK(12452, 1, 12123, 66), TESTUDO(
				12439, 0.7, 12031, 67), SWALLOW_WHOLE(12438, 1.4, 12029, 68), FRUITFALL(
				12423, 1.4, 12033, 69), FAMINE(12830, 1.5, 12820, 70), ARCTIC_BLAST(
				12451, 1.1, 12057, 71), RISE_FROM_THE_ASHES(14622, 8, 14623, 72), VOLCANIC(
				12826, 7.3, 12792, 73), MANTIS_STRIKE(12450, 3.7, 12011, 75), INFERNO_SCROLL(
				12841, 1.5, 12782, 76), ADAMANT_BULL(12465, 7.6, 12081, 76), DEADLY_CLAW(
				12831, 11.4, 12794, 77), ACORN(12457, 1.6, 12013, 78), TITANS(
				12824, 7.9, 12802, 79), TITANS1(12824, 7.9, 12806, 79), TITANS2(
				12824, 7.9, 12804, 79), REGROWTH(12442, 1.6, 12025, 80), SPIKE_SHOT(
				12456, 4.1, 12017, 83), EBON_THUNDER(12837, 8.3, 12788, 83), SWAMP(
				12832, 4.1, 12776, 85), RUNE_BULL(12466, 8.6, 12083, 86), HEALING_AURA(
				12434, 1.8, 12039, 88), BOIL(12833, 8.9, 12786, 89), MAGIC_FOCUS(
				12437, 4.6, 12089, 92), ESSENCE_SHIPMENT(12827, 1.9, 12796, 93), IRON_WITHIN(
				12828, 4.7, 12822, 95), WINTER_STORAGE(12435, 4.8, 12093, 96), STEEL_OF_LEGENDS(
				12825, 4.9, 12790, 99);

		private int scrollId;
		private double experience;
		private int pouchId;
		private int reqLevel;

		private Scrolls(int scrollId, double xp, int pouchId, int reqLevel) {
			this.scrollId = scrollId;
			this.experience = xp;
			this.pouchId = pouchId;
			this.reqLevel = reqLevel;
		}

		/**
		 * The mapping.
		 */
		private static final Map<Integer, Scrolls> SCROLLS = new HashMap<Integer, Scrolls>();

		/**
		 * Populate the mapping.
		 */
		static {
			for (Scrolls pouch : Scrolls.values()) {
				SCROLLS.put(pouch.scrollId, pouch);
			}
		}

		/**
		 * Gets a summoning pouchId object from the mapping.
		 * 
		 * @param pouchId
		 *            The pouchId item id.
		 * @return The {@code SummoningPouch} {@code Object}, <br>
		 *         or {@code null} if the pouchId didn't exist.
		 */
		public static Scrolls get(int pouchId) {
			return SCROLLS.get(pouchId);
		}
	}

	public static void createScroll(Player player, int itemId, int amount) {
		Scrolls scroll = Scrolls.get(itemId);
		if (scroll == null) {
			player.getPackets().sendGameMessage("You do not have the pouch required to create this scroll.");
			return;
		}
		if (amount == 28 || amount > player.getInventory().getItems().getNumberOf(scroll.pouchId)) {
			amount = player.getInventory().getItems().getNumberOf(scroll.pouchId);
		}
		if (!player.getInventory().containsItem(scroll.pouchId, 1)) {
			player.getPackets().sendGameMessage("You do not have enough "+ ItemDefinitions.getItemDefinitions(scroll.pouchId).getName().toLowerCase()+ "es to create "+ amount+ " "+ ItemDefinitions.getItemDefinitions(scroll.scrollId).getName().toLowerCase() + "s.");
			return;
		}
		if (player.getSkills().getLevel(Skills.SUMMONING) < scroll.reqLevel) {
			player.getPackets().sendGameMessage("You need a summoning level of "+ scroll.reqLevel+ " to create "+ amount+ " "+ ItemDefinitions.getItemDefinitions(scroll.scrollId).getName().toLowerCase() + "s.");
		}
		player.getInventory().deleteItem(scroll.pouchId, amount);
		player.getInventory().addItem(scroll.scrollId, amount * 10);
		player.getSkills().addXp(Skills.SUMMONING, scroll.experience);
		player.getPackets().sendGameMessage("You have created some "+ ItemDefinitions.getItemDefinitions(scroll.scrollId).getName().toLowerCase() + "s.");
	}

}
