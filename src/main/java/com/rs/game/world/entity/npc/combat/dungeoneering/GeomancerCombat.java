package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.dungeonnering.HobgoblinGeomancer;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.dungeoneering.*;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class GeomancerCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 10059 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		HobgoblinGeomancer boss = (HobgoblinGeomancer) npc;

		boolean atDistance = !Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0);

		if (Utils.random(boss.getManager().getParty().getTeam().size() > 1 ? 6 : 8) == 0 && !boss.isCantInteract()) {
			Position tile = new Position(target);
			DungeonManager dungeon = boss.getManager();
			RoomReference rRef = dungeon.getCurrentRoomReference(tile);
			Room room = dungeon.getRoom(rRef);

			if (room != null && room.getRoom() == DungeonUtils.getBossRoomWithChunk(DungeonConstants.ABANDONED_FLOORS, 24, 640)) {
				sendEntangle(npc, target);
				boss.sendTeleport(tile, rRef);
			}
			return 7;
		}

		int attackType = Utils.random(6);
		switch (attackType) {
		case 0:// MELEE
			if (atDistance)
				sendEarthBlast(npc, target, true);
			else {
				npc.animate(new Animation(12989));
				delayHit(npc, 0, target, getMeleeHit(npc, getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
			}
			break;
		case 1:// EARTH BLAST
		case 2:
			sendEarthBlast(npc, target, attackType == 2);
			break;
		case 3:// WEAKEN
			sendWeaken(npc, target);
			break;
		case 4:// SNARE
			sendEntangle(npc, target);
			break;
		case 5:
			sendPrayerSpell(npc);
			break;
		}

		return 4;
	}

	private void sendEarthBlast(NPC npc, Entity target, boolean multiAttack) {
		npc.setNextGraphics(new Graphics(2715));
		npc.animate(new Animation(12990));
		for (Entity t : npc.getPossibleTargets()) {
			if (!multiAttack && t.getIndex() != target.getIndex())
				continue;
			t.setNextGraphics(new Graphics(2726, 75, 100));
			World.sendProjectile(npc, t, 2720, 50, 18, 50, 50, 0, 0);
			delayHit(npc, 1, t, getMagicHit(npc, getRandomMaxHit(npc, (int) (npc.getMaxHit(NPCCombatDefinitions.MAGE) * .7), NPCCombatDefinitions.MAGE, t)));
		}
	}

	private void sendEntangle(NPC npc, Entity target) {
		npc.setNextGraphics(new Graphics(177, 0, 50));
		npc.animate(new Animation(12992));
		npc.removeTarget();
		World.sendProjectile(npc, target, 178, 40, 18, 55, 70, 5, 0);

		int damage = getRandomMaxHit(npc, (int) (npc.getMaxHit(NPCCombatDefinitions.MAGE) * 0.95), NPCCombatDefinitions.MAGE, target);

		if (damage > 0) {
			target.setNextGraphics(new Graphics(180, 75, 100));
			target.setFreezeDelay(12);
			if (target instanceof Player)
				((Player) target).getActionManager().setActionDelay(4);
		}
	}

	private void sendWeaken(NPC npc, Entity target) {
		npc.setNextGraphics(new Graphics(105, 0, 60));
		npc.animate(new Animation(12992));
		World.sendProjectile(npc, target, 106, 40, 18, 55, 70, 5, 0);

		int damage = getMaxHit(npc, NPCCombatDefinitions.MAGE, target);

		if (damage > 0) {
			target.setNextGraphics(new Graphics(107, 75, 150));

			if (target instanceof Player) {
				Player player = (Player) target;

				for (int skill = 0; skill < Skills.MAGIC; skill++) {
					if (skill == 3 || skill == 5)
						continue;
					player.getSkills().set(skill, (int) (player.getSkills().getLevel(skill) * .95));
				}
				player.getPackets().sendGameMessage("Your stats have been significantly lowered.");
			}
		}
	}

	private void sendPrayerSpell(NPC npc) {
		npc.animate(new Animation(12988));
		npc.setNextGraphics(new Graphics(2147));

		boolean hasDrained = false;

		for (Entity t : npc.getPossibleTargets()) {
			int damage = getMaxHit(npc, NPCCombatDefinitions.MAGE, t);

			if (damage > 0) {
				if (t instanceof Player) {
					Player player = (Player) t;
					if (player.getPrayer().hasPrayersOn()) {
						if (!hasDrained) {
							int prayerPoints = player.getPrayer().getPrayerpoints();

							npc.setNextGraphics(new Graphics(2369, 70, 0));
							if (prayerPoints > 0) {
								npc.heal(prayerPoints);
								hasDrained = true;
							}
						}
						player.getPrayer().drainPrayer(player.getPrayer().getPrayerpoints());
						player.setPrayerDelay(1000);
						player.getPackets().sendGameMessage("Your prayers have been disabled.");
					}
				}
			}
			delayHit(npc, 1, t, getMagicHit(npc, (int) (damage * .50)));
			t.setNextGraphics(new Graphics(2147));
			World.sendProjectile(npc, t, 2368, 50, 18, 55, 70, 5, 0);
		}
	}
}
