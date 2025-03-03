package com.rs.game.world.entity.player.actions;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class Listen extends Action {

	private static int[][] LISTEN_DEFS = { { 5713, 1549, 5748 },
			{ 11786, 1550, 11788 }, { 5713, 1551, 2921 } // TODO First emote
	};

	private int index;

	@Override
	public boolean start(Player player) {
		if (!process(player))
			return false;
		index = Utils.random(LISTEN_DEFS.length);
		player.setListening(true);
		player.animate(new Animation(LISTEN_DEFS[index][0]));
		player.getAppearence().setRenderEmote(LISTEN_DEFS[index][1]);
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (player.getToxin().poisoned()) {
			player.getPackets().sendGameMessage(
					"You don't feel like listening to a musician while you're poisoned.");
			return false;
		}
		if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"You can't listen to a musician until 10 seconds after the end of combat.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		return 0;
	}

	@Override
	public void stop(Player player) {
		player.setListening(false);
		player.animate(new Animation(LISTEN_DEFS[index][2]));
		player.getEmotesManager().setNextEmoteEnd();
		player.getAppearence().setRenderEmote(-1);
	}

}
