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

package org.icefaces.skin;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.faces.event.ValueChangeEvent;

@ManagedBean (name="skinBean")
@SessionScoped
public class Bean implements Serializable{
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
