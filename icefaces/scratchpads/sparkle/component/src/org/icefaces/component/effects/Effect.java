package org.icefaces.component.effects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.icefaces.component.utils.JSONBuilder;

public abstract class Effect{
	private boolean usingStyleClass;
	private EffectBehavior effectBehavior;
	private Map properties = new HashMap(); 
	private String sourceElement;
	
    String getSourceElement() {
		return sourceElement;
	}

	void setSourceElement(String sourceElement) {
		this.sourceElement = sourceElement;
		properties.put("node", "#"+ sourceElement);
	}

	public Map getProperties() {
		return properties;
	}

	public void setProperties(Map properties) {
		this.properties = properties;
	}
	
	public String getPropertiesAsJSON() {
		return convertMapToJSON(properties);
	}	
	
	private String convertMapToJSON(Map map) {
		JSONBuilder json = JSONBuilder.create();
		json.beginMap();
		Iterator<String> props = map.keySet().iterator();
		while (props.hasNext()) {
			String prop = props.next();
			Object value = map.get(prop);
			if (value == null) continue;
			if (value instanceof Map) {
				json.entry (prop, convertMapToJSON((Map)value), true);
			} else {
				String val = value.toString();
				try {
					Integer inVaue = Integer.parseInt(val);
					json.entry(prop, inVaue);
				} catch (Exception exception) {
					try {
						Float floatValue = Float.parseFloat(val);
						json.entry(prop, floatValue);
					} catch(Exception ex) {
						if ("true".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val)) {
							Boolean bolValue = Boolean.parseBoolean(val);
							json.entry(prop, bolValue);
						} else {
							json.entry(prop, value.toString());
						}
					}						
				}
			}
		}
        return json.endMap().toString();		
	}
	EffectBehavior getEffectBehavior() {
		return effectBehavior;
	}

	void setEffectBehavior(EffectBehavior effectBehavior) {
		this.effectBehavior = effectBehavior;
	}

	public boolean isUsingStyleClass() {
		return usingStyleClass;
	}

	public void setUsingStyleClass(boolean usingStyleClass) {
		this.usingStyleClass = usingStyleClass;
	}

	public String getName() {
    	return this.getClass().getSimpleName();
    }
	
	
	public void run() {
		if(null != getEffectBehavior()) {
			effectBehavior.setRun(true);
		}
	}
	
 
}
