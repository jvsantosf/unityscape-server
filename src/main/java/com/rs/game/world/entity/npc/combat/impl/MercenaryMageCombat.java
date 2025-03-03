package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class MercenaryMageCombat extends CombatScript {

	public static final String[] ATTACKS = new String[] {
		"I will make you suffer!", "Death is your only option!", "Why fight back?",
		"It is time for you to die.", "IS THIS ALL YOU'VE GOT?"
	};
	
	@Override
	public Object[] getKeys() {
		return new Object[] {8335};
	}

	@Override
	public int attack(final NPC npc, Entity target) {
		int attackStyle = Utils.random(5);
		if(attackStyle == 0) {
			npc.animate(new Animation(1979));
			final Position center = new Position(target);
			World.sendGraphics(npc, new Graphics(2929), center);
			npc.setNextForceTalk(new ForceTalk("Obliterate!"));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					for(Player player : World.getPlayers()) { //lets just loop all players for massive moves
						if(player == null || player.isDead() || player.isFinished())
							continue;
						if(player.withinDistance(center, 3)) {
							if(!player.getMusicsManager().hasMusic(843))
								player.getMusicsManager().playMusic(843);
							delayHit(npc, 0, player, new Hit(npc, Utils.random(150), HitLook.REGULAR_DAMAGE));
						}
					}
				}
				
			}, 4);
		}else if(attackStyle == 1) {
			npc.animate(new Animation(1979));
			final Position center = new Position(target);
			World.sendGraphics(npc, new Graphics(2191), center);
			npc.setNextForceTalk(new ForceTalk("How are the burns?"));
			WorldTasksManager.schedule(new WorldTask() {
				int count = 0;
				@Override
				public void run() {
					for(Player player : World.getPlayers()) { //lets just loop all players for massive moves
						if(player == null || player.isDead() || player.isFinished())
							continue;
						if(player.withinDistance(center, 1)) {
							delayHit(npc, 0, player, new Hit(npc, Utils.random(300), HitLook.REGULAR_DAMAGE));
						}
					}
					if(count++ == 10) {
						stop();
						return;
					}
				}
			}, 0, 0);
		}else if(attackStyle == 2) {
			npc.animate(new Animation(1979));
			final int dir = Utils.random(Utils.DIRECTION_DELTA_X.length);
			final Position center = new Position(npc.getX() + Utils.DIRECTION_DELTA_X[dir] * 5, npc.getY() + Utils.DIRECTION_DELTA_Y[dir] * 5, 0);
			npc.setNextForceTalk(new ForceTalk("I think it's time to clean my room!"));
			WorldTasksManager.schedule(new WorldTask() {
				int count = 0;
				@Override
				public void run() {
					for(Player player : World.getPlayers()) { //lets just loop all players for massive moves
						if(Utils.DIRECTION_DELTA_X[dir] == 0) {
							if(player.getX() != center.getX())
								continue;
						}
						if(Utils.DIRECTION_DELTA_Y[dir] == 0) {
							if(player.getY() != center.getY())
								continue;
						}
						if(Utils.DIRECTION_DELTA_X[dir] != 0) {
							if(Math.abs(player.getX() - center.getX()) > 5)
								continue;
						}
						if(Utils.DIRECTION_DELTA_Y[dir] != 0) {
							if(Math.abs(player.getY() - center.getY()) > 5)
								continue;
						}
						delayHit(npc, 0, player, new Hit(npc, Utils.random(150), HitLook.REGULAR_DAMAGE));
					}
					if(count++ == 5) {
						stop();
						return;
					}
				}
			}, 0, 0);
			World.sendProjectile(npc, center, 2196, 0, 0, 5, 35, 0, 0);
		}else if(attackStyle == 3) {
			delayHit(
					npc,
					2,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, Utils.random(300),
									NPCCombatDefinitions.MAGE, target)));
			World.sendProjectile(npc, target, 2873, 34, 16, 40, 35, 16, 0);
			npc.animate(new Animation(14221));
			npc.setNextForceTalk(new ForceTalk(ATTACKS[Utils.random(ATTACKS.length)]));
		}else if(attackStyle == 4) {
			npc.animate(new Animation(1979));
			npc.setNextGraphics(new Graphics(444));
			npc.heal(300);
		}
		return 5;
	}

}
