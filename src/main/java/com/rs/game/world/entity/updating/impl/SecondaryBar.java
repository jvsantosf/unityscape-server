package com.rs.game.world.entity.updating.impl;

public class SecondaryBar {

	private int beginningOffset, totalUnits, incrementalUnits;
	private boolean permanent;

	public SecondaryBar(int beginningOffset, int totalUnits, int incrementalUnits, boolean permanent) {
		this.beginningOffset = beginningOffset;
		this.totalUnits = totalUnits;
		this.incrementalUnits = incrementalUnits;
		this.permanent = permanent;
	}

	public int getBeginningOffset() {
		return beginningOffset;
	}

	public int getTotalUnits() {
		return totalUnits;
	}

	public int getIncrementalUnits() {
		return incrementalUnits;
	}

	public boolean isPermenant() {
		return permanent;
	}
}
