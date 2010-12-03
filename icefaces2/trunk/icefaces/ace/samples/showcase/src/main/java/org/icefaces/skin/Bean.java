package org.icefaces.skin;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean (name="skinBean")
@SessionScoped
public class Bean {
	private String skin = "ice-skin-sam";
	
	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	private SelectItem[] skins = new SelectItem[2];
	
	public SelectItem[] getSkins() {
		return skins;
	}

	public void setSkins(SelectItem[] skins) {
		this.skins = skins;
	}

	public Bean() {
		skins[0] = new SelectItem("ice-skin-sam", "Sam");
		skins[1] = new SelectItem("ice-skin-rime", "Rime");		
	}
	
	

}
