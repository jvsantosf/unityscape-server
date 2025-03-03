package com.rs.game.world.entity.npc.familiar;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;

/**
 * Represents the TzRek-Jad pet.
 * 
 * @author Emperor
 * 
 */
public final class TzRekJad extends Familiar {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 1345183208084953068L;

	/**
	 * Constructs a new {@code TzRekJad} {@code Object}.
	 * 
	 * @param owner
	 *            The owner.
	 * @param tile
	 *            The world tile to spawn on.
	 */
	public TzRekJad(Player owner, Position tile) {
		super(owner, null, tile, -1, true);
	}

	@Override
	public String getSpecialName() {
		return "null";
	}

	@Override
	public String getSpecialDescription() {
		return "null";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 0;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return null;
	}

	@Override
	public boolean submitSpecial(Object object) {
		return false;
	}

}