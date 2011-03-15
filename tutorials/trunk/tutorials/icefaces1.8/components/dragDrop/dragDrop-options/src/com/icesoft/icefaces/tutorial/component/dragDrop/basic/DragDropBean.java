package com.icesoft.icefaces.tutorial.component.dragDrop.basic;

import com.icesoft.faces.component.dragdrop.DndEvent;
import com.icesoft.faces.component.dragdrop.DragEvent;

/**
 * <p>The DragDropBean handles DragEvent listeners for the
 * Drag and Drop tutorial.</p>
 */
public class DragDropBean {

	private String dragMessage = "";
	private String dropValue = "";
		
	public void dragListener(DragEvent dragEvent){
	        dragMessage += DragEvent.getEventName(dragEvent.getEventType()) + ", ";
	        
	        if (dragEvent.getEventType() == DndEvent.DROPPED)
	        	dropValue = (String) dragEvent.getTargetDropValue();
	}

	public String getDropValue () {
			return dropValue;
	}
	
	public String getDragMessage () {
	        return dragMessage;
	}
}
