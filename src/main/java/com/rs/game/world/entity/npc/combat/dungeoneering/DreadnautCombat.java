package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.dungeonnering.Dreadnaut;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class DreadnautCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ 12848 };//GFX 2859 Poop bubbles that drain prayer
	}

	@Override
	public int attack(NPC npc, Entity target) {
		Dreadnaut boss = (Dreadnaut) npc;

		if (!Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0))
			return 0;

		if (Utils.random(5) == 0) {
			npc.animate(new Animation(14982));
			npc.setNextGraphics(new Graphics(2865));
			int damage = getRandomMaxHit(boss, boss.getMaxHit(), NPCCombatDefinitions.MELEE, target);
			if (damage > 0) {
				target.setNextGraphics(new Graphics(2866, 75, 0));
				sendReductionEffect(boss, target, damage);
			}
			if (target instanceof Player) {
				Player player = (Player) target;
				player.getPackets().sendGameMessage("You have been injured and are unable to use protection prayers.");
				player.setPrayerDelay(8000);
			}
			delayHit(npc, 1, target, getMeleeHit(npc, damage));
		} else {
			npc.animate(new Animation(14973));
			npc.setNextGraphics(new Graphics(2856));

			for (Entity t : boss.getPossibleTargets()) {
				if (!t.withinDistance(target, 2))
					continue;
				int damage = getRandomMaxHit(boss, boss.getMaxHit(), NPCCombatDefinitions.MELEE, t);
				World.sendProjectile(boss, t, 2857, 30, 30, 25, 35, 15, 1);
				if (damage > 0) {
					sendReductionEffect(boss, t, damage);
					boss.addSpot(new Position(t));
				} else
					t.setNextGraphics(new Graphics(2858, 75, 0));
				delayHit(npc, 1, t, getMeleeHit(npc, damage));
			}
		}
		return 5;
	}

	private void sendReductionEffect(Dreadnaut boss, Entity target, int damage) {
		if (!boss.canReduceMagicLevel() || !(target instanceof Player))
			return;
		Player player = (Player) target;
		player.getSkills().set(Skills.MAGIC, (int) (player.getSkills().getLevel(Skills.MAGIC) - (damage * .10)));
	}
}
