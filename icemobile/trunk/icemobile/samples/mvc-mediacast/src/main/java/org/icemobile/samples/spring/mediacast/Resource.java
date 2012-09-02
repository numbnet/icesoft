package org.icemobile.samples.spring.mediacast;

public class Resource {
	
	private String id;
	private byte[] data;
	private String type;
	private String fileName;
	
	public Resource(String id, byte[] data, String type) {
		super();
		this.id = id;
		this.data = data;
		this.type = type;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getURL(){
		return fileName;
	}

}
