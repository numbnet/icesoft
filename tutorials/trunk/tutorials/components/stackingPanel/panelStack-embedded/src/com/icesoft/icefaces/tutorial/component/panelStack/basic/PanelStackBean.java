package com.icesoft.icefaces.tutorial.component.panelStack.basic;

import javax.faces.event.ActionEvent;

/**
 * <p>
 * The PanelStackBean controls the visibilty of the 
 * panelStack's child panelGroup element. Using a bean
 * allows the selected element to be changed with a UI control.
 *</p>
 */
public class PanelStackBean {

	// default panel
	private String selectedGroup = "groupOne";
	private String actionSelectedGroup = "groupTwo";

	/**
	 * Gets the selected panel group.
	 * 
	 * @return the panel group id
	 */
	public String getSelectedGroup() {
		return selectedGroup;
	}

	/**
	 * Sets the selected panel group.
	 * 
	 * @param selectedGroup the new panel group id
	 */
	public void setSelectedGroup(String selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	public void switchGroup(ActionEvent e){
		selectedGroup = actionSelectedGroup;
	}

	public String getActionSelectedGroup() {
		return actionSelectedGroup;
	}

	public void setActionSelectedGroup(String actionSelectedGroup) {
		this.actionSelectedGroup = actionSelectedGroup;
	}
}
