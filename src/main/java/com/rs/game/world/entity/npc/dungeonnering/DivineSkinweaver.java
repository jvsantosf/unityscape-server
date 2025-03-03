package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonConstants;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public final class DivineSkinweaver extends DungeonBoss {

	private static final int[] SKELETONS =
	{ 10630, 10680, 10693 };

	private static final int[][] HOLES =
	{
	{ 0, 10 },
	{ 5, 15 },
	{ 11, 15 },
	{ 15, 10 },
	{ 15, 5 } };

	private static final String[] CLOSE_HOLE_MESSAGES =
	{ "Ride the wind and smite the tunnel.", "We have little time, tear down the tunnel.", "Churra! Bring down the tunnel while you can." };

	private final boolean[] holeClosed;
	private int count;
	private boolean requestedClose;
	private int healDelay;
	private int respawnDelay;
	private final List<DungeonSkeletonBoss> skeletons;
	private int killedCount;

	public DivineSkinweaver(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		holeClosed = new boolean[5];
		skeletons = Collections.synchronizedList(new ArrayList<DungeonSkeletonBoss>());
	}

	public void removeSkeleton(DungeonSkeletonBoss skeleton) {
		skeletons.remove(skeleton);
		if (!requestedClose && count < holeClosed.length) {
			killedCount++;
			if (killedCount == 3) {
				requestedClose = true;
				killedCount = 0;
				this.setNextForceTalk(new ForceTalk(CLOSE_HOLE_MESSAGES[com.rs.utility.Utils.random(CLOSE_HOLE_MESSAGES.length)]));
				for (Player p2 : getManager().getParty().getTeam()) {
					if (!getManager().isAtBossRoom(p2))
						continue;
					p2.getPackets().sendGameMessage("Divine skinweaver: <col=99CC66>" + getNextForceTalk().getText());
				}
			}
		}
	}

	private int[] getOpenHole() {
		List<int[]> holes = new ArrayList<int[]>();
		for (int[] hole : HOLES) {
			WorldObject object = getManager().getObjectWithType(getReference(), 49289, 0, hole[0], hole[1]);
			if (object != null && object.getId() != 49289)
				holes.add(new int[]
				{ object.getX() + Utils.ROTATION_DIR_X[object.getRotation()], object.getY() + Utils.ROTATION_DIR_Y[object.getRotation()] });
		}
		if (holes.size() == 0)
			return null;
		return holes.get(com.rs.utility.Utils.random(holes.size()));
	}

	@Override
	public void processNPC() {
		ArrayList<Entity> targets = getPossibleTargets();
		if (respawnDelay > 0) {
			respawnDelay--;
		} else if (count < holeClosed.length && targets.size() != 0 && skeletons.size() < 5) { //blablala spawn skeletons
			int[] coords = getOpenHole();
			if (coords != null) {
				skeletons.add((DungeonSkeletonBoss) getManager().spawnNPC(SKELETONS[com.rs.utility.Utils.random(SKELETONS.length)], 0, new Position(coords[0], coords[1], 0), getReference(), DungeonConstants.BOSS_NPC, 0.4D));
				respawnDelay = 10;
			}
		}
		if (healDelay > 0) {
			healDelay--;
			return;
		}
		Entity healTarget = null;
		for (Entity target : targets) {
			if (target.getHitpoints() >= target.getMaxHitpoints())
				continue;
			if (healTarget == null || com.rs.utility.Utils.getDistance(this, healTarget) > com.rs.utility.Utils.getDistance(this, target))
				healTarget = target;
		}
		if (healTarget == null)
			return;
		int distance = (4 - com.rs.utility.Utils.getDistance(this, healTarget));
		if (distance == 4 || distance < 0)
			return;
		int maxHeal = (int) (healTarget.getMaxHitpoints() * 0.25);
		
		healTarget.heal((distance + 1) * maxHeal / 4, 60);
		animate(new Animation(13678)); //TODO find real one plus gfxs
		setNextGraphics(new Graphics(2445));
		healTarget.setNextGraphics(new Graphics(2443, 60, 0));
		faceEntity(healTarget);
		healDelay = 4;
	}

	public void talkTo(Player player) {
		if (count < holeClosed.length || skeletons.size() > 0) {
			player.getDialogueManager().startDialogue("SimpleNPCMessage", getId(), "Chat later and kill the skeletons now, brah.");
			return;
		}
		if (killedCount == Integer.MAX_VALUE)
			return;
		setNextForceTalk(new ForceTalk("I see little danger in this room so move on to the next with my thanks."));
		for (Player p2 : getManager().getParty().getTeam()) {
			if (!getManager().isAtBossRoom(p2))
				continue;
			p2.getPackets().sendGameMessage("Divine skinweaver: <col=99CC66>" + getNextForceTalk().getText());
		}
		getManager().openStairs(getReference());
		drop();
		killedCount = Integer.MAX_VALUE;
	}

	public void blockHole(Player player, WorldObject object) {
		if (count >= holeClosed.length)
			return;
		player.animate(new Animation(833));
		if (!requestedClose) {
			player.getPackets().sendGameMessage("The portal is fully powered and shocks you with a large burst of energy.");
			player.applyHit(new Hit(player, (int) (player.getMaxHitpoints() * 0.2), Hit.HitLook.REGULAR_DAMAGE));
			return;
		}
		holeClosed[count++] = true;
		requestedClose = false;
		WorldObject closedHole = new WorldObject(object);
		closedHole.setId(49289);
		World.spawnObject(closedHole);

	}
}
