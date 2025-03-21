package com.rs.game.world.entity.npc.familiar;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class Abyssalparasite extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Abyssalparasite(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	   @Override
	    public String getSpecialName() {
		return "Abyssal drain";
	    }

	    @Override
	    public String getSpecialDescription() {
		return "Lowers an opponent's prayer with a magic attack.";
	    }

	    @Override
	    public int getBOBSize() {
		return 7;
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
		final Entity target = (Entity) object;
		final int damage = Utils.random(100);
		animate(new Animation(7675));
		setNextGraphics(new Graphics(1422));
		World.sendProjectile(this, target, 1423, 34, 16, 30, 35, 16, 0);
		if (target instanceof Player)
		    ((Player) target).getPrayer().drainPrayer(damage / 2);
		WorldTasksManager.schedule(new WorldTask() {

		    @Override
		    public void run() {
			target.applyHit(new Hit(getOwner(), damage, HitLook.MAGIC_DAMAGE));
		    }
		}, 2);
		return false;
	    }
}
