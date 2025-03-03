package com.rs.game.world.entity.player.content;

import java.util.TimerTask;

import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Region;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;


import java.util.List;

public class WildernessObelisk {

	/**
	 * Credits 2 Mx II src by savions
	 *
	 * @author dennis
	 * @leecher phillip
	 */

	private static final Position[] OBELISK_CENTER_TILES = { new Position(2978, 3864, 0),
			new Position(3033, 3730, 0), new Position(3104, 3792, 0), new Position(3154, 3618, 0),
			new Position(3217, 3654, 0), new Position(3305, 3914, 0) };
	private static final boolean[] IS_ACTIVE = new boolean[6];

	public static void activateObelisk(int id, final Player player) {
		final int index = id - 65616;
		final Position center = OBELISK_CENTER_TILES[index];
		if (IS_ACTIVE[index]) {
			player.getPackets().sendGameMessage("The obelisk is already active.");
			return;
		}
		IS_ACTIVE[index] = true;
		WorldObject object = World.getObjectWithId(center, id);
		if (object == null) // still loading objects i guess
			return;
		World.sendObjectAnimation(object, new Animation(2226));
		World.sendObjectAnimation(World.getObjectWithId(center.transform(4, 0, 0), id), new Animation(2226));
		World.sendObjectAnimation(World.getObjectWithId(center.transform(0, 4, 0), id), new Animation(2226));
		World.sendObjectAnimation(World.getObjectWithId(center.transform(4, 4, 0), id), new Animation(2226));
		World.spawnObjectTemporary(
				new WorldObject(65626, 10, 0, center.transform(2, 2, 0).getX(), center.transform(2, 2, 0).getY(), 0),
				8000);
		// TODO Add center piece for 8,000 seconds in center tile
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				for (int x = 1; x < 4; x++)
					for (int y = 1; y < 4; y++)
						World.sendGraphics(player, new Graphics(661), center.transform(x, y, 0));
				Region region = World.getRegion(center.getRegionId());
				List<Integer> playerIndexes = region.getPlayerIndexes();
				Position newCenter = OBELISK_CENTER_TILES[Utils.random(OBELISK_CENTER_TILES.length)];
				if (playerIndexes != null) {
					for (Integer i : playerIndexes) {
						Player p = World.getPlayers().get(i);
						if (p == null || (p.getX() < center.getX() + 1 || p.getX() > center.getX() + 3
								|| p.getY() < center.getY() + 1 || p.getY() > center.getY() + 3))
							continue;
						int offsetX = p.getX() - center.getX();
						int offsetY = p.getY() - center.getY();
						Magic.sendTeleportSpell(p, 8939, 8941, 1690, -1, 0, 0,
								new Position(newCenter.getX() + offsetX, newCenter.getY() + offsetY, 0), 3, false,
								Magic.OBJECT_TELEPORT, false);
					}
				}
				IS_ACTIVE[index] = false;
			}

		}, 9);

	}
}
