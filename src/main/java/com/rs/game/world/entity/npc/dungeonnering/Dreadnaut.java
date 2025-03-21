package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("serial")
public class Dreadnaut extends DungeonBoss {

	private List<GassPuddle> puddles;

	private int ticks;
	private boolean reduceMagicLevel;

	public Dreadnaut(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		setForceFollowClose(true);
		setRun(true);
		setLureDelay(6000);//6 seconds
		puddles = new CopyOnWriteArrayList<>();
	}

	@Override
	public void processNPC() {
		if (puddles == null) //still loading
			return;
		super.processNPC();
		if (!reduceMagicLevel) {
			if (isUnderCombat()) {
				for (Entity t : getPossibleTargets()) {
					if (!t.withinDistance(this, 1)) {
						ticks++;
						break;
					}
				}
			}
			if (ticks == 25) {
				reduceMagicLevel = true;
				setNextForceTalk(new ForceTalk("You cannot run from me forever!"));
			}
		}

		for (GassPuddle puddle : puddles) {
			puddle.cycles++;
			if (puddle.canDestroyPuddle()) {
				puddles.remove(puddle);
				continue;
			} else if (puddle.cycles % 2 != 0)
				continue;
			if (puddle.cycles % 2 == 0)
				puddle.refreshGraphics();
			List<Entity> targets = getPossibleTargets(true, true);
			for (Entity t : targets) {
				if (!t.matches(puddle.tile))
					continue;
				t.applyHit(new Hit(this, (int) com.rs.utility.Utils.random((int) (t.getHitpoints() * 0.25)) + 1, Hit.HitLook.REGULAR_DAMAGE));
			}
		}
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.60;
	}

	public boolean canReduceMagicLevel() {
		return reduceMagicLevel;
	}

	public void setReduceMagicLevel(boolean reduceMagicLevel) {
		this.reduceMagicLevel = reduceMagicLevel;
	}

	public void addSpot(Position tile) {
		GassPuddle puddle = new GassPuddle(this, tile);
		puddle.refreshGraphics();
		puddles.add(puddle);
	}

	private static class GassPuddle {
		final Dreadnaut boss;
		final Position tile;
		int cycles;

		public GassPuddle(Dreadnaut boss, Position tile) {
			this.tile = tile;
			this.boss = boss;
		}

		public void refreshGraphics() {
			World.sendGraphics(boss, new Graphics(2859, 0, 10), tile);
		}

		public boolean canDestroyPuddle() {
			return cycles == 50;
		}
	}
}
