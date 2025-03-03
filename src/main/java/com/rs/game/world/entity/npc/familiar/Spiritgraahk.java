package com.rs.game.world.entity.npc.familiar;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class Spiritgraahk extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Spiritgraahk(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

    @Override
    public String getSpecialName() {
	return "Groad";
    }

    @Override
    public String getSpecialDescription() {
	return "Attack the selected opponent at the cost of 3 special attack points.";
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
	return SpecialAttack.ENTITY;
    }

    @Override
    public boolean submitSpecial(Object object) {
	Entity entity = (Entity) object;
	if (getAttackedBy() != null) {
	    getOwner().getPackets().sendGameMessage("Your grahaak already has a target in its sights!");
	    return false;
	}
	getOwner().animate(new Animation(7660));
	getOwner().setNextGraphics(new Graphics(1316));
	this.getCombat().setTarget(entity);
	return false;
    }
}
