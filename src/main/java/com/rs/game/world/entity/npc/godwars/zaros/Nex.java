package com.rs.game.world.entity.npc.godwars.zaros;

import java.util.ArrayList;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.content.activities.ZarosGodwars;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public final class Nex extends NPC {

	private boolean followTarget;
	private int stage;
	private int minionStage;
	private int flyTime;
	private long lastVirus;
	private boolean embracedShadow;
	private boolean trapsSettedUp;
	private long lastSiphon;
	private boolean doingSiphon;
	private NPC[] bloodReavers;
	private int switchPrayersDelay;

	public Nex(int id, Position tile, int mapAreaNameHash,
               boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setCantInteract(true);
		setDamageCap(50);
		setLureDelay(3000);
		bloodReavers = new NPC[3];
	}

	@Override
	public void processNPC() {
		if (flyTime > 0)
			flyTime--;
		if (getStage() == 0 && minionStage == 0 && getHitpoints() <= 2400) {
			setDamageCap(0);
			setNextForceTalk(new ForceTalk("Fumus, don't fail me!"));
			getCombat().addCombatDelay(1);
			ZarosGodwars.breakFumusBarrier();
			playSound(3321, 2);
			minionStage = 1;
		} else if (getStage() == 1 && minionStage == 1
				&& getHitpoints() <= 1800) {
			setDamageCap(0);
			setNextForceTalk(new ForceTalk("Umbra, don't fail me!"));
			getCombat().addCombatDelay(1);
			ZarosGodwars.breakUmbraBarrier();
			playSound(3307, 2);
			minionStage = 2;
		} else if (getStage() == 2 && minionStage == 2
				&& getHitpoints() <= 1200) {
			setDamageCap(0);
			setNextForceTalk(new ForceTalk("Cruor, don't fail me!"));
			getCombat().addCombatDelay(1);
			ZarosGodwars.breakCruorBarrier();
			playSound(3298, 2);
			minionStage = 3;
		} else if (getStage() == 3 && minionStage == 3
				&& getHitpoints() <= 600) {
			setDamageCap(0);
			setNextForceTalk(new ForceTalk("Glacies, don't fail me!"));
			getCombat().addCombatDelay(1);
			ZarosGodwars.breakGlaciesBarrier();
			playSound(3327, 2);
			minionStage = 4;
		} else if (getStage() == 4 && minionStage == 4) {
			if (switchPrayersDelay > 0)
				switchPrayersDelay--;
			else {
				switchPrayers();
				resetSwitchPrayersDelay();
			}
		}
		if (isDead() || doingSiphon || isCantInteract())
			return;
		if (!getCombat().process())
			checkAgressivity();
	}

	@Override
	public void sendDeath(Entity source) {
		transformIntoNPC(13450);
		final NPCCombatDefinitions defs = getCombatDefinitions();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					animate(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					drop();
					finish();
					ZarosGodwars.endWar();
					stop();
				}
				loop++;
			}
		}, 0, 1);
		setNextForceTalk(new ForceTalk("Taste my wrath!"));
		playSound(3323, 2);
		final NPC target = this;
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				setNextGraphics(new Graphics(2259));
				ArrayList<Entity> possibleTargets = getPossibleTargets();
				if (possibleTargets != null) {
					for (Entity entity : possibleTargets) {
						if (entity == null || entity.isDead()
								|| entity.isFinished()
								|| !entity.withinDistance(target, 10))
							continue;
						World.sendProjectile(target, new Position(getX() + 2,
								getY() + 2, getZ()), 2260, 24, 0, 41, 35,
								30, 0);
						World.sendProjectile(target, new Position(getX() + 2,
								getY(), getZ()), 2260, 41, 0, 41, 35, 30, 0);
						World.sendProjectile(target, new Position(getX() + 2,
								getY() - 2, getZ()), 2260, 41, 0, 41, 35,
								30, 0);
						World.sendProjectile(target, new Position(getX() - 2,
								getY() + 2, getZ()), 2260, 41, 0, 41, 35,
								30, 0);
						World.sendProjectile(target, new Position(getX() - 2,
								getY(), getZ()), 2260, 41, 0, 41, 35, 30, 0);
						World.sendProjectile(target, new Position(getX() - 2,
								getY() - 2, getZ()), 2260, 41, 0, 41, 35,
								30, 0);
						World.sendProjectile(target, new Position(getX(),
								getY() + 2, getZ()), 2260, 41, 0, 41, 35,
								30, 0);
						World.sendProjectile(target, new Position(getX(),
								getY() - 2, getZ()), 2260, 41, 0, 41, 35,
								30, 0);
						entity.applyHit(new Hit(target, Utils.getRandom(600),
								HitLook.REGULAR_DAMAGE));
					}
				}
			}
		});
	}

	public ArrayList<Entity> calculatePossibleTargets(Position current,
                                                      Position position, boolean northSouth) {
		ArrayList<Entity> list = new ArrayList<Entity>();
		for (Entity e : getPossibleTargets()) {
			if (e.inArea(current.getX(), current.getY(), position.getX()
					+ (northSouth ? 2 : 0), position.getY()
					+ (!northSouth ? 2 : 0))

					|| e.inArea(position.getX(), position.getY(),
							current.getX() + (northSouth ? 2 : 0),
							current.getY() + (!northSouth ? 2 : 0)))
				list.add(e);
		}
		return list;
	}

	public void moveNextStage() {
		if (getStage() == 0 && minionStage == 1) {
			setDamageCap(50);
			setNextForceTalk(new ForceTalk("Darken my shadow!"));
			World.sendProjectile(ZarosGodwars.umbra, this, 2244, 18, 18, 60,
					30, 0, 0);
			getCombat().addCombatDelay(1);
			setStage(1);
			playSound(3302, 2);
		} else if (getStage() == 1 && minionStage == 2) {
			setDamageCap(50);
			setNextForceTalk(new ForceTalk("Flood my lungs with blood!"));
			World.sendProjectile(ZarosGodwars.cruor, this, 2244, 18, 18, 60,
					30, 0, 0);
			getCombat().addCombatDelay(1);
			setStage(2);
			playSound(3306, 2);
		} else if (getStage() == 2 && minionStage == 3) {
			setDamageCap(50);
			killBloodReavers();
			setNextForceTalk(new ForceTalk("Infuse me with the power of ice!"));
			World.sendProjectile(ZarosGodwars.glacies, this, 2244, 18, 18, 60,
					30, 0, 0);
			getCombat().addCombatDelay(1);
			setStage(3);
			playSound(3303, 2);
		} else if (getStage() == 3 && minionStage == 4) {
			setDamageCap(50);
			setNextForceTalk(new ForceTalk("NOW, THE POWER OF ZAROS!"));
			animate(new Animation(17410));
			setNextGraphics(new Graphics(1204));
			getCombat().addCombatDelay(1);
			heal(600);
			setStage(4);
			playSound(3312, 2);
		}
	}

	public void resetSwitchPrayersDelay() {
		switchPrayersDelay = 35; // 25sec
	}

	public void switchPrayers() {
		transformIntoNPC(getId() == 13449 ? 13447 : getId() + 1);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (doingSiphon)
			hit.setHealHit();
		if (getId() == 13449 && hit.getLook() == HitLook.MELEE_DAMAGE) {
			Entity source = hit.getSource();
			if (source != null) {
				int deflectedDamage = (int) (hit.getDamage() * 0.1);
				hit.setDamage((int) (hit.getDamage() * source
						.getMeleePrayerMultiplier()));
				if (deflectedDamage > 0)
					source.applyHit(new Hit(this, deflectedDamage,
							HitLook.REFLECTED_DAMAGE));
			}
		}
		super.handleIngoingHit(hit);
	}

	@Override
	public void animate(Animation nextAnimation) {
		if (doingSiphon)
			return;
		super.animate(nextAnimation);
	}

	@Override
	public void setNextGraphics(Graphics nextGraphic) {
		if (doingSiphon)
			return;
		super.setNextGraphics(nextGraphic);
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		return ZarosGodwars.getPossibleTargets();
	}

	public boolean isFollowTarget() {
		return followTarget;
	}

	public void setFollowTarget(boolean followTarget) {
		this.followTarget = followTarget;
	}

	public int getAttacksStage() {
		return getStage();
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getFlyTime() {
		return flyTime;
	}

	public void setFlyTime(int flyTime) {
		this.flyTime = flyTime;
	}

	public long getLastVirus() {
		return lastVirus;
	}

	public void sendVirusAttack(ArrayList<Entity> hitedEntitys,
			ArrayList<Entity> possibleTargets, Entity infected) {
		for (Entity t : possibleTargets) {
			if (hitedEntitys.contains(t))
				continue;
			if (Utils.getDistance(t.getX(), t.getY(), infected.getX(),
					infected.getY()) <= 1) {
				t.setNextForceTalk(new ForceTalk("*Cough*"));
				t.applyHit(new Hit(this, Utils.getRandom(100),
						HitLook.REGULAR_DAMAGE));
				hitedEntitys.add(t);
				sendVirusAttack(hitedEntitys, possibleTargets, infected);
			}
		}
		playSound(3296, 2);
	}

	public void setLastVirus(long lastVirus) {
		this.lastVirus = lastVirus;
	}

	public boolean isEmbracedShadow() {
		return embracedShadow;
	}

	public void setEmbracedShadow(boolean embracedShadow) {
		this.embracedShadow = embracedShadow;
	}

	public boolean isTrapsSettedUp() {
		return trapsSettedUp;
	}

	public void setTrapsSettedUp(boolean trapsSettedUp) {
		this.trapsSettedUp = trapsSettedUp;
	}

	public long getLastSiphon() {
		return lastSiphon;
	}

	public void setLastSiphon(long lastSiphon) {
		this.lastSiphon = lastSiphon;
	}

	public NPC[] getBloodReavers() {
		return bloodReavers;
	}

	public void killBloodReavers() {
		for (int index = 0; index < bloodReavers.length; index++) {
			if (bloodReavers[index] == null)
				continue;
			NPC npc = bloodReavers[index];
			bloodReavers[index] = null;
			if (npc.isDead())
				return;
			heal(npc.getHitpoints());
			npc.sendDeath(this);
		}
	}

	public boolean isDoingSiphon() {
		return doingSiphon;
	}

	public void setDoingSiphon(boolean doingSiphon) {
		this.doingSiphon = doingSiphon;
	}

	public int getStage() {
		return stage;
	}
}
