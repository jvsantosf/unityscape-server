package com.rs.game.world.entity.npc.pet;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.familiar.Bunyip;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.npc.familiar.Talonbeast;
import com.rs.game.world.entity.player.BuffManager;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.PlayerCombat;
import com.rs.game.world.entity.player.content.pets.PetDetails;
import com.rs.game.world.entity.player.content.pets.Pets;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * Represents a pet.
 * @author Emperor
 *
 */
public final class Pet extends NPC {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -2848843157767889742L;

	/**
	 * The owner.
	 */
	private final Player owner;

	/**
	 * The "near" directions.
	 */
	private final int[][] checkNearDirs;

	/**
	 * The item id.
	 */
	private final int itemId;

	/**
	 * The pet details.
	 */
	private final PetDetails details;

	/**
	 * The growth rate of the pet.
	 */
	private double growthRate;

	/**
	 * The pets type.
	 */
	private final Pets pet;

	/**
	 * The chasing target.
	 */
	private NPC chasing;

	/**
	 * Constructs a new {@code Pet} {@code Object}.
	 * @param id The NPC id.
	 * @param itemId The item id.
	 * @param owner The owner.
	 * @param tile The world tile.
	 */
	public Pet(int id, int itemId, Player owner, Position tile, PetDetails details) {
		super(id, tile, -1, false);
		this.owner = owner;
		this.itemId = itemId;
		checkNearDirs = Utils.getCoordOffsetsNear(super.getSize());
		this.details = details;
		pet = Pets.forId(itemId);
		if (pet == Pets.TROLL_BABY && owner.getPetManager().getTrollBabyName() != null) {
			setName(owner.getPetManager().getTrollBabyName());
		}
		sendMainConfigurations();
		sendFollowerDetails();
	}



	@Override
	public void processNPC() {
//		attack(owner.getAttackedBy());
		unlockOrb();
		if (pet == Pets.HOLYDRAKE && !owner.getBuffManager().hasBuff(BuffManager.BuffType.PRAYER_RENEWAL)) {
			owner.getBuffManager().applyBuff(new BuffManager.Buff(BuffManager.BuffType.PRAYER_RENEWAL, 500, true));
		}
		if (pet == Pets.BLOODSPIDER) {
			if (Utils.getRandom(40) == 0) {
				//owner.heal(3);
				animate(27989);
				owner.applyHit(new Hit(owner, 30, HitLook.HEALED_DAMAGE));
				owner.setNextGraphics(new Graphics(Graphics.asOSRS(1542), 0, 0));
			}
		}
		if (pet == Pets.TROLL_BABY || pet.getFood().length > 0) {
			details.updateHunger(0.025);
			owner.getPackets().sendConfigByFile(4286, (int) details.getHunger());
		}
		if (details.getHunger() >= 90.0 && details.getHunger() < 90.025) {
			owner.getPackets().sendGameMessage("<col=ff0000>Your pet is starving, feed it before it runs off.</col>");
		} else if (details.getHunger() == 100.0) {
			owner.getPetManager().setNpcId(-1);
			owner.getPetManager().setItemId(-1);
			owner.setPet(null);
			owner.getPetManager().removeDetails(itemId);
			owner.getPackets().sendGameMessage("Your pet has ran away to find some food!");
			switchOrb(false);
			owner.getPackets().closeInterface(owner.getInterfaceManager().hasRezizableScreen() ? 98 : 212);
			owner.getPackets().sendIComponentSettings(747, 17, 0, 0, 0);
			finish();
			return;
		}
		if (growthRate > 0.000) {
			details.updateGrowth(growthRate);
			owner.getPackets().sendConfigByFile(4285, (int) details.getGrowth());
			if (details.getGrowth() == 100.0) {
				growNextStage();
			}
		}
		if (!withinDistance(owner, 12)) {
			call();
			return;
		}
		if (getChasing() != null) {
			sendChasing();
			if (getWalkSteps().isEmpty()) {
				animate(new Animation(9163));
				boolean caught = Utils.random(100) <= (isKitten() ? 5 : 70);
				if (caught) {
					chasing.applyHit(new Hit(owner, chasing.getMaxHitpoints(), HitLook.REGULAR_DAMAGE));
					owner.setNextForceTalk(new ForceTalk("Great job puss!"));
				} else {
					owner.setNextForceTalk(new ForceTalk("You shit cat!"));
				}
				setChasing(null);
				setNextForceTalk(new ForceTalk("Meow!"));
			}
		} else if (pet.isCanAttack()) {
			getCombat().process();
			if (owner.getActionManager().getAction() instanceof PlayerCombat) {
				Entity target = ((PlayerCombat) owner.getActionManager().getAction()).getTarget();
				if (canAttack(target))
					getCombat().setTarget(target);
			} else
				getCombat().removeTarget();
			if (!getCombat().underCombat())
				sendFollow();
		} else
			sendFollow();
	}

