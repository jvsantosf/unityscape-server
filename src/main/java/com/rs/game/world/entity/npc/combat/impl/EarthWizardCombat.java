package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

/**
 * 
 * @author Mario(AlterOPSnet)
 * 
 *
 */

public class EarthWizardCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 2711 };
	}
	
	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		
		npc.setNextGraphics(new Graphics(2713));
		npc.animate(new Animation(14222));
		
		World.sendProjectile(npc,
								target,
										2718, 40, 40, 50, 50, 0, 0);
		delayHit(npc,
				14,
					target,
						getMagicHit(npc, Utils.random(50)));
		target.setNextGraphics(new Graphics(2723, 100, 100));
		
				return defs.getAttackDelay();

	}
}