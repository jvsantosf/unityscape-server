package com.rs.game.world.entity.player.actions.objects;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;

/*
 * @Author Adam
 * Mos Le'Harmless
 */

public class MosLeHarmless {
	
	
	public static void HandleCave(Player player,
			final WorldObject object) {
		if (object.getId() == 15767){//entrance 1
			player.setNextPosition(new Position(3748, 9373, 0));
		}
		if (object.getId() == 15812){//exit 1
			player.setNextPosition(new Position(3749, 2973, 0));
		}
		if (object.getId() == 15790){
			if (object.getX() == 3814 && object.getY() == 9462) {
				
				player.setNextPosition(new Position(3816, 3062, 0));
			}
			if (object.getX() == 3829 && object.getY() == 9462) {
				
				player.setNextPosition(new Position(3831, 3062, 0));
			}
		}
		if (object.getId() == 15791){
			if (object.getX() == 3829 && object.getY() == 3062) {
				
				player.setNextPosition(new Position(3830, 9461, 0));
			}
			if (object.getX() == 3814 && object.getY() == 3062) {
				
				player.setNextPosition(new Position(3815, 9461, 0));
			}
		}
		
	}
	
	
	
	

	public static boolean isObject(final WorldObject object) {
		switch (object.getId()) {
		case 15767:
		case 15812:
		case 15790:
		case 15791:
		return true;
		default:
		return false;
		}
	}
	
	public static void HandleObject(Player player, final WorldObject object) {
		final int id = object.getId();
		if (id == 15767 || id == 15812 || id == 15790 || id == 15791) { 
			MosLeHarmless.HandleCave(player, object);
		}
		
		
	}

}