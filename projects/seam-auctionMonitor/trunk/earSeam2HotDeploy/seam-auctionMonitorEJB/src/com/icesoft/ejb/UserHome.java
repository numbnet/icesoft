package com.icesoft.ejb;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.ScopeType;

import com.icesoft.eb.User;

import javax.faces.event.ActionEvent;


@Name("userHome")
@Scope(ScopeType.CONVERSATION)
public class UserHome extends EntityHome<User> {
	private boolean openStatus = true;

	public void setUserUserId(String id) {
		setId(id);
	}

	public String getUserUserId() {
		return (String) getId();
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


}
