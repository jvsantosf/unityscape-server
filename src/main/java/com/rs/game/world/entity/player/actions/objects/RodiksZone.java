package com.rs.game.world.entity.player.actions.objects;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;

/*
 * @Author Adam
 * RodiksZone
 */

public class RodiksZone {
	
	
	public static void HandleStairs(Player player,
			final WorldObject object) {
		if (object.getId() == 37211){
			player.setNextPosition(new Position(player.getX(), player.getY(), 1));
		}
	}
	public static void OpenDoor(Player player,
			final WorldObject object) {
		if (player.getY() <= 4691)
			player.setNextPosition(new Position(4704, 4692, 0));
		if (player.getY() >= 4692)
			player.setNextPosition(new Position(4704, 4691, 0));
	}
	
	
	

	public static boolean isObject(final WorldObject object) {
		switch (object.getId()) {
		case 37211:
		case 58847:
		return true;
		default:
		return false;
		}
	}
	
	public static void HandleObject(Player player, final WorldObject object) {
		final int id = object.getId();
		if (id == 37211) { 
			RodiksZone.HandleStairs(player, object);
		}
		if (id == 58847) { 
			RodiksZone.OpenDoor(player, object);
		}
		
	}

}