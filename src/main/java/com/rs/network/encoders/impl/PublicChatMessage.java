package com.rs.network.encoders.impl;

public class PublicChatMessage extends ChatMessage {

	private int effects;

	public PublicChatMessage(String message, int effects) {
		super(message);
		this.effects = effects;
	}

	public int getEffects() {
		return effects;
	}

}
