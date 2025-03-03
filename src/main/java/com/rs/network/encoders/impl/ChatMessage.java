package com.rs.network.encoders.impl;

import com.rs.utility.Censor;
import com.rs.utility.Utils;

import java.nio.charset.StandardCharsets;

public class ChatMessage {

	private String message;
	private String filteredMessage;

	public ChatMessage(String message) {
		filteredMessage = Censor.getFilteredMessage(message);
		this.message = Utils.fixChatMessage(new String(message.getBytes(StandardCharsets.UTF_8)));
	}

	public String getMessage(boolean filtered) {
		return filtered ? filteredMessage : message;
	}
}
