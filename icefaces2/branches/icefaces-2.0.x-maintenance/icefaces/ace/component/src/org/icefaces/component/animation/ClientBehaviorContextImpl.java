/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

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
