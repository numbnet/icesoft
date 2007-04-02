/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.tasks;

import com.icesoft.applications.faces.webmail.calendar.Event;
import com.icesoft.applications.faces.webmail.WebmailMediator;


/**
 * The <code>Task</code> class represents the <code>TaskBean</code>'s model.
 * This class is responsible for storing task related data such as priority, status, etc.
 * All generic event data such as name or description is stored in the parent Event class
 * <code>TasksBean</code> view.
 *
 * @since 1.0
 */
public class Task extends Event {

    protected int priority;

    protected int status;

    protected int percentComplete;
    protected String percentCompleteStr;


    public Task() {
        super();
    }

    public int getPriority() {
        return priority;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusAsString() {
        // Get the label based on the status value
        return (TasksManager.statusList[status].getLabel());
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPercentComplete(int percentComplete) {
        if (percentComplete > 100) {
            this.percentComplete = 100;
        } else if (percentComplete < 0) {
            this.percentComplete = 0;
        } else {
            this.percentComplete = percentComplete;
        }
        percentCompleteStr = Integer.toString(this.percentComplete);
    }

    public void setPercentCompleteStr(String percentCompleteStr) {
        this.percentCompleteStr = percentCompleteStr;
        if (percentCompleteStr.equals(""))
        	this.percentComplete = 0;
        else{
        	try {
            	setPercentComplete(Integer.parseInt(percentCompleteStr));
        	} catch (NumberFormatException e) {
            	//System.out.println("Invalid number format.");
            	WebmailMediator.addMessage(
            	        "tevp:taskEditForm",
            	        "percentInput",
            	        "webmail.tasks.edit.invalidNumber", null);
        	}
		}
    }

    public String getPercentCompleteStr() {
        return percentCompleteStr;
    }
}
