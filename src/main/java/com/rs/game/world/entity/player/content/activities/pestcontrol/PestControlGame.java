package com.rs.game.world.entity.player.content.activities.pestcontrol;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.activities.Lander;
import com.rs.game.world.entity.player.content.activities.PestControl;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class PestControlGame extends Controller {

    private PestControl control;
    private double points;

    @Override
    public void start() {
	control = (PestControl) getArguments()[0];
	setArguments(null);
	setPoints(0.0D);
	sendInterfaces();
	player.setForceMultiArea(true);
    }

    @Override
    public void sendInterfaces() {
	updatePestPoints();
	player.getInterfaceManager().sendOverlay(408, false);
    }

    private void updatePestPoints() {
	boolean isGreen = getPoints() > 250;
	player.getPackets().sendIComponentText(408, 11, (isGreen ? "<col=75AE49>" : "") + (int) getPoints() + "</col>");
    }

    @Override
    public void forceClose() {
	if (control != null) {
	    if (control.getPortalCount() != 0) {
		if (control.getPlayers().contains(player))
		    control.getPlayers().remove(player);
	    }
	    player.useStairs(-1, Lander.getLanders()[control.getPestData().ordinal()].getLanderRequierment().getExitTile(), 1, 2);
	} else
	    player.useStairs(-1, new Position(2657, 2639, 0), 1, 2);
	player.setForceMultiArea(false);
	player.getInterfaceManager().closeOverlay(false);
	player.reset();
    }

    @Override
    public void magicTeleported(int teleType) {
	player.getControlerManager().forceStop();
    }

    @Override
    public boolean processMagicTeleport(Position toTile) {
	player.getDialogueManager().startDialogue("SimpleMessage", "You can't leave the pest control area like this.");
	return false;
    }

    @Override
    public boolean processItemTeleport(Position toTile) {
	player.getDialogueManager().startDialogue("SimpleMessage", "You can't leave the pest control area like this.");
	return false;
    }

    @Override
    public boolean canMove(int dir) {
	Position toTile = new Position(player.getX() + Utils.DIRECTION_DELTA_X[dir], player.getY() + Utils.DIRECTION_DELTA_Y[dir], player.getZ());
	return !control.isBrawlerAt(toTile);
    }

    @Override
    public boolean login() {
	return true;
    }

    @Override
    public boolean logout() {
	if (control != null)
	    control.getPlayers().remove(player);
	return false;
    }

    @Override
    public boolean canSummonFamiliar() {
	player.getPackets().sendGameMessage("You feel it's best to keep your Familiar away during this game.");
	return false;
    }

    @Override
    public boolean sendDeath() {
	WorldTasksManager.schedule(new WorldTask() {
	    int loop;

	    @Override
	    public void run() {
		if (loop == 0) {
		    player.animate(new Animation(836));
		} else if (loop == 1) {
		    player.getPackets().sendGameMessage("Oh dear, you have died.");
		} else if (loop == 3) {
		    player.reset();
		    player.setNextPosition(control.getWorldTile(35 - Utils.random(4), 54 - (Utils.random(3))));
		    player.animate(new Animation(-1));
		} else if (loop == 4) {
		    player.getPackets().sendMusicEffect(90);
		    stop();
		}
		loop++;
	    }
	}, 0, 1);
	return false;
    }

    @Override
    public void trackXP(int skillId, int addedXp) {
	    setPoints(getPoints() + addedXp);
	updatePestPoints();
    }

    public double getPoints() {
	return points;
    }

    public void setPoints(double points) {
	this.points = points;
    }
}
