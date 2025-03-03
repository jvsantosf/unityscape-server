package com.rs.game.world.entity.npc.familiar;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class Voidspinner extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1639238550551778316L;

	public Voidspinner(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Call To Arms";
	}

	@Override
	public String getSpecialDescription() {
		return "Teleports the player to Void Outpost.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 3;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		player.setNextGraphics(new Graphics(1316));
		player.animate(new Animation(7660));
		// Magic.sendTeleportSpell(player, upEmoteId, downEmoteId, upGraphicId,
		// downGraphicId, 0, 0, tile, 3, true, Magic.OBJECT_TELEPORT);
		return true;
	}
}
