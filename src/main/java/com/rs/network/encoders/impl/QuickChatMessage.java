package com.rs.network.encoders.impl;

import com.rs.network.encoders.impl.PublicChatMessage;

public class QuickChatMessage extends PublicChatMessage {

    private int fileId;

    public QuickChatMessage(int fileId, byte[] data) {
	super(data == null ? null : new String(data), 0x8000);
	this.fileId = fileId;
    }

    public int getFileId() {
	return fileId;
    }

    public void setFileId(int fileId) {
	this.fileId = fileId;
    }

}