	/**
	 * Grows into the next stage of this pet (if any).
	 */
	public void growNextStage() {
		if (details.getStage() == 3) {
			return;
		}
		if (pet == null) {
			return;
		}
		int npcId = pet.getNpcId(details.getStage() + 1);
		if (npcId < 1) {
			return;
		}
		details.setStage(details.getStage() + 1);
		int itemId = pet.getItemId(details.getStage());
		if (pet.getNpcId(details.getStage() + 1) > 0) {
			details.updateGrowth(-100.0);
		}
		owner.getPetManager().setItemId(itemId);
		owner.getPetManager().setNpcId(npcId);
		finish();
		Pet newPet = new Pet(npcId, itemId, owner, owner, details);
		newPet.growthRate = growthRate;
		owner.setPet(newPet);
		owner.getPackets().sendGameMessage("<col=ff0000>Your pet has grown larger.</col>");
	}

	public boolean canAttack(Entity target) {
		if (target instanceof Player) {
//			Player player = (Player) target;
//			if (!owner.isCanPvp() || !player.isCanPvp())
				return false;
		}
		return !target.isDead()
				&& ((owner.isAtMultiArea() && isAtMultiArea() && target
				.isAtMultiArea()) || (owner.isForceMultiArea() && target
				.isForceMultiArea()))
				&& owner.getControlerManager().canAttack(target);
	}
	/**
	 * Picks up the pet.
	 */
	public void pickup() {
		if (!owner.getInventory().hasFreeSlots()) {
			owner.sm("You have no space in your inventory to carry your pet, it have been banked!");
			owner.getBank().addItem(itemId, 1, true);;
			owner.setPet(null);
			owner.getPetManager().setNpcId(-1);
			owner.getPetManager().setItemId(-1);
			switchOrb(false);
			owner.getPackets().closeInterface(owner.getInterfaceManager().hasRezizableScreen() ? 98 : 212);
			owner.getPackets().sendIComponentSettings(747, 17, 0, 0, 0);
			finish();
		} else {
			owner.getInventory().addItem(itemId, 1);
			owner.setPet(null);
			owner.getPetManager().setNpcId(-1);
			owner.getPetManager().setItemId(-1);
			switchOrb(false);
			owner.getPackets().closeInterface(owner.getInterfaceManager().hasRezizableScreen() ? 98 : 212);
			owner.getPackets().sendIComponentSettings(747, 17, 0, 0, 0);
			finish();
		}
	}

