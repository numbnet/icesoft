package com.icesoft.tutorial.timezone;

import java.util.TimeZone;

public class TimeZoneWrapper {
	private String id;
	private String componentId;

	public TimeZoneWrapper(String id, String componentId) {
		this.id = id;
		this.componentId = componentId;
	}

	public String getComponentId() {
		return componentId;
	}

	public String getId() {
		return id;
	}

	public boolean isRelevant(String componentId) {
		return componentId.endsWith(this.componentId);
	}
}
