package com.rs.game.world.entity.player.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.activities.clanwars.FfaZone;
import com.rs.game.world.entity.player.content.activities.clanwars.RequestController;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.controller.impl.Kalaboss;
import com.rs.game.world.entity.player.controller.impl.Wilderness;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

/*
 * content package used for static stuff
 */
public class Magic {

	public static final int MAGIC_TELEPORT = 0, ITEM_TELEPORT = 1,
			OBJECT_TELEPORT = 2;

	@SuppressWarnings("unused")
	private static final int AIR_RUNE = 556, WATER_RUNE = 555,
	EARTH_RUNE = 557, FIRE_RUNE = 554, MIND_RUNE = 558,
	NATURE_RUNE = 561, CHAOS_RUNE = 562, DEATH_RUNE = 560,
	BLOOD_RUNE = 565, SOUL_RUNE = 566, ASTRAL_RUNE = 9075,
	LAW_RUNE = 563, STEAM_RUNE = 4694, MIST_RUNE = 4695,
	DUST_RUNE = 4696, SMOKE_RUNE = 4697, MUD_RUNE = 4698,
	LAVA_RUNE = 4699, COSMIC_RUNE = 564, ARMADYL_RUNE = 21773;

	public static final boolean hasInfiniteRunes(int runeId, int weaponId,
			int shieldId) {
		if (runeId == AIR_RUNE) {
			if (weaponId == 1381 || weaponId == 21777 || weaponId == 1397) {
				return true;
			}
		} else if (runeId == WATER_RUNE) {
			if (weaponId == 1383 || weaponId == 1395 || shieldId == 18346 || shieldId == 18346) {
				return true;
			}
		} else if (runeId == EARTH_RUNE) {
			if (weaponId == 1385 || weaponId == 1399) {
				return true;
			}
		} else if (runeId == FIRE_RUNE) {
			if (weaponId == 1387 || weaponId == 22494 || weaponId == 1393) {
				return true;
			}
		} else if (runeId == MIND_RUNE) {
			if (weaponId == 69) {
				return true;
			}
		} else if (runeId == NATURE_RUNE) {
			if (weaponId == 69 || weaponId == 18341) {
				return true;
			}
		} else if (runeId == CHAOS_RUNE) {
			if (weaponId == 69) {
				return true;
			}
		} else if (runeId == ARMADYL_RUNE) {
			if (weaponId == 69) {
				return true;
			}
		} else if (runeId == LAW_RUNE) {
			if (weaponId == 69 || weaponId == 18342) {
				return true;
			}
		} else if (runeId == ASTRAL_RUNE) {
			if (weaponId == 69) {
				return true;
			}
		} else if (runeId == SOUL_RUNE) {
			if (weaponId == 69) {
				return true;
			}
		} else if (runeId == DEATH_RUNE) {
			if (weaponId == 69) {
				return true;
			}
		} else if (runeId == BLOOD_RUNE) {
			if (weaponId == 69) {
				return true;
			}
		}
		return false;
	}