	/**
	 * Calls the pet.
	 */
	public void call() {
		int size = getSize();
		Position teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			final Position tile = new Position(new Position(owner.getX() + checkNearDirs[0][dir], owner.getY()
					+ checkNearDirs[1][dir], owner.getZ()));
			if (World.canMoveNPC(tile.getZ(), tile.getX(), tile.getY(), size)) {
				teleTile = tile;
				break;
			}
		}
		if (teleTile == null) {
			return;
		}
		setNextPosition(teleTile);
	}

	/**
	 * Follows the owner.
	 */
	protected void sendChasing() {
		setNextFaceEntity(chasing);
		int size = getSize();
		int distanceX = chasing.getX() - getX();
		int distanceY = chasing.getY() - getY();
		if (distanceX < size && distanceX > -1 && distanceY < size
				&& distanceY > -1 && !chasing.hasWalkSteps() && !hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(chasing.getX() + 1, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(chasing.getX() - size , getY())) {
					resetWalkSteps();
					if (!addWalkSteps(getX(), chasing.getY() + 1)) {
						resetWalkSteps();
						addWalkSteps(getX(), chasing.getY() - size );
					}
				}
			}
			return;
		}
		if (!clipedProjectile(chasing, true) || distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
			resetWalkSteps();
			addWalkStepsInteract(chasing.getX(), chasing.getY(), getRun() ? 2 : 1, size, true);
			return;
		}
		resetWalkSteps();
	}



	/**
	 * Follows the owner.
	 */
	@Override
	protected void sendFollow() {
		if (getLastFaceEntity() != owner.getClientIndex()) {
			setNextFaceEntity(owner);
		}
		if (getFreezeDelay() >= Utils.currentTimeMillis() || getFollowing() != null) {
			return;
		}
		int size = getSize();
		int distanceX = owner.getX() - getX();
		int distanceY = owner.getY() - getY();
		if (distanceX < size && distanceX > -1 && distanceY < size
				&& distanceY > -1 && !owner.hasWalkSteps() && !hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(owner.getX() + 1, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(owner.getX() - size , getY())) {
					resetWalkSteps();
					if (!addWalkSteps(getX(), owner.getY() + 1)) {
						resetWalkSteps();
						addWalkSteps(getX(), owner.getY() - size );
					}
				}
			}
			return;
		}
		if (!clipedProjectile(owner, true) || distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
			resetWalkSteps();
			addWalkStepsInteract(owner.getX(), owner.getY(), getRun() ? 2 : 1, size, true);
			return;
		}
		resetWalkSteps();
	}

	/**
	 * Sends the main configurations for the Pet interface (+ summoning orb).
	 */
	public void sendMainConfigurations() {
		switchOrb(true);
		owner.getPackets().sendConfig(448, itemId);// configures
		owner.getPackets().sendConfig(1160, 243269632); // sets npc emote
		owner.getPackets().sendGlobalConfig(1436, 0);
		unlockOrb(); // temporary
	}

	/**
	 * Sends the follower details.
	 */
	public void sendFollowerDetails() {
		owner.getPackets().sendConfigByFile(4285, (int) details.getGrowth());
		owner.getPackets().sendConfigByFile(4286, (int) details.getHunger());
		boolean res = owner.getInterfaceManager().hasRezizableScreen();
		owner.getPackets().sendInterface(true, res ? 746 : 548, res ? 119 : 179, 662);
		unlock();
		owner.getPackets().sendGlobalConfig(168, 8);// tab id
	}

	/**
	 * Switch the Summoning orb state.
	 * @param enable If the orb should be enabled.
	 */
	public void switchOrb(boolean enable) {
		owner.getPackets().sendConfig(1174, enable ? getId() : 0);
		if (enable) {
			unlock();
			return;
		}
		lockOrb();
	}

	/**
	 * Checks if pet is a kitten.
	 * @return
	 */
	private boolean isKitten() {
		return pet.getBabyNpcId() == 1555 || pet.getBabyNpcId() == 1556 || pet.getBabyNpcId() == 1557
				|| pet.getBabyNpcId() == 1558 || pet.getBabyNpcId() == 1559 || pet.getBabyNpcId() == 1560
				|| pet.getBabyNpcId() == 7583 || pet.getBabyNpcId() == 14089;
	}

	/**
	 * Unlocks the orb.
	 */
	public void unlockOrb() {
		owner.getPackets().sendHideIComponent(747, 9, false);
		Familiar.sendLeftClickOption(owner);
	}

	/**
	 * Unlocks the interfaces.
	 */
	public void unlock() {
		owner.getPackets().sendHideIComponent(747, 9, false);
	}

	/**
	 * Locks the orb.
	 */
	public void lockOrb() {
		owner.getPackets().sendHideIComponent(747, 9, true);
	}

	/**
	 * Sets the chasing target.
	 * @param chasing
	 */
	public void setChasing(NPC chasing) {
		this.chasing = chasing;
	}

	/**
	 * Gets the chasing target.
	 * @return
	 */
	public NPC getChasing() {
		return chasing;
	}

	/**
	 * Gets the details.
	 * @return The details.
	 */
	public PetDetails getDetails() {
		return details;
	}

	/**
	 * Gets the growthRate.
	 * @return The growthRate.
	 */
	public double getGrowthRate() {
		return growthRate;
	}

	/**
	 * Sets the growthRate.
	 * @param growthRate The growthRate to set.
	 */
	public void setGrowthRate(double growthRate) {
		this.growthRate = growthRate;
	}

	/**
	 * Gets the item id of the pet.
	 * @return The item id.
	 */
	public int getItemId() {
		return itemId;
	}

	public boolean isOwner(Player player) {
		return this.owner == player;
	}

}