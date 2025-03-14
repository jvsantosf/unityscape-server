package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.dungeonnering.Rammernaut;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.TemporaryAttributes;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class RammernautCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ 9767 };
	}

	public static int getChargeCount(NPC npc) {

		Integer charge = (Integer) npc.getTemporaryAttributtes().get(TemporaryAttributes.Key.RAMMERNAUT_CHARGE);

		return charge == null ? 0 : charge;

	}

	public static void setChargeCount(NPC npc, int count) {
		npc.getTemporaryAttributtes().put(TemporaryAttributes.Key.RAMMERNAUT_CHARGE, count);

	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int chargeCount = getChargeCount(npc);

		if (chargeCount > 1 && target instanceof Player) {
			((Rammernaut) npc).setChargeTarget((Player) target);
			setChargeCount(npc, 0);
			return 0;
		}

		if (!Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0)) {
			setChargeCount(npc, chargeCount + 1);
			return 3;
		}
		setChargeCount(npc, Utils.random(10) == 0 ? 2 : 0); //1 in 10 change charging next att

		if (Utils.random(5) == 0) {
			npc.animate(new Animation(13705));
			for (Entity entity : npc.getPossibleTargets()) {
				if (!Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), entity.getX(), entity.getY(), entity.getSize(), 0))
					continue;
				((Rammernaut) npc).applyStunHit(entity, npc.getMaxHit(NPCCombatDefinitions.MELEE));
			}
			return npc.getAttackSpeed();
		}

		if (((Rammernaut) npc).isRequestSpecNormalAttack() && target instanceof Player) {
			((Rammernaut) npc).setRequestSpecNormalAttack(false);
			Player player = (Player) target;
			player.getPackets().sendGameMessage("Your prayers have been disabled.");
			player.setPrayerDelay(8000);
			player.getPackets().sendGameMessage("Your defence been reduced.");
			player.getSkills().drainLevel(Skills.DEFENCE, Utils.random(3) + 1);

		}

		//default melee attack can be protected with prayer
		npc.animate(new Animation(defs.getAttackEmote()));
		delayHit(npc, 0, target, getMeleeHit(npc, getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
		return npc.getAttackSpeed();
	}
}
