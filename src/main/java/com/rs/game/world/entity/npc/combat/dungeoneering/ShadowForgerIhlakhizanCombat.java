package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.dungeonnering.ShadowForgerIhlakhizan;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.TemporaryAttributes;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.game.world.entity.updating.impl.NewForceMovement;
import com.rs.utility.Utils;

public class ShadowForgerIhlakhizanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ "Shadow-Forger Ihlakhizan" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {

		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final ShadowForgerIhlakhizan forger = (ShadowForgerIhlakhizan) npc;
		final DungeonManager manager = forger.getManager();

		for (Entity t : npc.getPossibleTargets())
			t.getTemporaryAttributtes().remove(TemporaryAttributes.Key.SHADOW_FORGER_SHADOW);

		if (Utils.random(4) == 0) {
			if (Utils.random(3) == 0) {
				npc.animate(new Animation(13019));
				npc.setNextGraphics(new Graphics(2370));
				for (int i = 0; i < 10; i++) {
					final Position tile = ((ShadowForgerIhlakhizan) npc).getManager().getTile(forger.getReference(), 2 + Utils.random(12), 2 + Utils.random(12));
					if (Utils.colides(npc.getX(), npc.getY(), npc.getSize(), tile.getX(), tile.getY(), 1))
						continue;
					World.sendProjectile(npc, tile, 2371, 120, 30, 41, 30, 16, 0);
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							World.sendGraphics(npc, new Graphics(2374), tile);
							for (Player player : forger.getManager().getParty().getTeam()) {
								if (player.isDead() || player.getX() != tile.getX() || player.getY() != tile.getY())
									continue;
								player.applyHit(new Hit(npc, Utils.random(npc.getMaxHit(NPCCombatDefinitions.RANGE)) + 1, HitLook.RANGE_DAMAGE));
							}
						}

					}, 2);
				}
				return npc.getAttackSpeed();
			} else {
				npc.setNextGraphics(new Graphics(2600));
				for (Entity t : npc.getPossibleTargets()) {
					if (t instanceof Player) {
						Player player = (Player) t;
						player.getPackets().sendGameMessage("The shadow-forger starts to glow.");
					}
				}
				forger.setUsedShadow();
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						npc.animate(new Animation(13016));
						for (Entity t : npc.getPossibleTargets()) {
							t.applyHit(new Hit(npc, Utils.random((int) (t.getMaxHitpoints() * 0.74)) + 1, HitLook.REGULAR_DAMAGE));
							if (t instanceof Player) {
								WorldTasksManager.schedule(new WorldTask() {
									private int ticks;
									private Position tile;

									@Override
									public void run() {
										ticks++;
										if (ticks == 1) {
											if (target instanceof Player) {
												Player player = (Player) target;
												player.lock(2);
												player.stopAll();
											}
											byte[] dirs = Utils.getDirection(npc.getDirection());
											for (int distance = 2; distance >= 0; distance--) {
												tile = new Position(new Position(target.getX() + (dirs[0] * distance), target.getY() + (dirs[1] * distance), target.getZ()));
												if (World.isFloorFree(tile.getZ(), tile.getX(), tile.getY()) && manager.isAtBossRoom(tile))
													break;
												else if (distance == 0)
													tile = new Position(target);
											}
											target.faceEntity(forger);
											target.animate(new Animation(10070));
											target.setNextForceMovement(new NewForceMovement(target, 0, tile, 2, target.getDirection()));
										} else if (ticks == 2) {
											target.setNextPosition(tile);
											stop();
											return;
										}
									}
								}, 0, 0);
								Player player = (Player) t;
								for (int stat = 0; stat < 7; stat++) {
									if (stat == Skills.HITPOINTS)
										continue;
									int drain = player.getSkills().getLevel(stat) / 2;
									if (stat == Skills.PRAYER)
										player.getPrayer().drainPrayer(drain * 10);
									player.getSkills().drainLevel(stat, drain);
								}
							}
						}
					}

				}, 3);
				return npc.getAttackSpeed() + 3;
			}
		}

		int attackStyle = Utils.random(Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0) ? 2 : 1);

		switch (attackStyle) {
		case 0:
			npc.animate(new Animation(13025));
			npc.setNextGraphics(new Graphics(2375));
			World.sendProjectile(npc, target, 2376, 120, 30, 60, 70, 16, 0);
			target.setNextGraphics(new Graphics(2377, 120, 0));
			delayHit(npc, 3, target, getRegularHit(npc, getMaxHit(npc, NPCCombatDefinitions.MAGE, target)));
			break;
		case 1:
			npc.animate(new Animation(defs.getAttackEmote()));
			for (Entity t : npc.getPossibleTargets()) {
				if (!Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), t.getX(), t.getY(), t.getSize(), 0))
					continue;
				delayHit(npc, 0, t, getMeleeHit(npc, getMaxHit(npc, NPCCombatDefinitions.MELEE, t)));
			}
			break;
		}

		return npc.getAttackSpeed();
	}
}
