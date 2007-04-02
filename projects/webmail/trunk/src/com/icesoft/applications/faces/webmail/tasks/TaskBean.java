/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.tasks;

import com.icesoft.applications.faces.webmail.util.db.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.faces.event.ActionEvent;

/**
 * <p>The <code>TaskBean</code> class represents the view of the <code>Task</code>
 * model class. This class is responsible for handling user events from the
 * corresponding JSF view code.</p>
 *
 * @since 1.0
 */
public class TaskBean extends Task {

    private static Log log = LogFactory.getLog(TaskBean.class);

    private TaskListBean parentTasks;

    /**
     * Saves the model values for this bean's model and changes the view to the
     * tasksBean view.
     */
    public void saveChanges(ActionEvent event) {

        log.info("Save Changes");

        // check if this is a new addition
        if (userName == null) {
            userName = parentTasks.getValidUser().getUserName();
        }

        try {
            // save the changes to the database
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.saveOrUpdate(this);
            // finish the transaction
            session.getTransaction().commit();
        }
        catch (HibernateException e) {
            log.error("Failed to cancel task changes - SQL error", e);
        }
    }

    /**
     * Cancels any changes of the underlining bean model data and changes the view
     * to the tasksBean view.
     */
    public void restoreChanges(ActionEvent event) {
        log.info("Cancel Changes");

        try {
            // save the changes to the database
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.refresh(this);
            // finish the transaction
            session.getTransaction().commit();
        }
        catch (HibernateException e) {

        }

    }

    /**
     * Sets the call back to the list of tasks.
     * @param tasksBean
     */
    public void setTasksCallback(TaskListBean tasksBean) {
        parentTasks = tasksBean;
    }

    /**
     * Called when a new task has been selected.
     * @param event
     */
    public void changeSelectedTask(ActionEvent event) {
        log.info("Set selection change event");
        if (parentTasks != null) {
            parentTasks.setEditableTaskBean(this);
        }
    }
}
