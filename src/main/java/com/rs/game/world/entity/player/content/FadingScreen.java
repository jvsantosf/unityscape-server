package com.rs.game.world.entity.player.content;

import java.util.TimerTask;

import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Logger;
import com.rs.utility.Utils;

public final class FadingScreen {

	private FadingScreen() {
		
	}
	
	public static void fade(final Player player, final Runnable event) {
		unfade(player, fade(player), event);
	}
	
	public static void unfade(final Player player, long startTime, final Runnable event) {
		long leftTime =  2500 - (Utils.currentTimeMillis() - startTime);
		if(leftTime > 0) {
			CoresManager.fastExecutor.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						unfade(player, event);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
				
			}, leftTime);
		}else
			unfade(player, event);
	}
	
	
	public static void unfade(final Player player, Runnable event) {
		event.run();
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.getInterfaceManager().sendFadingInterface(170);
				CoresManager.fastExecutor.schedule(new TimerTask() {
					@Override
					public void run() {
						try {
							player.getInterfaceManager().closeFadingInterface();
						} catch (Throwable e) {
							Logger.handle(e);
						}
					}
				}, 2000);
			}
			
		});
	}
	
	public static long fade(Player player) {
		player.getInterfaceManager().sendFadingInterface(115);
		return Utils.currentTimeMillis();
	}
	
}
