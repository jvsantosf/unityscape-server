package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class KetZekCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] {"Ket-Zek", 15207};
	}//anims: DeathEmote: 9257 DefEmote: 9253 AttackAnim: 9252 gfxs: healing: 444 - healer

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		int hit = 0;
		if (distanceX > size || distanceX < -1 || distanceY > size
				|| distanceY < -1) {
			commenceMagicAttack(npc, target, hit);
			return defs.getAttackDelay();
		}
		int attackStyle = Utils.getRandom(1);
		switch(attackStyle) {
		case 0:
			hit = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target);
			npc.animate(new Animation(defs.getAttackEmote()));
			delayHit(npc, 0, target, getMeleeHit(npc, hit));
			break;
		case 1:
			commenceMagicAttack(npc, target, hit);
			break;
		}
		return defs.getAttackDelay();
	}

	private void commenceMagicAttack(final NPC npc, final Entity target, int hit) {
		hit = getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit() - 50, NPCCombatDefinitions.MAGE, target);
		npc.animate(new Animation(16136));
		//npc.setNextGraphics(new Graphics(1622, 0, 96 << 16));
		World.sendProjectile(npc, target, 2984, 34, 16, 30, 35, 16, 0);
		delayHit(npc, 2, target, getMagicHit(npc, hit));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				target.setNextGraphics(new Graphics(2983, 0, 96 << 16));
			}
		}, 2);
	}
}
