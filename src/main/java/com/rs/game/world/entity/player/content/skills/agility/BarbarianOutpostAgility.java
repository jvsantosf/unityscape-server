package com.rs.game.world.entity.player.content.skills.agility;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceMovement;

public class BarbarianOutpostAgility {

	public static void enterObstaclePipe(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 35)) {
			return;
		}
		player.lock(4);
		player.animate(new Animation(10580));
		final Position toTile = new Position(object.getX(), player.getY() >= 3561 ? 3558 : 3561, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 2, player.getY() >= 3561 ? ForceMovement.SOUTH : ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextPosition(toTile);
				player.getSkills().addXp(Skills.AGILITY, 1 / 20);
			}

		}, 1);
	}

	public static void runUpWall(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 90)) {
			return;
		}
		player.lock(10);
		final Position toTile = new Position(2538, 3545, 2);
		WorldTasksManager.schedule(new WorldTask() {

			boolean secondLoop;

			@Override
			public void run() {

				if (!secondLoop) {
					player.setNextForceMovement(new ForceMovement(player, 7, toTile, 8, ForceMovement.NORTH));
					player.animate(new Animation(10492));
					secondLoop = true;
				} else {
					player.animate(new Animation(10493));
					player.setNextPosition(toTile);
					player.getSkills().addXp(Skills.AGILITY, 15);
					stop();
				}

			}

		}, 1, 6);
	}

	public static void climbUpWall(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 90)) {
			return;
		}
		player.useStairs(10023, new Position(2536, 3546, 3), 2, 3);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.animate(new Animation(11794));
				player.getSkills().addXp(Skills.AGILITY, 15);
			}

		}, 1);
	}

	public static void fireSpringDevice(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 90)) {
			return;
		}
		player.lock(5);
		player.addWalkSteps(2533, 3547, -1, false);
		final Position toTile = new Position(2532, 3553, 3);
		WorldTasksManager.schedule(new WorldTask() {

			boolean secondLoop;

			@Override
			public void run() {
				if (!secondLoop) {
					player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.NORTH));
					player.animate(new Animation(4189));
					World.sendObjectAnimation(player, object, new Animation(11819));

					secondLoop = true;
				} else {
					player.setNextPosition(toTile);
					player.getSkills().addXp(Skills.AGILITY, 15);
					stop();
				}
			}

		}, 1, 1);
	}

	public static void crossBalanceBeam(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 90)) {
			return;
		}
		player.lock(4);
		final Position toTile = new Position(2536, 3553, 3);
		player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.EAST));
		player.animate(new Animation(16079));
		player.getAppearence().setRenderEmote(330);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextPosition(toTile);
				player.getSkills().addXp(Skills.AGILITY, 15);
				player.animate(new Animation(-1));
				stop();
			}

		}, 2);
	}

	public static void jumpOverGap(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 90)) {
			return;
		}
		player.lock(1);
		player.animate(new Animation(2586));
		player.getAppearence().setRenderEmote(-1);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextPosition(new Position(2538, 3553, 2));
				player.animate(new Animation(2588));
				player.getSkills().addXp(Skills.AGILITY, 15);
				stop();
			}

		}, 0);
	}

	public static void slideDownRoof(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 90)) {
			return;
		}
		player.lock(6);
		player.animate(new Animation(11792));
		final Position toTile = new Position(2544, player.getY(), 0);
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 5, ForceMovement.EAST));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 0) {
					player.setNextPosition(new Position(2541, player.getY(), 1));
					player.animate(new Animation(11790));
					stage = 1;
				} else if (stage == 1) {
					stage = 2;
				} else if (stage == 2) {
					player.animate(new Animation(11791));
					stage = 3;
				} else if (stage == 3) {
					player.setNextPosition(toTile);
					player.animate(new Animation(2588));
					player.getSkills().addXp(Skills.AGILITY, 15);
					if (getStage(player) == 1) {
						removeStage(player);
						player.getSkills().addXp(Skills.AGILITY, 615);
						player.advancedagilitylaps++;
						player.barbariancourse++;
						player.agilityLapsD++;
						if (player.agilityLapsD == 100) {
							player.getInventory().addItem(14936, 1);
							World.sendWorldMessage("<col=ff8c38><img=4>News: "+ player.getDisplayName() + " has just earned Agile Top!</col> ", false);
						}
					}
					stop();
				}
			}

		}, 0, 0);
	}

	public static void swingOnRopeSwing(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 35)) {
			return;
		}
		if (player.getY() != 3554) {
			player.getPackets().sendGameMessage("You'll need to get closer to make this jump.");
			return;
		}
		player.lock(4);
		player.animate(new Animation(751));
		World.sendObjectAnimation(player, object, new Animation(497));
		final Position toTile = new Position(object.getX(), 3549, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 3, ForceMovement.SOUTH));
		player.getSkills().addXp(Skills.AGILITY, 22);
		player.getPackets().sendGameMessage("You skilfully swing across.", true);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextPosition(toTile);
				setStage(player, 0);
			}

		}, 1);
	}

	public static void walkAcrossLogBalance(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 35)) {
			return;
		}
		if (player.getY() != object.getY()) {
			player.addWalkSteps(2551, 3546, -1, false);
			player.lock(2);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					walkAcrossLogBalanceEnd(player, object);
				}
			}, 1);
		} else {
			walkAcrossLogBalanceEnd(player, object);
		}
	}

	private static void walkAcrossLogBalanceEnd(final Player player, WorldObject object) {
		player.getPackets().sendGameMessage("You walk carefully across the slippery log...", true);
		player.lock(17);
		player.animate(new Animation(9908));
		final Position toTile = new Position(2541, object.getY(), object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 16, ForceMovement.WEST));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextPosition(toTile);
				player.getSkills().addXp(Skills.AGILITY, 13);
				player.getPackets().sendGameMessage("... and make it safely to the other side.", true);
				if (getStage(player) == 0) {
					setStage(player, 1);
				}
			}

		}, 15);
	}

	public static void walkAcrossBalancingLedge(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 35)) {
			return;
		}
		player.getPackets().sendGameMessage("You put your food on the ledge and try to edge across...", true);
		player.lock(5);
		player.animate(new Animation(753));
		player.getAppearence().setRenderEmote(157);
		final Position toTile = new Position(2532, object.getY(), object.getZ());
		player.setRun(true);
		player.addWalkSteps(toTile.getX(), toTile.getY(), -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.animate(new Animation(759));
				player.getAppearence().setRenderEmote(-1);
				player.getSkills().addXp(Skills.AGILITY, 22);
				player.getPackets().sendGameMessage("You skilfully edge across the gap.", true);
				if (getStage(player) == 2) {
					setStage(player, 3);
				}
			}
		}, 3);
	}

	public static void climbObstacleNet(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 35) || player.getY() < 3545 || player.getY() > 3547) {
			return;
		}
		player.getPackets().sendGameMessage("You climb the netting...", true);
		player.getSkills().addXp(Skills.AGILITY, 8.2);
		player.useStairs(828, new Position(object.getX() - 1, player.getY(), 1), 1, 2);
		if (getStage(player) == 1) {
			setStage(player, 2);
		}
	}

	public static void climbOverCrumblingWall(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 35)) {
			return;
		}
		if (player.getX() >= object.getX()) {
			player.getPackets().sendGameMessage("You cannot climb that from this side.");
			return;
		}
		player.getPackets().sendGameMessage("You climb the low wall...", true);
		player.lock(3);
		player.animate(new Animation(4853));
		final Position toTile = new Position(object.getX() + 1, object.getY(), object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 2, ForceMovement.EAST));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextPosition(toTile);
				player.getSkills().addXp(Skills.AGILITY, 13.7);
				int stage = getStage(player);
				if (stage == 3) {
					setStage(player, 4);
				} else if (stage == 4) {
					removeStage(player);
					player.getSkills().addXp(Skills.AGILITY, 46.3);
				}
			}

		}, 1);
	}

	public static void removeStage(Player player) {
		player.getTemporaryAttributtes().remove("BarbarianOutpostCourse");
	}

	public static void setStage(Player player, int stage) {
		player.getTemporaryAttributtes().put("BarbarianOutpostCourse", stage);
	}

	public static int getStage(Player player) {
		Integer stage = (Integer) player.getTemporaryAttributtes().get("BarbarianOutpostCourse");
		if (stage == null) {
			return -1;
		}
		return stage;
	}
}
