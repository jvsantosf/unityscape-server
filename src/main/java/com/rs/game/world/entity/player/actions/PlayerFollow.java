package com.rs.game.world.entity.player.actions;

import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

public class PlayerFollow extends Action {

	private Player target;

	public PlayerFollow(Player target) {
		this.target = target;
	}

	@Override
	public boolean start(Player player) {
		player.setNextFaceEntity(target);
		if (checkAll(player))
			return true;
		player.setNextFaceEntity(null);
		return false;
	}

	private boolean checkAll(Player player) {
		if (player.isDead() || player.isFinished() || target.isDead()
				|| target.isFinished())
			return false;
		int distanceX = player.getX() - target.getX();
		int distanceY = player.getY() - target.getY();
		int size = target.getSize();
		int maxDistance = 16;
		if (player.getZ() != target.getZ()
				|| distanceX > size + maxDistance
				|| distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance) 
			return false;
		if (player.getFreezeDelay() >= Utils.currentTimeMillis())
			return true;
		maxDistance = 0;
		if ((!player.clipedProjectile(target, maxDistance == 0))
				|| distanceX > size + maxDistance
				|| distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance) {
			if (player.hasWalkSteps())
				player.resetWalkSteps();
			player.addWalkStepsInteract(target.getX(), target.getY(),
					player.getRun() ? 2 : 1, size, true);
			// }
			return true;
		} else {
			player.resetWalkSteps();
			int lastFaceEntity = target.getLastFaceEntity();
			if (lastFaceEntity >= 32768) {
				lastFaceEntity -= 32768;
				if (lastFaceEntity == player.getIndex())
					player.addWalkSteps(target.getLastPosition().getX(),
							target.getLastPosition().getY(), size, true);
			}
		}

		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		return 0;
	}

	@Override
	public void stop(final Player player) {
		player.setNextFaceEntity(null);
	}

}
