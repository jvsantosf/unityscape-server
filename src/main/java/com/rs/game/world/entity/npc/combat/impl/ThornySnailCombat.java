package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class ThornySnailCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6807, 6806 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			npc.animate(new Animation(8148));
			npc.setNextGraphics(new Graphics(1385));
			World.sendProjectile(npc, target, 1386, 34, 16, 30, 35, 16, 0);
			delayHit(
					npc,
					1,
					target,
					getRangeHit(
							npc,
							getRandomMaxHit(npc, 80,
									NPCCombatDefinitions.RANGE, target)));
			npc.setNextGraphics(new Graphics(1387));
		} else {
			npc.animate(new Animation(8143));
			delayHit(
					npc,
					1,
					target,
					getRangeHit(
							npc,
							getRandomMaxHit(npc, 40,
									NPCCombatDefinitions.RANGE, target)));
		}
		return defs.getAttackDelay();
	}

}