	public static final boolean checkCombatSpell(Player player, int spellId,
			int set, boolean delete) {
		if (spellId == 65535 || spellId == 65534 || spellId == 65533 || spellId == 65536 || spellId == 65537 || spellId == 65532) {
			return true;
		}
		switch (player.getCombatDefinitions().getSpellBook()) {
		case 193:
			switch (spellId) {
			case 28:
				if (!checkSpellRequirements(player, 50, delete, CHAOS_RUNE, 2,
						DEATH_RUNE, 2, FIRE_RUNE, 1, AIR_RUNE, 1)) {
					return false;
				}
				break;
			case 32:
				if (!checkSpellRequirements(player, 52, delete, CHAOS_RUNE, 2,
						DEATH_RUNE, 2, AIR_RUNE, 1, SOUL_RUNE, 1)) {
					return false;
				}
				break;
			case 24:
				if (!checkSpellRequirements(player, 56, delete, CHAOS_RUNE, 2,
						DEATH_RUNE, 2, BLOOD_RUNE, 1)) {
					return false;
				}
				break;
			case 20:
				if (!checkSpellRequirements(player, 58, delete, CHAOS_RUNE, 2,
						DEATH_RUNE, 2, WATER_RUNE, 2)) {
					return false;
				}
				break;
			case 30:
				if (!checkSpellRequirements(player, 62, delete, CHAOS_RUNE, 4,
						DEATH_RUNE, 2, FIRE_RUNE, 2, AIR_RUNE, 2)) {
					return false;
				}
				break;
			case 34:
				if (!checkSpellRequirements(player, 64, delete, CHAOS_RUNE, 4,
						DEATH_RUNE, 2, AIR_RUNE, 1, SOUL_RUNE, 2)) {
					return false;
				}
				break;
			case 26:
				if (!checkSpellRequirements(player, 68, delete, CHAOS_RUNE, 4,
						DEATH_RUNE, 2, BLOOD_RUNE, 2)) {
					return false;
				}
				break;
			case 22:
				if (!checkSpellRequirements(player, 70, delete, CHAOS_RUNE, 4,
						DEATH_RUNE, 2, WATER_RUNE, 4)) {
					return false;
				}
				break;
			case 29:
				if (!checkSpellRequirements(player, 74, delete, DEATH_RUNE, 2,
						BLOOD_RUNE, 2, FIRE_RUNE, 2, AIR_RUNE, 2)) {
					return false;
				}
				break;
			case 33:
				if (!checkSpellRequirements(player, 76, delete, DEATH_RUNE, 2,
						BLOOD_RUNE, 2, AIR_RUNE, 2, SOUL_RUNE, 2)) {
					return false;
				}
				break;
			case 25:
				if (!checkSpellRequirements(player, 80, delete, DEATH_RUNE, 2,
						BLOOD_RUNE, 4)) {
					return false;
				}
				break;
			case 21:
				if (!checkSpellRequirements(player, 82, delete, DEATH_RUNE, 2,
						BLOOD_RUNE, 2, WATER_RUNE, 3)) {
					return false;
				}
				break;
			case 31:
				if (!checkSpellRequirements(player, 86, delete, DEATH_RUNE, 4,
						BLOOD_RUNE, 2, FIRE_RUNE, 4, AIR_RUNE, 4)) {
					return false;
				}
				break;
			case 35:
				if (!checkSpellRequirements(player, 88, delete, DEATH_RUNE, 4,
						BLOOD_RUNE, 2, AIR_RUNE, 4, SOUL_RUNE, 3)) {
					return false;
				}
				break;
			case 27:
				if (!checkSpellRequirements(player, 92, delete, DEATH_RUNE, 4,
						BLOOD_RUNE, 4, SOUL_RUNE, 1)) {
					return false;
				}
				break;
			case 23:
				if (!checkSpellRequirements(player, 94, delete, DEATH_RUNE, 4,
						BLOOD_RUNE, 2, WATER_RUNE, 6)) {
					return false;
				}
				break;
			case 36: //Miasmic rush.
				if (!checkSpellRequirements(player, 61, delete, CHAOS_RUNE, 2, EARTH_RUNE, 1, SOUL_RUNE, 1)) {
					return false;
				}
				int weaponId = player.getEquipment().getWeaponId();
				if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
					player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
					player.getPackets().sendGameMessage("Extreme donators can cast Miasmic spells without Zuriel's staff.");
					return false;
				}
				break;
			case 38: //Miasmic burst.
				if (!checkSpellRequirements(player, 73, delete, CHAOS_RUNE, 4, EARTH_RUNE, 2, SOUL_RUNE, 2)) {
					return false;
				}
				weaponId = player.getEquipment().getWeaponId();
				if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
					player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
					player.getPackets().sendGameMessage("Extreme donators can cast Miasmic spells without Zuriel's staff.");
					return false;
				}
				break;
			case 37: //Miasmic blitz.
				if (!checkSpellRequirements(player, 85, delete, BLOOD_RUNE, 2, EARTH_RUNE, 3, SOUL_RUNE, 3)) {
					return false;
				}
				weaponId = player.getEquipment().getWeaponId();
				if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
					player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
					player.getPackets().sendGameMessage("Extreme donators can cast Miasmic spells without Zuriel's staff.");
					return false;
				}
				break;
			case 39: //Miasmic barrage.
				if (!checkSpellRequirements(player, 97, delete, BLOOD_RUNE, 4, EARTH_RUNE, 4, SOUL_RUNE, 4)) {
					return false;
				}
				weaponId = player.getEquipment().getWeaponId();
				if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
					player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
					player.getPackets().sendGameMessage("Extreme donators can cast Miasmic spells without Zuriel's staff.");
					return false;
				}
				break;
			default:
				return false;
			}
			break;
		case 192:
			switch (spellId) {
			case 25:
				if (!checkSpellRequirements(player, 1, delete, AIR_RUNE, 1,
						MIND_RUNE, 1)) {
					return false;
				}
				break;
			case 28:
				if (!checkSpellRequirements(player, 5, delete, WATER_RUNE, 1,
						AIR_RUNE, 1, MIND_RUNE, 1)) {
					return false;
				}
				break;
			case 30:
				if (!checkSpellRequirements(player, 9, delete, EARTH_RUNE, 2,
						AIR_RUNE, 1, MIND_RUNE, 1)) {
					return false;
				}
				break;
			case 32:
				if (!checkSpellRequirements(player, 13, delete, FIRE_RUNE, 3,
						AIR_RUNE, 2, MIND_RUNE, 1)) {
					return false;
				}
				break;
			case 34: // air bolt
				if (!checkSpellRequirements(player, 17, delete, AIR_RUNE, 2,
						CHAOS_RUNE, 1)) {
					return false;
				}
				break;
			case 36:// bind
				if (!checkSpellRequirements(player, 20, delete, EARTH_RUNE, 3,
						WATER_RUNE, 3, NATURE_RUNE, 2)) {
					return false;
				}
				break;
			case 55: // snare
				if (!checkSpellRequirements(player, 50, delete, EARTH_RUNE, 4,
						WATER_RUNE, 4, NATURE_RUNE, 3)) {
					return false;
				}
				break;
			case 81:// entangle
				if (!checkSpellRequirements(player, 79, delete, EARTH_RUNE, 5,
						WATER_RUNE, 5, NATURE_RUNE, 4)) {
					return false;
				}
				break;
			case 39: // water bolt
				if (!checkSpellRequirements(player, 23, delete, WATER_RUNE, 2,
						AIR_RUNE, 2, CHAOS_RUNE, 1)) {
					return false;
				}
				break;
			case 42: // earth bolt
				if (!checkSpellRequirements(player, 29, delete, EARTH_RUNE, 3,
						AIR_RUNE, 2, CHAOS_RUNE, 1)) {
					return false;
				}
				break;
			case 45: // fire bolt
				if (!checkSpellRequirements(player, 35, delete, FIRE_RUNE, 4,
						AIR_RUNE, 3, CHAOS_RUNE, 1)) {
					return false;
				}
				break;
			case 49: // air blast
				if (!checkSpellRequirements(player, 41, delete, AIR_RUNE, 3,
						DEATH_RUNE, 1)) {
					return false;
				}
				break;
			case 52: // water blast
				if (!checkSpellRequirements(player, 47, delete, WATER_RUNE, 3,
						AIR_RUNE, 3, DEATH_RUNE, 1)) {
					return false;
				}
				break;
			case 47: //crumble undead
				if (!checkSpellRequirements(player, 39, delete, AIR_RUNE, 2,
						EARTH_RUNE, 2, CHAOS_RUNE, 1)) {
					return false;
				}
				break;
			case 56: //magic dart
				if (player.getEquipment().getWeaponId() != 4170) {
					player.sm("You need to be equipping a Slayer's staff to cast this spell.");
					return false;
				}
				if (player.getSkills().getLevel(Skills.SLAYER) < 55) {
					player.sm("You need at least 55 Slayer to cast this spell.");
					return false;
				}
				if (!checkSpellRequirements(player, 1, delete, DEATH_RUNE, 1, MIND_RUNE, 4))
					return false;
				break;
			case 58: // earth blast
				if (!checkSpellRequirements(player, 53, delete, EARTH_RUNE, 4,
						AIR_RUNE, 3, DEATH_RUNE, 1)) {
					return false;
				}
				break;
			case 63: // fire blast
				if (!checkSpellRequirements(player, 59, delete, FIRE_RUNE, 5,
						AIR_RUNE, 4, DEATH_RUNE, 1)) {
					return false;
				}
				break;
			case 70: // air wave
				if (!checkSpellRequirements(player, 62, delete, AIR_RUNE, 5,
						BLOOD_RUNE, 1)) {
					return false;
				}
				break;
			case 73: // water wave
				if (!checkSpellRequirements(player, 65, delete, WATER_RUNE, 7,
						AIR_RUNE, 5, BLOOD_RUNE, 1)) {
					return false;
				}
				break;
			case 77: // earth wave
				if (!checkSpellRequirements(player, 70, delete, EARTH_RUNE, 7,
						AIR_RUNE, 5, BLOOD_RUNE, 1)) {
					return false;
				}
				break;
			case 80: // fire wave
				if (!checkSpellRequirements(player, 75, delete, FIRE_RUNE, 7,
						AIR_RUNE, 5, BLOOD_RUNE, 1)) {
					return false;
				}
				break;
			case 84:
				if (!checkSpellRequirements(player, 81, delete, AIR_RUNE, 7,
						DEATH_RUNE, 1, BLOOD_RUNE, 1)) {
					return false;
				}
				break;
			case 87:
				if (!checkSpellRequirements(player, 85, delete, WATER_RUNE, 10,
						AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1)) {
					return false;
				}
				break;
			case 89:
				if (!checkSpellRequirements(player, 85, delete, EARTH_RUNE, 10,
						AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1)) {
					return false;
				}
				break;
			case 66: // Sara Strike
				if (player.getEquipment().getWeaponId() != 2415) {
					player.getPackets()
					.sendGameMessage(
							"You need to be equipping a Saradomin staff to cast this spell.",
							true);
					return false;
				}
				if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4,
						FIRE_RUNE, 1, BLOOD_RUNE, 2)) {
					return false;
				}
				break;
			case 67: // Guthix Claws
				if (player.getEquipment().getWeaponId() != 2416) {
					player.getPackets()
					.sendGameMessage(
							"You need to be equipping a Guthix Staff or Void Mace to cast this spell.",
							true);
					return false;
				}
				if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4,
						FIRE_RUNE, 1, BLOOD_RUNE, 2)) {
					return false;
				}
				break;
			case 68: // Flame of Zammy
				if (player.getEquipment().getWeaponId() != 2417) {
					player.getPackets()
					.sendGameMessage(
							"You need to be equipping a Zamorak Staff to cast this spell.",
							true);
					return false;
				}
				if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4,
						FIRE_RUNE, 4, BLOOD_RUNE, 2)) {
					return false;
				}
				break;
			case 91:
				if (!checkSpellRequirements(player, 85, delete, FIRE_RUNE, 10,
						AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1)) {
					return false;
				}
				break;
			case 86: // teleblock
				if (!checkSpellRequirements(player, 85, delete, CHAOS_RUNE, 1,
						LAW_RUNE, 1, DEATH_RUNE, 1)) {
					return false;
				}
				break;
			case 99: // Storm of Armadyl
				if (!checkSpellRequirements(player, 77, delete, ARMADYL_RUNE, 1)) {
					return false;
				}
				break;
			default:
				return false;
			}
			break;
		default:
			return false;
		}
		if (set >= 0) {
			if (set == 0) {
				player.getCombatDefinitions().setAutoCastSpell(spellId);
			} else {
				player.getTemporaryAttributtes().put("tempCastSpell", spellId);
			}
		}
		return true;
	}

	public static final void setCombatSpell(Player player, int spellId) {
		if (player.getCombatDefinitions().getAutoCastSpell() == spellId) {
			player.getCombatDefinitions().resetSpells(true);
		} else {
			checkCombatSpell(player, spellId, 0, false);
		}
	}

	public static final void processLunarSpell(Player player, int spellId,
			int packetId) {
		player.stopAll(false);
		switch (spellId) {
		case 37:
			if (player.getSkills().getLevel(Skills.MAGIC) < 94) {
				player.getPackets().sendGameMessage(
						"Your Magic level is not high enough for this spell.");
				return;
			} else if (player.getSkills().getLevel(Skills.DEFENCE) < 40) {
				player.getPackets().sendGameMessage("You need a Defence level of 40 for this spell");
				return;
			}
			Long lastVeng = (Long) player.getTemporaryAttributtes().get("LAST_VENG");
			if (lastVeng != null && lastVeng + 30000 > Utils.currentTimeMillis()) {
				player.getPackets()
				.sendGameMessage(
						"Players may only cast vengeance once every 30 seconds.");
				return;
			}
			if (!checkRunes(player, true, ASTRAL_RUNE, 4, DEATH_RUNE, 2,
					EARTH_RUNE, 10)) {
				return;
			}
			player.setNextGraphics(new Graphics(726, 0, 100));
			player.animate(new Animation(4410));
			player.setCastVeng(true);
			player.getTemporaryAttributtes().put("LAST_VENG", Utils.currentTimeMillis());
			player.getPackets().sendGameMessage("You cast a vengeance.");
			break;



		case 74: //Vengeance group
			Long lastVengGroup = (Long) player.getTemporaryAttributtes().get("LAST_VENGGRO UP");

			if (lastVengGroup != null && lastVengGroup + 30000 > Utils.currentTimeMillis()) {
				player.getPackets()
				.sendGameMessage(
						"Players may only cast vengeance group once every 30 seconds.");
				return;
			}

			if (!player.isAtMultiArea()) {
				player.getPackets().sendGameMessage("You can only cast vengeance group in a multi area.");
				return;
			}
			if (player.getSkills().getLevel(Skills.MAGIC) < 95) {
				player.getPackets().sendGameMessage("You need a level of 95 magic to cast vengeance group.");
				return;
			}
			if (!checkRunes(player, true, ASTRAL_RUNE, 4, DEATH_RUNE, 3,
					EARTH_RUNE, 11)){

				player.getPackets().sendGameMessage("You don't have enough runes to cast vengeance group.");
				return;
			}
			int count = 0;
			for (Player other : World.getPlayers()) {
				if (other.withinDistance(player, 4)){
					other.getPackets().sendGameMessage("Someone cast the Group Vengeance spell and you were affected!");
					other.setCastVeng(true);
					other.setNextGraphics(new Graphics(725, 0, 100));
					other.getTemporaryAttributtes().put("LAST_VENGGROU P", Utils.currentTimeMillis());
					other.getTemporaryAttributtes().put("LAST_VENG", Utils.currentTimeMillis());
					count++;
				}
			}
			player.getPackets().sendGameMessage("The spell affected " + count + " nearby people.");
			player.setNextGraphics(new Graphics(725, 0, 100)); // may not be needed though
			player.animate(new Animation(4410));
			player.setCastVeng(true);
			player.getInventory().deleteItem(ASTRAL_RUNE, 4);
			player.getInventory().deleteItem(DEATH_RUNE, 3);
			player.getInventory().deleteItem(EARTH_RUNE, 11);
			break;

		case 34://Swap SpellBook
			if (!checkRunes(player, true, ASTRAL_RUNE, 3, COSMIC_RUNE, 2,
					LAW_RUNE, 1)){

				player.getPackets().sendGameMessage("You don't have enough runes to swap spellbooks.");
				return;
			}
			player.getDialogueManager().startDialogue("SwapSpellBook");
			break;

			//Lunar teleport spells by adam
		case 43://Lunar island
			sendLunarTeleportSpell(player, 69, 64, new Position(2100, 3914,
					0), LAW_RUNE, 1, ASTRAL_RUNE, 2, EARTH_RUNE, 2);
			break;

		case 54://ZMI alter
			sendLunarTeleportSpell(player, 71, 64, new Position(2455, 3233,
					0), LAW_RUNE, 1, ASTRAL_RUNE, 2, EARTH_RUNE, 6);
			break;

		case 67://South Falador
			sendLunarTeleportSpell(player, 72, 64, new Position(3007, 3327,
					0), LAW_RUNE, 1, ASTRAL_RUNE, 2, AIR_RUNE, 2);
			break;

		case 47://WaterBirth Island
			sendLunarTeleportSpell(player, 72, 64, new Position(2547, 3754,
					0), LAW_RUNE, 1, ASTRAL_RUNE, 2, WATER_RUNE, 1);
			break;

		case 22://Barbarian Outpost
			sendLunarTeleportSpell(player, 75, 64, new Position(2544, 3569,
					0), LAW_RUNE, 2, ASTRAL_RUNE, 2, FIRE_RUNE, 2);
			break;

		case 69://North Ardougne
			sendLunarTeleportSpell(player, 76, 64, new Position(2614, 3340,
					0), LAW_RUNE, 1, ASTRAL_RUNE, 2, WATER_RUNE, 5);
			break;

		case 41://Port Khazard
			sendLunarTeleportSpell(player, 78, 64, new Position(2660, 3157,
					0), LAW_RUNE, 2, ASTRAL_RUNE, 2, WATER_RUNE, 4);
			break;

		case 40://Fishing Guild
			sendLunarTeleportSpell(player, 85, 64, new Position(2614, 3385,
					0), LAW_RUNE, 3, ASTRAL_RUNE, 3, WATER_RUNE, 8);
			break;

		case 44://Catherby
			sendLunarTeleportSpell(player, 87, 64, new Position(2808, 3434,
					0), LAW_RUNE, 3, ASTRAL_RUNE, 3, WATER_RUNE, 10);
			break;

		case 51://Ice Plat
			sendLunarTeleportSpell(player, 89, 64, new Position(2971, 3915,
					0), LAW_RUNE, 3, ASTRAL_RUNE, 3, WATER_RUNE, 8);
			break;

		case 75://TrollHeim
			sendLunarTeleportSpell(player, 92, 64, new Position(2889, 3669,
					0), LAW_RUNE, 3, ASTRAL_RUNE, 3, WATER_RUNE, 10);



			break;
		case 53: //Heal Group


			if (Utils.currentTimeMillis() < 3000) {
				player.getPackets()
				.sendGameMessage(
						"You can only heal people every 30 seconds");
				return;
			}

			if (!player.isAtMultiArea()) {
				player.getPackets().sendGameMessage("You can only use this spell in multi");
				return;
			}
			if (player.getSkills().getLevel(Skills.MAGIC) < 95) {
				player.getPackets().sendGameMessage("You need a level of 95 magic to cast Heal Group");
				return;
			}
			if (!checkRunes(player, true, ASTRAL_RUNE, 4000000, BLOOD_RUNE, 2000000,
					LAW_RUNE, 3000000)){

				player.getPackets().sendGameMessage("You need 4m Astral runes, 2m blood runes and 3m law runes to cas this");
				return;
			}
			int count1 = 0;
			for (Player other : World.getPlayers()) {
				if (other.withinDistance(player, 5)){

					other.setNextGraphics(new Graphics(1690, 0, 100));

					count1++;
					int playerHp = player.getHitpoints();
					int mult = (int) (playerHp * 0.75);
					int split = mult / count1;
					other.getPackets().sendGameMessage("You have been healed: " + split + "  Hp by " +player.getDisplayName()+"");

					other.heal(split);
				}
			}
			player.getPackets().sendGameMessage("The spell affected " + count1 + " nearby people.");

			player.animate(new Animation(4411));

			player.getInventory().deleteItem(ASTRAL_RUNE, 4000000);
			player.getInventory().deleteItem(BLOOD_RUNE, 2000000);
			player.getInventory().deleteItem(LAW_RUNE, 3000000);
			break;

		case 39:
			useHomeTele(player);
			break;
		}
	}

	public static final void processAncientSpell(Player player, int spellId,
			int packetId) {
		player.stopAll(false);
		switch (spellId) {
		case 28:
		case 32:
		case 24:
		case 20:
		case 30:
		case 34:
		case 26:
		case 22:
		case 29:
		case 33:
		case 25:
		case 21:
		case 31:
		case 35:
		case 27:
		case 23:
		case 36:
		case 37:
		case 38:
		case 39:
			setCombatSpell(player, spellId);
			break;
		case 40:
			sendAncientTeleportSpell(player, 54, 64, new Position(3099, 9882,
					0), LAW_RUNE, 2, FIRE_RUNE, 1, AIR_RUNE, 1);
			break;
		case 41:
			sendAncientTeleportSpell(player, 60, 70, new Position(3222, 3336,
					0), LAW_RUNE, 2, SOUL_RUNE, 1);
			break;
		case 42:
			sendAncientTeleportSpell(player, 66, 76, new Position(3492, 3471,
					0), LAW_RUNE, 2, BLOOD_RUNE, 1);

			break;
		case 43:
			sendAncientTeleportSpell(player, 72, 82, new Position(3006, 3471,
					0), LAW_RUNE, 2, WATER_RUNE, 4);
			break;
		case 44:
			sendAncientTeleportSpell(player, 78, 88, new Position(2990, 3696,
					0), LAW_RUNE, 2, FIRE_RUNE, 3, AIR_RUNE, 2);
			break;
		case 45:
			sendAncientTeleportSpell(player, 84, 94, new Position(3217, 3677,
					0), LAW_RUNE, 2, SOUL_RUNE, 2);
			break;
		case 46:
			sendAncientTeleportSpell(player, 90, 100, new Position(3288, 3886,
					0), LAW_RUNE, 2, BLOOD_RUNE, 2);
			break;
		case 47:
			sendAncientTeleportSpell(player, 96, 106, new Position(2977, 3873,
					0), LAW_RUNE, 2, WATER_RUNE, 8);
			break;
		case 48:
			useHomeTele(player);
			break;
		}
	}

	public static final void processNormalSpell(Player player, int spellId,
			int packetId) {
		player.stopAll(false);
		switch (spellId) {
		case 25: // air strike
		case 28: // water strike
		case 30: // earth strike
		case 32: // fire strike
		case 34: // air bolt
		case 39: // water bolt
		case 42: // earth bolt
		case 45: // fire bolt
		case 47: // crumble undead
		case 49: // air blast
		case 52: // water blast
		case 58: // earth blast
		case 63: // fire blast
		case 70: // air wave
		case 73: // water wave
		case 77: // earth wave
		case 80: // fire wave
		case 99:
		case 84:
		case 87:
		case 89:
		case 91:
		case 36:
		case 55:
		case 81:
		case 66:
		case 67:
		case 68:
		case 56:
			setCombatSpell(player, spellId);
			break;
		case 27: // crossbow bolt enchant
			if (player.getSkills().getLevel(Skills.MAGIC) < 4) {
				player.getPackets().sendGameMessage(
						"Your Magic level is not high enough for this spell.");
				return;
			}
			player.stopAll();
			player.getInterfaceManager().sendInterface(432);
			break;
		case 24:
			useHomeTele(player);
			break;
		case 37: // mobi
			sendNormalTeleportSpell(player, 10, 19,
					new Position(2413, 2848, 0), LAW_RUNE, 1, WATER_RUNE, 1,
					AIR_RUNE, 1);
			break;
		case 40: // varrock
			sendNormalTeleportSpell(player, 25, 19,
					new Position(3212, 3424, 0), FIRE_RUNE, 1, AIR_RUNE, 3,
					LAW_RUNE, 1);
			break;
		case 43: // lumby
			sendNormalTeleportSpell(player, 31, 41,
					new Position(3222, 3218, 0), EARTH_RUNE, 1, AIR_RUNE, 3,
					LAW_RUNE, 1);
			break;
		case 46: // fally
			sendNormalTeleportSpell(player, 37, 48,
					new Position(2964, 3379, 0), WATER_RUNE, 1, AIR_RUNE, 3,
					LAW_RUNE, 1);
			break;
		case 51: // camelot
			sendNormalTeleportSpell(player, 45, 55.5, new Position(2757, 3478,
					0), AIR_RUNE, 5, LAW_RUNE, 1);
			break;
		case 57: // ardy
			sendNormalTeleportSpell(player, 51, 61,
					new Position(2664, 3305, 0), WATER_RUNE, 2, LAW_RUNE, 2);
			break;
		case 62: // watch
			sendNormalTeleportSpell(player, 58, 68,
					new Position(2547, 3113, 2), EARTH_RUNE, 2, LAW_RUNE, 2);
			break;
		case 69: // troll
			sendNormalTeleportSpell(player, 61, 68,
					new Position(2888, 3674, 0), FIRE_RUNE, 2, LAW_RUNE, 2);
			break;
		case 72: // ape
			sendNormalTeleportSpell(player, 64, 76,
					new Position(2776, 9103, 0), FIRE_RUNE, 2, WATER_RUNE, 2,
					LAW_RUNE, 2, 1963, 1);
			break;
		}
	}

	private static void useHomeTele(Player player) {
		player.stopAll();
		player.getInterfaceManager().sendInterface(1092);
	}

	public static final boolean checkSpellRequirements(Player player,
			int level, boolean delete, int... runes) {
		if (player.getSkills().getLevelForXp(Skills.MAGIC) < level) {
			player.getPackets().sendGameMessage(
					"Your Magic level is not high enough for this spell.");
			return false;
		}
		return checkRunes(player, delete, runes);
	}

	public static void DaemonheimTeleport(final Player player, final Position tile) {
		if (!player.getControlerManager().processItemTeleport(tile)) {
			return;
		}
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.animate(new Animation(13652));
					player.setNextGraphics(new Graphics(2602));
				} else if (loop == 5) {
					player.setNextPosition(tile);
				} else if (loop == 6) {
					player.animate(new Animation(13654));
					player.setNextGraphics(new Graphics(2603));
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	public static boolean hasStaffOfLight(int weaponId) {
		if (weaponId == 15486 || weaponId == 22207 || weaponId == 22209
				|| weaponId == 22211 || weaponId == 22213 || ToxicStaffOfTheDead.isToxicStaff(weaponId)) {
			return true;
		}
		return false;
	}

	public static void resourcesTeleport(final Player player, final int x,
			final int y, final int h) {
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.animate(new Animation(13288));
					player.setNextGraphics(new Graphics(2516));
				} else if (loop == 1) {
					player.setNextPosition(new Position(x, y, h));
					player.animate(new Animation(13285));
					player.setNextGraphics(new Graphics(2517));
					player.unlock();
				}
				loop++;
			}
		}, 0, 1);
	}

	public static final boolean checkRunes(Player player, boolean delete,
			int... runes) {
		int weaponId = player.getEquipment().getWeaponId();
		int shieldId = player.getEquipment().getShieldId();
		int runesCount = 0;
		while (runesCount < runes.length) {
			int runeId = runes[runesCount++];
			int ammount = runes[runesCount++];
			if (hasInfiniteRunes(runeId, weaponId, shieldId)|| player.getInventory().containsItem(42791, 1)) {
				continue;
			}
			if (player.getEquipment().wearingSkillCape(Skills.MAGIC) && Utils.getRandom(10) == 0) {
				continue;
			}
			if (hasStaffOfLight(weaponId) && Utils.getRandom(8) == 0 && runeId != 21773) {
				continue;
			}
			if (!player.getInventory().containsItem(runeId, ammount)) {
				player.getPackets().sendGameMessage(
						"You do not have enough "
								+ ItemDefinitions.getItemDefinitions(runeId)
								.getName().replace("rune", "Rune")
								+ "s to cast this spell.");
				return false;
			}
		}
		if (delete) {
			runesCount = 0;
			while (runesCount < runes.length) {
				int runeId = runes[runesCount++];
				int ammount = runes[runesCount++];
				if (hasInfiniteRunes(runeId, weaponId, shieldId)) {
					continue;
				}
				player.getInventory().deleteItem(runeId, ammount);
			}
		}
		return true;
	}

	public static final void sendAncientTeleportSpell(Player player, int level,
													  double xp, Position tile, int... runes) {
		sendTeleportSpell(player, 1979, -1, 1681, -1, level, xp, tile, 5, true,
				MAGIC_TELEPORT, runes);
	}
	public static final void sendLunarTeleportSpell(Player player, int level,
													double xp, Position tile, int... runes) {
		sendTeleportSpell(player, 9606, 8941, 1685, -1, level, xp, tile, 5, true,
				MAGIC_TELEPORT, runes);
	}

	public static final void sendNormalTeleportSpell(Player player, int level,
													 double xp, Position tile, int... runes) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, level, xp, tile, 3,
				true, MAGIC_TELEPORT, runes);
		player.atWarriorsGuild = false;
	}
	public static final void sendTrialTeleportSpell(Player player, int level,
													double xp, Position tile, int... runes) {
		sendTeleportSpell(player, 17542, 17537, 3402, 3399, level, xp, tile, 7,
				true, MAGIC_TELEPORT, runes);
	}

	public static final void sendPegasusTeleportSpell(Player player, int level,
													  double xp, Position tile, int... runes) {
		sendTeleportSpell(player, 17106, 17133, 3223, -1, level, xp, tile, 17,
				true, MAGIC_TELEPORT, runes);
	}

	public static final void sendDemonTeleportSpell(Player player, int level,
													double xp, Position tile, int... runes) {
		sendTeleportSpell(player, 17108, 17133, 3225, -1, level, xp, tile, 17,
				true, MAGIC_TELEPORT, runes);
	}

	public static final void sendDemon2TeleportSpell(Player player, int level,
													 double xp, Position tile, int... runes) {
		sendTeleportSpell(player, -1, -1, 3224, -1, level, xp, tile, 20,
				true, MAGIC_TELEPORT, runes);
	}

	public static final void sendCopterTeleportSpell(Player player, int level,
													 double xp, Position tile, int... runes) {
		sendTeleportSpell(player, 17191, 17133, 3254, -1, level, xp, tile, 15,
				true, MAGIC_TELEPORT, runes);
	}

	public static final void sendToadTeleportSpell(Player player, int level,
												   double xp, Position tile, int... runes) {
		sendTeleportSpell(player, 17080, 17133, 3220, -1, level, xp, tile, 10,
				true, MAGIC_TELEPORT, runes);
	}

	public static final void sendBoxTeleportSpell(Player player, int level,
												  double xp, Position tile, int... runes) {
		sendTeleportSpell(player, 17245, -1, 3258, -1, level, xp, tile, 11,
				true, MAGIC_TELEPORT, runes);
	}


	public static final boolean sendItemTeleportSpell(Player player,
			boolean randomize, int upEmoteId, int upGraphicId, int delay,
			Position tile) {
		player.atWarriorsGuild = false;
		return sendTeleportSpell(player, upEmoteId, -2, upGraphicId, -1, 0, 0,
				tile, delay, randomize, ITEM_TELEPORT);

	}

	public static void pushLeverTeleport(final Player player,
			final Position tile) {
		if (!player.getControlerManager().processObjectTeleport(tile)) {
			return;
		}
		player.animate(new Animation(2140));
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.unlock();
				Magic.sendObjectTeleportSpell(player, false, tile);
			}
		}, 1);
	}

	public static final void sendObjectTeleportSpell(Player player,
			boolean randomize, Position tile) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, 0, 0, tile, 3,
				randomize, OBJECT_TELEPORT);
	}

	public static final void sendDelayedObjectTeleportSpell(Player player,
			int delay, boolean randomize, Position tile) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, 0, 0, tile, delay,
				randomize, OBJECT_TELEPORT);
	}

	public static final boolean sendTeleportSpell(final Player player,
												  int upEmoteId, final int downEmoteId, int upGraphicId,
												  final int downGraphicId, int level, final double xp,
												  final Position tile, int delay, final boolean randomize,
												  final int teleType, int... runes) {
		if (player.isLocked()) {
			return false;
		}
		if (player.getSkills().getLevel(Skills.MAGIC) < level) {
			player.getPackets().sendGameMessage(
					"Your Magic level is not high enough for this spell.");
			return false;
		}
		if (!checkRunes(player, false, runes)) {
			return false;
		}
		if (teleType == MAGIC_TELEPORT) {
			if (!player.getControlerManager().processMagicTeleport(tile)) {
				return false;
			}
		} else if (teleType == ITEM_TELEPORT) {
			if (!player.getControlerManager().processItemTeleport(tile)) {
				return false;
			}
		} else if (teleType == OBJECT_TELEPORT) {
			if (!player.getControlerManager().processObjectTeleport(tile)) {
				return false;
			}
		}
		checkRunes(player, true, runes);
		player.stopAll();
		if (upEmoteId != -1) {
			player.animate(new Animation(upEmoteId));
		}
		if (upGraphicId != -1) {
			player.setNextGraphics(new Graphics(upGraphicId));
		}
		if (teleType == MAGIC_TELEPORT) {
			player.getPackets().sendSound(5527, 0, 2);
		}
		player.lock(3 + delay);
		WorldTasksManager.schedule(new WorldTask() {

			boolean removeDamage;

			@Override
			public void run() {
				if(!removeDamage) {
					Position teleTile = tile;
					if (randomize) {
						// attemps to randomize tile by 4x4 area
						for (int trycount = 0; trycount < 10; trycount++) {
							teleTile = new Position(tile, 2);
							if (World.canMoveNPC(tile.getZ(), teleTile.getX(),
									teleTile.getY(), player.getSize())) {
								break;
							}
							teleTile = tile;
						}
					}
					player.setNextPosition(teleTile);
					player.getControlerManager().magicTeleported(teleType);
					if (player.getControlerManager().getControler() == null) {
						teleControlersCheck(player, teleTile);
					}
					if (xp != 0) {
						player.getSkills().addXp(Skills.MAGIC, xp);
					}
					if (downEmoteId != -1) {
						player.animate(new Animation(
								downEmoteId == -2 ? -1 : downEmoteId));
					}
					if (downGraphicId != -1) {
						player.setNextGraphics(new Graphics(downGraphicId));
					}
					if (teleType == MAGIC_TELEPORT) {
						player.getPackets().sendSound(5524, 0, 2);
						player.setNextFacePosition(new Position(teleTile.getX(),
								teleTile.getY() - 1, teleTile.getZ()));
						player.setDirection(6);
					}
					removeDamage = true;
				}else {
					player.resetReceivedDamage();
					stop();
				}
			}
		}, delay, 0);
		return true;
	}
	
	public static final boolean sendTeleportSpell(final Player player,
												  int upEmoteId, final int downEmoteId, int upGraphicId,
												  final int downGraphicId, int level, final double xp,
												  final Position tile, int delay, final boolean randomize,
												  final int teleType, Item item, int... runes) {
		long currentTime = Utils.currentTimeMillis();
		if (player.isLocked()) {
			return false;
		}
		if (player.getSkills().getLevel(Skills.MAGIC) < level) {
			player.getPackets().sendGameMessage(
					"Your Magic level is not high enough for this spell.");
			return false;
		}
		if (!checkRunes(player, false, runes)) {
			return false;
		}
		if (teleType == MAGIC_TELEPORT) {
			if (!player.getControlerManager().processMagicTeleport(tile)) {
				return false;
			}
		} else if (teleType == ITEM_TELEPORT) {
			if (!player.getControlerManager().processItemTeleport(tile, item)) {
				return false;
			}
		} else if (teleType == OBJECT_TELEPORT) {
			if (!player.getControlerManager().processObjectTeleport(tile)) {
				return false;
			}
		}
		checkRunes(player, true, runes);
		player.stopAll();
		if (upEmoteId != -1) {
			player.animate(new Animation(upEmoteId));
		}
		if (upGraphicId != -1) {
			player.setNextGraphics(new Graphics(upGraphicId));
		}
		if (teleType == MAGIC_TELEPORT) {
			player.getPackets().sendSound(5527, 0, 2);
		}
		player.lock(3 + delay);
		WorldTasksManager.schedule(new WorldTask() {

			boolean removeDamage;

			@Override
			public void run() {
				if(!removeDamage) {
					Position teleTile = tile;
					if (randomize) {
						// attemps to randomize tile by 4x4 area
						for (int trycount = 0; trycount < 10; trycount++) {
							teleTile = new Position(tile, 2);
							if (World.canMoveNPC(tile.getZ(), teleTile.getX(),
									teleTile.getY(), player.getSize())) {
								break;
							}
							teleTile = tile;
						}
					}
					player.setNextPosition(teleTile);
					player.getControlerManager().magicTeleported(teleType);
					if (player.getControlerManager().getControler() == null) {
						teleControlersCheck(player, teleTile);
					}
					if (xp != 0) {
						player.getSkills().addXp(Skills.MAGIC, xp);
					}
					if (downEmoteId != -1) {
						player.animate(new Animation(
								downEmoteId == -2 ? -1 : downEmoteId));
					}
					if (downGraphicId != -1) {
						player.setNextGraphics(new Graphics(downGraphicId));
					}
					if (teleType == MAGIC_TELEPORT) {
						player.getPackets().sendSound(5524, 0, 2);
						player.setNextFacePosition(new Position(teleTile.getX(),
								teleTile.getY() - 1, teleTile.getZ()));
						player.setDirection(6);
					}
					removeDamage = true;
				}else {
					player.resetReceivedDamage();
					stop();
				}
			}
		}, delay, 0);
		return true;
	}

	private final static Position[] TABS = { new Position(3217, 3426, 0),
			new Position(3222, 3218, 0), new Position(2965, 3379, 0),
			new Position(2758, 3478, 0), new Position(2660, 3306, 0) };

	public static boolean useTabTeleport(final Player player, final int itemId) {
		if (itemId < 8007 || itemId > 8007 + TABS.length - 1) {
			return false;
		}
		if (useTeleTab(player, TABS[itemId - 8007])) {
			player.getInventory().deleteItem(itemId, 1);
		}
		return true;
	}

	public static boolean useTeleTab(final Player player, final Position tile) {
		if (!player.getControlerManager().processItemTeleport(tile)) {
			return false;
		}
		player.lock();
		player.animate(new Animation(9597));
		player.setNextGraphics(new Graphics(1680));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 0) {
					player.animate(new Animation(4731));
					stage = 1;
				} else if(stage == 1){
					Position teleTile = tile;
					// attemps to randomize tile by 4x4 area
					for (int trycount = 0; trycount < 10; trycount++) {
						teleTile = new Position(tile, 2);
						if (World.canMoveNPC(tile.getZ(), teleTile.getX(),
								teleTile.getY(), player.getSize())) {
							break;
						}
						teleTile = tile;
					}
					player.setNextPosition(teleTile);
					player.getControlerManager().magicTeleported(ITEM_TELEPORT);
					if (player.getControlerManager().getControler() == null) {
						teleControlersCheck(player, teleTile);
					}
					player.setNextFacePosition(new Position(teleTile.getX(),
							teleTile.getY() - 1, teleTile.getZ()));
					player.setDirection(6);
					player.animate(new Animation(-1));
					stage = 2;
				}else if (stage == 2) {
					player.resetReceivedDamage();
					player.unlock();
					stop();
				}

			}
		}, 2, 1);
		return true;
	}

	public static final boolean sendNormalTeleportSpell2(Player player, int level, double xp, Position tile, int... runes) {
		return sendTeleportSpell(player, 8939, 8941, 1576, 1577, level, xp, tile, 3, true, MAGIC_TELEPORT, runes);
	}

	public static void teleControlersCheck(Player player, Position teleTile) {
		if (Kalaboss.isAtKalaboss(teleTile)) {
			player.getControlerManager().startControler("Kalaboss");
		} else if (Wilderness.isAtWild(teleTile)) {
			player.getControlerManager().startControler("Wilderness");
		} else if (RequestController.inWarRequest(player)) {
			player.getControlerManager().startControler("clan_wars_request");
		} else if (FfaZone.inArea(player)) {
			player.getControlerManager().startControler("clan_wars_ffa");
		}
	}

	private Magic() {

	}

	public static final boolean sendTeleportSpell(final Player player,
												  int upEmoteId, final int downEmoteId, int upGraphicId,
												  final int downGraphicId, int level, final double xp,
												  final Position tile, int delay, final boolean randomize,
												  final int teleType, final boolean removecontroler, int... runes) {
		long currentTime = Utils.currentTimeMillis();
		if (player.isLocked()) {
			return false;
		}
		if (player.getSkills().getLevel(Skills.MAGIC) < level) {
			player.getPackets().sendGameMessage(
					"Your Magic level is not high enough for this spell.");
			return false;
		}
		if (runes != null) {
			if (!checkRunes(player, false, runes)) {
				return false;
			}
		}
		if (teleType == MAGIC_TELEPORT) {
			if (!player.getControlerManager().processMagicTeleport(tile)) {
				return false;
			}
		} else if (teleType == ITEM_TELEPORT) {
			if (!player.getControlerManager().processItemTeleport(tile)) {
				return false;
			}
		} else if (teleType == OBJECT_TELEPORT) {
			if (!player.getControlerManager().processObjectTeleport(tile)) {
				return false;
			}
		}
		if (runes != null) {
			checkRunes(player, true, runes);
		}
		player.stopAll();
		if (upEmoteId != -1) {
			player.animate(new Animation(upEmoteId));
		}
		if (upGraphicId != -1) {
			player.setNextGraphics(new Graphics(upGraphicId));
		}
		if (teleType == MAGIC_TELEPORT) {
			player.getPackets().sendSound(5527, 0, 2);
		}
		player.lock(3 + delay);
		player.getAppearence().transformIntoNPC(-1);
		player.getTemporaryAttributtes().put("isNpc", false);
		WorldTasksManager.schedule(new WorldTask() {

			boolean removeDamage;

			@Override
			public void run() {
				if (!removeDamage) {
					Position teleTile = tile;
					if (randomize) {
						// attemps to randomize tile by 4x4 area
						for (int trycount = 0; trycount < 10; trycount++) {
							if (tile != null) {
								teleTile = new Position(tile, 2);
							}
							if (teleTile != null) {
								if (World.canMoveNPC(tile.getZ(),
										teleTile.getX(), teleTile.getY(),
										player.getSize())) {
									break;
								}
							}
							teleTile = tile;
						}
					}
					player.setNextPosition(teleTile);
					if (removecontroler == true) {
						player.getControlerManager().magicTeleported(teleType);
						if (player.getControlerManager().getControler() == null) {
							teleControlersCheck(player, teleTile);
						}
					}
					if (xp != 0) {
						player.getSkills().addXp(Skills.MAGIC, xp);
					}
					if (downEmoteId != -1) {
						player.animate(new Animation(
								downEmoteId == -2 ? -1 : downEmoteId));
					}
					if (downGraphicId != -1) {
						player.setNextGraphics(new Graphics(downGraphicId));
					}
					if (teleType == MAGIC_TELEPORT) {
						player.getPackets().sendSound(5524, 0, 2);
						player.setNextFacePosition(new Position(teleTile
								.getX(), teleTile.getY() - 1, teleTile
								.getZ()));
						player.setDirection(6);
					}
					removeDamage = true;
				} else {
					player.resetReceivedDamage();
					stop();
				}
			}
		}, delay, 0);
		return true;
	}
}
