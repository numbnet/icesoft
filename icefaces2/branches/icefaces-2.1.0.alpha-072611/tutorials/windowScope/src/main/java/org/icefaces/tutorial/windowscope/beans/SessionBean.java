package org.icefaces.tutorial.windowscope.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="sessionBean")
@SessionScoped
public class SessionBean implements Serializable {
	private Timestamp created;
	
	public SessionBean() {
		created = new Timestamp(System.currentTimeMillis());
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}
}
