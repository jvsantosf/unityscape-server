package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class MiniKrilTsutsaroth extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6201 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(4) == 0) {
			switch (Utils.getRandom(8)) {
			case 0:
				npc.setNextForceTalk(new ForceTalk("Attack them, you dogs!"));
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("Forward!"));
				break;
			case 2:
				npc.setNextForceTalk(new ForceTalk("Death to Saradomin's dogs!"));
				break;
			case 3:
				npc.setNextForceTalk(new ForceTalk("Kill them, you cowards!"));
				break;
			case 4:
				npc.setNextForceTalk(new ForceTalk(
						"The Dark One will have their souls!"));
				npc.playSound(3229, 2);
				break;
			case 5:
				npc.setNextForceTalk(new ForceTalk("Zamorak curse them!"));
				break;
			case 6:
				npc.setNextForceTalk(new ForceTalk("Rend them limb from limb!"));
				break;
			case 7:
				npc.setNextForceTalk(new ForceTalk("No retreat!"));
				break;
			case 8:
				npc.setNextForceTalk(new ForceTalk("Flay them all!"));
				break;
			}
		}
		int attackStyle = Utils.getRandom(2);
		switch (attackStyle) {
		case 0:// magic flame attack
			npc.animate(new Animation(14962));
			npc.setNextGraphics(new Graphics(1210));

				delayHit(
						npc,
						1,
						target,
						getMagicHit(
								npc,
								getRandomMaxHit(npc, 300,
										NPCCombatDefinitions.MAGE, target)));
				World.sendProjectile(npc, target, 1211, 41, 16, 41, 35, 16, 0);
				if (Utils.getRandom(4) == 0)
					target.getToxin().applyToxin(ToxinType.POISON);
			break;
		case 1:// main attack
		case 2:// melee attack
			int damage = 463;// normal
			for (Entity e : npc.getPossibleTargets()) {
				if (e instanceof Player
						&& (((Player) e).getPrayer().usingPrayer(0, 19) || ((Player) e)
								.getPrayer().usingPrayer(1, 9))) {
					Player player = (Player) e;
					damage = 497;
					npc.setNextForceTalk(new ForceTalk("YARRRRRRR!"));
					player.getPrayer().drainPrayer((Math.round(damage / 20)));
					player.setPrayerDelay(Utils.getRandom(5) + 5);
					player.getPackets()
							.sendGameMessage(
									"K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.");
				}
				npc.animate(new Animation(damage <= 463 ? 14968
						: 14968));
				delayHit(
						npc,
						0,
						e,
						getMeleeHit(
								npc,
								getRandomMaxHit(npc, damage,
										NPCCombatDefinitions.MELEE, e)));
			}
			break;
		}
		return defs.getAttackDelay();
	}
}
