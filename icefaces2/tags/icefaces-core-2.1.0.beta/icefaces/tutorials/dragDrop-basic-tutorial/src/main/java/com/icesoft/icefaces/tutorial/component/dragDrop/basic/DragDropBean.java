package com.icesoft.icefaces.tutorial.component.dragDrop.basic;

import com.icesoft.faces.component.dragdrop.DragEvent;

/**
 * <p>The DragDropBean handles DragEvent listeners for the
 * Drag and Drop tutorial.</p>
 */
public class DragDropBean {

	private String dragMessage = "";
		
	public void dragListener(DragEvent dragEvent){
	        dragMessage += DragEvent.getEventName(dragEvent.getEventType()) + ", ";
	}

	public String getDragMessage () {
	        return dragMessage;
	}
}
