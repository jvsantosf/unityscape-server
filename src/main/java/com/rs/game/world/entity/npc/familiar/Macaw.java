package com.rs.game.world.entity.npc.familiar;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.HerbCleaning.Herbs;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class Macaw extends Familiar {

	/**
	 * 
	 */
	 private int specialLock = -1;
	private static final long serialVersionUID = -7805271915467121215L;

	public Macaw(Player owner, Pouches pouch, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

    @Override
    public String getSpecialName() {
	return "Herbcall";
    }

    @Override
    public void processNPC() {
	super.processNPC();
	if (specialLock > 0)
	    specialLock--;
	else if (specialLock == 0) {
	    specialLock = -1;
	    getOwner().getPackets().sendGameMessage("Your macaw feels rested and ready for flight again.");
	}
    }

    @Override
    public String getSpecialDescription() {
	return "Can produce one herb, all herbs up to and including Torstol, within a 60 second range.";
    }

    @Override
    public int getBOBSize() {
	return 0;
    }

    @Override
    public int getSpecialAmount() {
	return 12;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
	return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(Object object) {
	if (specialLock > 0) {
	    getOwner().getPackets().sendGameMessage("Your macaw is too tired to continue searching for herbs.");
	    return false;
	}
	specialLock = 100;
	getOwner().setNextGraphics(new Graphics(1300));
	getOwner().animate(new Animation(7660));
	animate(new Animation(8013));
	final Position tile = new Position(getOwner().getX() - 1, getOwner().getY(), getOwner().getZ());
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		World.sendGraphics(getOwner(), new Graphics(1321), tile);
		WorldTasksManager.schedule(new WorldTask() {

		    @Override
		    public void run() {
			animate(new Animation(8014));
		    }
		}, 3);
	    }
	}, 2);
	Herbs herb;
	if (Utils.getRandom(100) >= 5)
	    herb = Herbs.values()[Utils.random(Herbs.values().length)];
	else
	    herb = Herbs.values()[Utils.getRandom(3)];
	World.addGroundItem(new Item(herb.getHerbId(), 1), tile, getOwner(), true, 180, false);
	return true;
    }
}
