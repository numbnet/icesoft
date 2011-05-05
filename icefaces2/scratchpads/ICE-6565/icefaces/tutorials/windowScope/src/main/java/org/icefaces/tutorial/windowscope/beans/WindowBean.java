package org.icefaces.tutorial.windowscope.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="windowBean")
@CustomScoped(value = "#{window}")
public class WindowBean implements Serializable {
	private Timestamp created;
	
	public WindowBean() {
		created = new Timestamp(System.currentTimeMillis());
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}
}
