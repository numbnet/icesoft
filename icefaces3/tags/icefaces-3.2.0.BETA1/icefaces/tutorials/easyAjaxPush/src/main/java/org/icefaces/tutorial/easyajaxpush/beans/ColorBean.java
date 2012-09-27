/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.tutorial.easyajaxpush.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.icefaces.application.PushRenderer;

import org.icefaces.tutorial.easyajaxpush.model.TextModel;

@ManagedBean(name="colorBean")
@ViewScoped
public class ColorBean implements Serializable {
	private static final String PUSH_GROUP = "colorPage";
	
	@ManagedProperty(value="#{messageBean}")
	private MessageBean messageBean;
	private String color = "black";
	private String sessionId;
	
	public ColorBean() {
		PushRenderer.addCurrentSession(PUSH_GROUP);
		
		FacesContext fcontext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession)fcontext.getExternalContext().getSession(false);
		sessionId = session.getId();
	}
	
	public void setMessageBean(MessageBean messageBean) {
		this.messageBean = messageBean;
	}

        public List<TextModel> getTextList() {
                return messageBean.getTextList();
        }
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String chooseColor() {
		messageBean.addToList(sessionId, color);
		
		PushRenderer.render(PUSH_GROUP);
		
		return null;
	}
}
