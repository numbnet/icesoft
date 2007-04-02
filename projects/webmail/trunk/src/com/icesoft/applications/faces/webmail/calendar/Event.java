/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * Class to represent an event, and track information about the event
 *
 * @since 1.0
 */
public abstract class Event {

    private static Log log = LogFactory.getLog(Event.class);

    protected boolean selected;

    protected int id;

    // UserAccount name of user
    protected String userName;

    protected String name;
    protected String description;

    protected int category;

    protected Date reminderDate;
    protected Date startDate;
    protected Date endDate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    /**
     * Indicates that view has selected this task.
     *
     * @return true if selected, false otherwise.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the selected state of this task.
     *
     * @param selected true for selected, false otherwise.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}