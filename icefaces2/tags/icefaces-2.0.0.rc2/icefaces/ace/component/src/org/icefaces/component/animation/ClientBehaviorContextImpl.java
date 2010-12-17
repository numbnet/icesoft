package org.icefaces.component.animation;

import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;

public class ClientBehaviorContextImpl extends ClientBehaviorContext {
	private UIComponent uiComponent;
	private Collection<Parameter> parameters;
	private String eventName;
	private String sourceId;
	
	public ClientBehaviorContextImpl(UIComponent uiComponent, String eventName) {
		this.uiComponent = uiComponent;
		this.eventName = eventName;
	}
	
	public ClientBehaviorContextImpl(UIComponent uiComponent, String eventName, Collection<Parameter> parameters) {
		this(uiComponent, eventName);
		this.parameters = parameters;
	}
	
	public ClientBehaviorContextImpl(UIComponent uiComponent, String eventName, Collection<Parameter> parameters, String sourceId) {
		this(uiComponent, eventName, parameters);
		this.sourceId = sourceId;
	}
	
	@Override
	public UIComponent getComponent() {
		return uiComponent;
	}

	@Override
	public String getEventName() {
		return eventName;
	}

	@Override
	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	@Override
	public Collection<Parameter> getParameters() {
		return parameters;
	}

	@Override
	public String getSourceId() {
		return sourceId;
	}

}
