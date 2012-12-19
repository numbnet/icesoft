package org.icemobile.samples.springbasic;

import java.util.Map;

public class ModelBean {
	
	private String name;
	
	
	private Map<String, String> additionalInfo;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(Map<String, String> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("properties name=");
        if (name != null) {
        	sb.append("'").append(name).append("', ");
        } else {
        	sb.append(name).append(", ");
        }
        sb.append("additionalInfo=").append(additionalInfo);
        return sb.toString();
    }
}
