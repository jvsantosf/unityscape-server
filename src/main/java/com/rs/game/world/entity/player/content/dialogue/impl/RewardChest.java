package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.qbd.QueenBlackDragon;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * Handles the Queen Black Dragon reward chest dialogue.
 * @author Emperor
 *
 */
public final class RewardChest extends Dialogue {

	/**
	 * The NPC.
	 */
	private QueenBlackDragon npc;
	
	@Override
	public void start() {
		npc = (QueenBlackDragon) parameters[0];
		super.sendDialogue("This strange device is covered in indecipherable script. It opens for you,", "displaying only a small sample of the objects it contains.");

	}

	@Override
	public void run(int interfaceId, int componentId) {
		npc.openRewardChest(true);
		super.end();
	}

	@Override
	public void finish() { }
	
}