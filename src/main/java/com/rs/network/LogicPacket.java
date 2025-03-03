package com.rs.network;

import com.rs.network.io.InputStream;

public class LogicPacket {

	private int id;
	byte[] data;

	public LogicPacket(int id, int size, InputStream stream) {
		this.id = id;
		data = new byte[size];
		stream.getBytes(data, 0, size);
	}

	public int getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}
	
	public byte[] setData(byte[] data) {
		return this.data = data;
	}

}
