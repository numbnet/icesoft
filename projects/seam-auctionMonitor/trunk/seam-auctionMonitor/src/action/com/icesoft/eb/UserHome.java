package com.icesoft.eb;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import javax.faces.event.ActionEvent;
import com.icesoft.faces.component.accordion.PanelAccordion;

@Name("userHome")
public class UserHome extends EntityHome<User> {
	private boolean openStatus = true;

	public void setUserUserId(Integer id) {
		setId(id);
	}

	public Integer getBidderBidderId() {
		return (Integer) getId();
	}

	@Override
	protected User createInstance() {
		User user = new User();
		return user;
	}

	public void wire() {
	}

	public boolean isWired() {
		return true;
	}

	public User getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void toggle(ActionEvent event) {
		PanelAccordion component1 = (PanelAccordion) event.getSource();
		openStatus = component1.getExpanded().booleanValue();
	}

	public void setOpenStatus(boolean input) {
		this.openStatus = input;
	}

	public boolean getOpenStatus() {
		return openStatus;
	}
}
