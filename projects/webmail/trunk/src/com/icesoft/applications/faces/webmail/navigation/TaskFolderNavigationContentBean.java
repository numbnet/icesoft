/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.navigation;

import com.icesoft.applications.faces.webmail.tasks.TasksManager;
import com.icesoft.faces.component.dragdrop.DropEvent;

import javax.faces.event.ActionEvent;

/**
 * This is just a wrapper for a TasksManager so that it can be managed
 * in the context of the NavigationManager for displaying content.
 *
 * @since 1.0
 */
public class TaskFolderNavigationContentBean extends NavigationContent {

    protected TasksManager tasksManager;

    /**
     * Creates a new TaskFolderNavigationContentBean.  The default icon for
     * this node is also set.
     */
    public TaskFolderNavigationContentBean() {
        super();
        setLeaf(true);
        setLeafIcon("images/taskfortree.gif");
    }

    public TasksManager getTasksManager() {
        return tasksManager;
    }

    public void setTasksManager(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
    }

    /**
     * Sets the navigationSelctionBeans selected state to this NavigationContent
     */
    public void contentVisibleAction(ActionEvent event) {
        // update tasks list
        if (tasksManager != null)
            tasksManager.getTasksBean().refreshTaskList();

        // set navigation selected panel
        super.contentVisibleAction(event);
    }

    /**
     * Called when an drop event occures on this navigation content.
     *
     * @param event drop event.
     */
    public void navigationDropAction(DropEvent event) {
        currentEffect = dropFailureEffect;
        dropFailureEffect.setFired(false);
    }
}
