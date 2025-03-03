package com.rs.game.world.entity.player.content.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.godwars.zaros.Nex;
import com.rs.game.world.entity.npc.godwars.zaros.NexMinion;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public final class ZarosGodwars {

	private static final List<Player> playersOn = Collections
			.synchronizedList(new ArrayList<Player>());
	// private static final Object LOCK = new Object();

	public static Nex nex;
	public static NexMinion fumus;
	public static NexMinion umbra;
	public static NexMinion cruor;
	public static NexMinion glacies;

	public static int getPlayersCount() {
		return playersOn.size();
	}

	public static void breakFumusBarrier() {
		// synchronized(LOCK) {
		if (fumus == null)
			return;
		fumus.breakBarrier();
		// }
	}

	public static void breakUmbraBarrier() {
		// synchronized(LOCK) {
		if (umbra == null)
			return;
		umbra.breakBarrier();
		// }
	}

	public static void breakCruorBarrier() {
		// synchronized(LOCK) {
		if (cruor == null)
			return;
		cruor.breakBarrier();
		// }
	}

	public static void breakGlaciesBarrier() {
		// synchronized(LOCK) {
		if (glacies == null)
			return;
		glacies.breakBarrier();
		// }
	}

	public static void addPlayer(Player player) {
		// synchronized(LOCK) {
		if (playersOn.contains(player)) {
			// System.out.println("ERROR DOUBLE ENTRY!");
			return;
		}
		playersOn.add(player);
		startWar();
		// }
	}

	public static void removePlayer(Player player) {
		// synchronized(LOCK) {
		playersOn.remove(player);
		cancelWar();
		// }
	}

	public static void deleteNPCS() {
		if (nex != null) {
			nex.killBloodReavers();
			nex.finish();
			nex = null;
		}
		if (fumus != null) {
			fumus.finish();
			fumus = null;
		}
		if (umbra != null) {
			umbra.finish();
			umbra = null;
		}
		if (cruor != null) {
			cruor.finish();
			cruor = null;
		}
		if (glacies != null) {
			glacies.finish();
			glacies = null;
		}
	}

	private static void cancelWar() {
		if (getPlayersCount() == 0)
			deleteNPCS();
	}

	public static ArrayList<Entity> getPossibleTargets() {
		// synchronized(LOCK) {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>(
				playersOn.size());
		for (Player player : playersOn) {
			if (player == null || player.isDead() || player.hasFinished()
					|| !player.isRunning())
				continue;
			possibleTarget.add(player);
			/*
			 * if (player.getFamiliar() != null &&
			 * player.getFamiliar().isAgressive())
			 * possibleTarget.add(player.getFamiliar());
			 */
		}
		return possibleTarget;
		// }
	}

	public static void moveNextStage() {
		// synchronized(LOCK) {
		if (nex == null)
			return;
		nex.moveNextStage();
		// }
	}

	public static void endWar() {
		// synchronized(LOCK) {
		deleteNPCS();
		CoresManager.slowExecutor.schedule(new Runnable() {

			@Override
			public void run() {
				startWar();
			}

		}, 1, TimeUnit.MINUTES);
		// }
	}

	private static void startWar() {
		if (getPlayersCount() >= 1) {
			if (nex == null) {
				World.spawnNPC(13447, new Position(2924, 5202, 0), -1, 0,
						true);
				WorldTasksManager.schedule(new WorldTask() {
					private int count = 0;

					@Override
					public void run() {
						// synchronized(LOCK) {
						if (nex == null) {
							stop();
							return;
						}
						if (count == 1) {
							nex.setNextForceTalk(new ForceTalk("AT LAST!"));
							nex.animate(new Animation(17412));
							nex.setNextGraphics(new Graphics(1217));
							nex.playSound(3295, 2);
						} else if (count == 3) {
							World.spawnNPC(13451, new Position(2913, 5215, 0),
									-1, 0, true);
							fumus.setDirection(Utils.getFaceDirection(1, -1));
							nex.setNextFacePosition(new Position(fumus
									.getCoordFaceX(fumus.getSize()), fumus
									.getCoordFaceY(fumus.getSize()), 0));
							nex.setNextForceTalk(new ForceTalk("Fumus!"));
							nex.animate(new Animation(17413));
							World.sendProjectile(fumus, nex, 2244, 18, 18, 60,
									30, 0, 0);
							nex.playSound(3325, 2);
						} else if (count == 5) {
							World.spawnNPC(13452, new Position(2937, 5215, 0),
									-1, 0, true);
							umbra.setDirection(Utils.getFaceDirection(-1, -1));
							nex.setNextFacePosition(new Position(umbra
									.getCoordFaceX(umbra.getSize()), umbra
									.getCoordFaceY(umbra.getSize()), 0));
							nex.setNextForceTalk(new ForceTalk("Umbra!"));
							nex.animate(new Animation(17413));
							World.sendProjectile(umbra, nex, 2244, 18, 18, 60,
									30, 0, 0);
							nex.playSound(3313, 2);
						} else if (count == 7) {
							World.spawnNPC(13453, new Position(2937, 5191, 0),
									-1, 0, true);
							cruor.setDirection(Utils.getFaceDirection(-1, 1));
							nex.setNextFacePosition(new Position(cruor
									.getCoordFaceX(cruor.getSize()), cruor
									.getCoordFaceY(cruor.getSize()), 0));
							nex.setNextForceTalk(new ForceTalk("Cruor!"));
							nex.animate(new Animation(17413));
							World.sendProjectile(cruor, nex, 2244, 18, 18, 60,
									30, 0, 0);
							nex.playSound(3299, 2);
						} else if (count == 9) {
							World.spawnNPC(13454, new Position(2913, 5191, 0),
									-1, 0, true);
							glacies.setNextFacePosition(new Position(glacies
									.getCoordFaceX(glacies.getSize()), glacies
									.getCoordFaceY(glacies.getSize()), 0));
							glacies.setDirection(Utils.getFaceDirection(1, 1));
							nex.setNextFacePosition(new Position(glacies
									.getCoordFaceX(glacies.getSize()), glacies
									.getCoordFaceY(glacies.getSize()), 0));
							nex.setNextForceTalk(new ForceTalk("Glacies!"));
							nex.animate(new Animation(17413));
							World.sendProjectile(glacies, nex, 2244, 18, 18,
									60, 30, 0, 0);
							nex.playSound(3304, 2);
						} else if (count == 11) {
							nex.setNextForceTalk(new ForceTalk(
									"Fill my soul with smoke!"));
							World.sendProjectile(fumus, nex, 2244, 18, 18, 60,
									30, 0, 0);

							nex.playSound(3310, 2);
						} else if (count == 13) {
							nex.setCantInteract(false);
							stop();
							return;
						}
						count++;
					}
					// }
				}, 0, 1);
			}
		}
	}

	private ZarosGodwars() {

	}
}
