package com.rs.game.world.entity.player.actions.skilling;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.Region;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.activities.dueling.DuelArena;
import com.rs.game.world.entity.player.content.activities.dueling.DuelControler;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.network.decoders.handlers.InventoryOptionsHandler;
import com.rs.utility.Utils;

public class Firemaking extends Action {

	public static enum Fire {
		NORMAL(1511, 1, 300, 70755, 40, 20), ACHEY(2862, 1, 300, 70756, 40, 1), OAK(1521, 15, 450, 70757, 60, 1), WILLOW(1519, 30, 450, 70758, 90, 1), TEAK(6333, 35, 450, 70759, 105, 1), ARCTIC_PINE(10810, 42, 500, 70760, 125, 1), MAPLE(1517, 45, 500, 70761, 135, 1), MAHOGANY(6332, 50, 700, 70762, 157.5, 1), EUCALYPTUS(12581, 58, 700, 70763, 193.5, 1), YEW(1515, 60, 800, 70764, 202.5, 1), MAGIC(1513, 75, 900, 70765, 303.8, 1), CURSED_MAGIC(13567, 82, 1000, 70766, 303.8, 1);

		private int logId;
		private int level;
		private int life;
		private int fireId;
		private int time;
		private double xp;

		Fire(int logId, int level, int life, int fireId, double xp, int time) {
			this.logId = logId;
			this.level = level;
			this.life = life;
			this.fireId = fireId;
			this.xp = xp;
			this.time = time;
		}

		public int getLogId() {
			return logId;
		}

		public int getLevel() {
			return level;
		}

		public int getLife() {
			return life * 600;
		}

		public int getFireId() {
			return fireId;
		}

		public double getExperience() {
			return xp;
		}

		public int getTime() {
			return time;
		}
	}

	private Fire fire;

	public Firemaking(Fire fire) {
		this.fire = fire;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player, fire, false)) {
			return false;
		}
		player.getPackets().sendGameMessage("You attempt to light the logs.", true);
		player.getInventory().deleteItem(fire.getLogId(), 1);
		World.addGroundItem(new Item(fire.getLogId(), 1), new Position(player), player, true, 180);
		Long time = (Long) player.getTemporaryAttributtes().remove("Fire");
		boolean quickFire = time != null && time > Utils.currentTimeMillis();
		setActionDelay(player, quickFire ? 1 : 2);
		if (!quickFire) {
			player.animate(new Animation(16700));
		}
		return true;
	}

	public static boolean isFiremaking(Player player, Item item1, Item item2) {
		Item log = InventoryOptionsHandler.contains(590, item1, item2);
		if (log == null) {
			return false;
		}
		return isFiremaking(player, log.getId());
	}

	public static boolean isFiremaking(Player player, int logId) {
		for (Fire fire : Fire.values()) {
			if (fire.getLogId() == logId) {
				player.getActionManager().setAction(new Firemaking(fire));
				return true;
			}
		}
		return false;
	}

	public static boolean checkAll(Player player, Fire fire, boolean usingPyre) {
		if (!usingPyre) {
			if (!player.getInventory().containsItemToolBelt(590)) {
				player.getPackets().sendGameMessage("You do not have the required items to light this.");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.FIREMAKING) < fire.getLevel()) {
			player.getPackets().sendGameMessage("You do not have the required level to light this.");
			return false;
		}
		if (!World.canMoveNPC(player.getZ(), player.getX(), player.getY(),
				1) // cliped
				|| World.getObjectWithSlot(usingPyre ? player.getFamiliar() : player, Region.OBJECT_SLOT_FLOOR) != null
				|| player.getControlerManager().getControler() instanceof DuelArena || player.getControlerManager().getControler() instanceof DuelControler) { // contains
			// object
			player.getPackets().sendGameMessage("You can't light a fire here.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player, fire, false);
	}

	public static double increasedExperience(Player player, double totalXp) {
		if (player.getEquipment().getGlovesId() == 13660) {
			totalXp *= 1.025;
		}
		if (player.getEquipment().getRingId() == 13659) {
			totalXp *= 1.025;
		}
		return totalXp;
	}

	@Override
	public int processWithDelay(final Player player) {
		final Position tile = new Position(player);
		if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1)) {
			if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1)) {
				if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1)) {
					player.addWalkSteps(player.getX(), player.getY() - 1, 1);
				}
			}
		}
		player.getPackets().sendGameMessage("The fire catches and the logs begin to burn.", true);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				final FloorItem item = World.getRegion(tile.getRegionId()).getGroundItem(fire.getLogId(), tile, player);
				if (item == null) {
					return;
				}
				if (!World.removeGroundItem(player, item, false)) {
					return;
				}
				player.burntLogs++;
				player.fireslit++;
				World.spawnTempGroundObject(new WorldObject(fire.getFireId(), 10, 0, tile.getX(), tile.getY(), tile.getZ()), 592, fire.getLife());
				if (fire.getLife() == 0 && item.getId() == 592) {
					World.deleteObject(new Position(tile.getX(), tile.getY(), tile.getZ()));
				}
				player.getSkills().addXp(Skills.FIREMAKING, increasedExperience(player, fire.getExperience()));
				player.setNextFacePosition(tile);
			}
		}, 1);
		player.getTemporaryAttributtes().put("Fire", Utils.currentTimeMillis() + 1800);
		return -1;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

	public static Fire getFire(int logId) {
		for (Fire fire : Fire.values()) {
			if (fire.getLogId() == logId) {
				return fire;
			}
		}
		return null;
	}
}
