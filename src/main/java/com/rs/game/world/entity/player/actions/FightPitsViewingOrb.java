package com.rs.game.world.entity.player.actions;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;

public class FightPitsViewingOrb extends Action {

	public static final Position[] ORB_TELEPORTS = { new Position(4571, 5092, 0),
			new Position(4571, 5107, 0), new Position(4590, 5092, 0),
			new Position(4571, 5077, 0),
			new Position(4557, 5092, 0) };
	
	private Position tile;

	@Override
	public boolean start(Player player) {
		if (!process(player))
			return false;
		tile = new Position(player);
		player.getAppearence().switchHidden();
		player.getPackets().sendBlackOut(5);
		player.setNextPosition(ORB_TELEPORTS[0]);
		player.getInterfaceManager().sendInventoryInterface(374);
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (player.getToxin().poisoned()) {
			player.getPackets().sendGameMessage(
					"You can't use orb while you're poisoned.");
			return false;
		}
		if (player.getFamiliar() != null) {
			player.getPackets().sendGameMessage("You can't use orb with a familiar.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		return 0;
	}

	@Override
	public void stop(final Player player) {
		player.lock(2);
		player.getInterfaceManager().closeInventoryInterface();
		player.getAppearence().switchHidden();
		player.getPackets().sendBlackOut(0);
		player.setNextPosition(tile);
	}

}
