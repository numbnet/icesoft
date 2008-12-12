/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
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
