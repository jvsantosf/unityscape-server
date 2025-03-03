package com.rs.game.world.entity.player.controller.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.Controller;

public class Kalaboss extends Controller {

	private boolean showingOption;

	@Override
	public void start() {
		setInviteOption(true);
	}

	@SuppressWarnings("unused")
	@Override
	public boolean canPlayerOption1(Player target) {
		if (true) {
			return true;
		}
		player.setNextFacePosition(target);
		player.getPackets().sendGameMessage("You can't do that right now.");
		return false;
	}

	@Override
	public boolean login() {
		moved();
		return false;
	}

	@Override
	public boolean sendDeath() {
		setInviteOption(false);
		removeControler();
		return true;
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

	@Override
	public void forceClose() {
		setInviteOption(false);
	}

	@Override
	public void moved() {
		if (player.getX() == 3385 && player.getY() == 3615) {
			setInviteOption(false);
			removeControler();
			player.getControlerManager().startControler("Wilderness");
		} else {
			if (!isAtKalaboss(player)) {
				setInviteOption(false);
				removeControler();
			} else
				setInviteOption(true);
		}
	}

	public static boolean isAtKalaboss(Position tile) {
		return tile.getX() >= 3385 && tile.getX() <= 3513
				&& tile.getY() >= 3605 && tile.getY() <= 3794;
	}

	public void setInviteOption(boolean show) {
		if (show == showingOption)
			return;
		showingOption = show;
		player.getPackets()
				.sendPlayerOption(show ? "Invite" : "null", 1, false);
	}
}
