package com.rs.game.world.entity.player.actions.objects;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;

/*
 * @Author Justin
 * Varrock City
 */

public class EdgevilleDungeon {
	
	public static void MonkeyBars(final Player player,
			final WorldObject object) {
		if ((player.getX() != 3120 && player.getY() != 9964)
				|| (player.getX() != 3121 && player.getY() != 9964)
				|| (player.getX() != 3120 && player.getY() != 9969)
				|| (player.getX() != 3121 && player.getY() != 9969))
		    return;
		final boolean running = player.getRun();
		player.setRunHidden(false);
		if (player.getX() == 3120 && player.getY() == 9964) {
			player.lock(5);
			player.addWalkSteps(3120, 9969, -1, false);//where it's going to take you
		} else if (player.getX() == 3121 && player.getY() == 9964) {
			player.lock(5);
			player.addWalkSteps(3121, 9969, -1, false);//where it's going to take you
		} else if (player.getX() == 3120 && player.getY() == 9969) {
			player.lock(5);
			player.addWalkSteps(3120, 9964, -1, false);//where it's going to take you
		} else if (player.getX() == 3121 && player.getY() == 9969) {
			player.lock(5);
			player.addWalkSteps(3121, 9964, -1, false);//where it's going to take you
		}
				
		player.getPackets().sendGameMessage("You Jump across the monkey bars...", true);
		WorldTasksManager.schedule(new WorldTask() {
		    boolean secondloop;

		    @Override
		    public void run() {
			if (!secondloop) {
			    secondloop = true;
			    player.getAppearence().setRenderEmote(744);
			} else {
			    player.getAppearence().setRenderEmote(-1);
			    player.setRunHidden(running);
			    player.getSkills().addXp(Skills.AGILITY, 4);
			    player.getPackets().sendGameMessage("... and make it safely to the other side.", true);
			    stop();
			}
		    }
		}, 0, 6);
	    }
	
	
	

	public static boolean isObject(final WorldObject object) {
		switch (object.getId()) {
		case 29375:
		return true;
		default:
		return false;
		}
	}
	
	public static void HandleObject(Player player, final WorldObject object) {
		final int id = object.getId();
		if (id == 29375) { 
			EdgevilleDungeon.MonkeyBars(player, object); 
		}
	}

}