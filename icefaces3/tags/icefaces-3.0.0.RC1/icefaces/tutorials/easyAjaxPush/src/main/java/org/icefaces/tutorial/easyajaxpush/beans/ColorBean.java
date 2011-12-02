package org.icefaces.tutorial.easyajaxpush.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.icefaces.application.PushRenderer;

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
