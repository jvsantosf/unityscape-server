package com.rs.game.world.entity.player.content.activities.dueling;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.Controller;

public class DuelControler extends Controller {

	@Override
	public void start() {
		sendInterfaces();
		player.getPackets().sendPlayerOption("Challenge", 1, false);
		moved();
	}

	@Override
	public boolean login() {
		start();
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public void forceClose() {
		remove();
	}

	@Override
	public boolean processMagicTeleport(Position toTile) {
		return true;
	}

	@Override
	public boolean processItemTeleport(Position toTile) {
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		removeControler();
		remove();
	}

	@Override
	public void moved() {
		if (!isAtDuelArena(player)) {
			removeControler();
			remove();
		}
	}

	@Override
	public boolean canPlayerOption1(final Player target) {
		player.stopAll();
		if (target.getInterfaceManager().containsScreenInter() || target.isLocked()) {
			player.getPackets().sendGameMessage("The other player is busy.");
			return false;
		}
		if (target.getTemporaryAttributtes().get("DuelChallenged") == player) {
			player.getControlerManager().removeControlerWithoutCheck();
			target.getControlerManager().removeControlerWithoutCheck();
			target.getTemporaryAttributtes().remove("DuelChallenged");
			player.setLastDuelRules(new DuelRules(player, target));
			target.setLastDuelRules(new DuelRules(target, player));
			player.getControlerManager().startControler("DuelArena", target,
					target.getTemporaryAttributtes().get("DuelFriendly"));
			target.getControlerManager().startControler("DuelArena", player,
					target.getTemporaryAttributtes().remove("DuelFriendly"));
			return false;
		}
		player.getTemporaryAttributtes().put("DuelTarget", target);
		player.getInterfaceManager().sendInterface(640);
		player.getTemporaryAttributtes().put("WillDuelFriendly", true);
		player.getVarsManager().sendVar(283, 67108864);
		return false;
	}

	public static void challenge(Player player) {
		player.closeInterfaces();
		Boolean friendly = (Boolean) player.getTemporaryAttributtes().remove("WillDuelFriendly");
		if (friendly == null)
			return;
		Player target = (Player) player.getTemporaryAttributtes().remove("DuelTarget");
		if (target == null || target.isFinished() || !target.withinDistance(player, 14)
				|| !(target.getControlerManager().getControler() instanceof DuelControler)) {
			player.getPackets()
					.sendGameMessage("Unable to find " + (target == null ? "your target" : target.getDisplayName()));
			return;
			// }if ((player.isIronMan && !target.isIronMan) || (!player.isIronMan &&
			// target.isIronMan)) {
			// player.sm("A isIronMan cannot duel with a normal player.");
			// return;
		}
		player.getTemporaryAttributtes().put("DuelChallenged", target);
		player.getTemporaryAttributtes().put("DuelFriendly", friendly);
		player.getPackets().sendGameMessage("Sending " + target.getDisplayName() + " a request...");
		target.getPackets().sendDuelChallengeRequestMessage(player, friendly);
	}

	public void remove() {
		player.getInterfaceManager().closeOverlay(false);
		player.getPackets().sendPlayerOption("null", 1, false);
	}

	@Override
	public void sendInterfaces() {
		if (isAtDuelArena(player))
			player.getInterfaceManager().sendOverlay(638, false);
	}

	public static boolean isAtDuelArena(Player player) {
		return player.inArea(3341, 3265, 3387, 3281);
	}
}
