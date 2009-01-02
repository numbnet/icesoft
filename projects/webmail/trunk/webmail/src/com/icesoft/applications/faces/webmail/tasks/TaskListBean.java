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
import com.icesoft.applications.faces.webmail.WebmailBase;
import com.icesoft.applications.faces.webmail.login.User;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.util.Date;
import com.icesoft.faces.component.ext.RowSelectorEvent;
/**
 * <p>The <code>TasksBean</code> class represents the view of the <code>Tasks</code>
 * model class. This class is responsible for handling user events from the
 * corresponding JSF view code.</p>
 * <p/>
 * <p>The <code>TasksBean</code> is responsible for interacting with
 * <code>TaskDAO</code> object and thus the manipulation of the database
 * values. </p>
 *
 * @since 1.0
 */
public class TaskListBean extends TaskList implements WebmailBase {

    // userBean object for this session login, need by DAO to build SQL queries.
    private User validUser;

    // selected task
    private TaskBean editableTaskBean;

    // checked count
    private int checkedCount = 0;
    
    /**
      * Field for enabling/disabling multiple tasks selection in email list table.
      * Placeholder for future enhancement which allows user to make choice between 
      * one or multiple message selections
      */	
    public boolean multipleSelection = true;

    /**
     * The method should be called to initialize the TasksBean and to refresh
     * the list of tasks from the dB.
     */
    public void refreshTaskList() {
        // Query the DB to find out how many mail accounts are associated with
        if (validUser != null) {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            session.beginTransaction();
            Query mailAccountQuery = session.createQuery("from TaskBean where username = :verifiedUser");

            mailAccountQuery.setParameter("verifiedUser", validUser.getUserName());

            try {
                // clean up the old data representation before retrieving the new
                if (taskList != null) {
                    taskList.clear();
                }

                // get accounts associated with user
                taskList = mailAccountQuery.list();
            } catch (HibernateException e) {
                log.error("Failed to read tasks table - SQL error", e);
            }

            // finish the transaction
            session.getTransaction().commit();

            // set call back to Tasks bean
            checkedCount = 0;
            TaskBean taskBean;
            for (int i = taskList.size() - 1; i >= 0; i--) {
                taskBean = (TaskBean) taskList.get(i);
                taskBean.setTasksCallback(this);

                if (taskBean.isSelected()) {
                    checkedCount++;
                }
            }

            // resort list
            sort(getSort(), isAscending());
        }
    }

    /**
     * Changes the webmail view to display a blank task edit view.  When this
     * view is saved the task is added to the database. If the new task
     * is saved in this manner, it will also become the current editable task
     */
    public void createNewTask(ActionEvent actionEvent) {
        log.debug("Create new task");

        // create a new taskSBean which may or may not be saved by the user
        editableTaskBean = new TaskBean();
        editableTaskBean.setName("New Task");
        editableTaskBean.setStatus(0);
        editableTaskBean.setCategory(0);
        editableTaskBean.setDescription("");
        editableTaskBean.setTasksCallback(this);
        editableTaskBean.setPriority(1);
        editableTaskBean.setReminderDate(new Date());
        editableTaskBean.setStartDate(new Date());
        editableTaskBean.setEndDate(new Date());

    }

    /**
     * Sets the desired task as the current task and changes the view so that
     * the current task is in editable form. The task can then be changed by
     * the view controls.
     */
    public void editTasks(ActionEvent actionEvent) {
        log.debug("Edit task");

        // work through taskList and find selected one.
        TaskBean taskBean;
        for (int i = taskList.size() - 1; i >= 0; i--) {
            taskBean = (TaskBean) taskList.get(i);

            // edit the first selected instance, should only ever be one.
            if (taskBean.isSelected()) {
                editableTaskBean = taskBean;
            }
        }

        // resort list
        sort(getSort(), isAscending());
    }

    /**
     * Removes all tasks in the tasks model that are in the selected state.
     */
    public void deleteTasks(ActionEvent actionEvent) {
        log.debug("Delete task");

        // save the changes to the database
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        // work through taskList and remove selected ones
        TaskBean taskBean;
        for (int i = taskList.size() - 1; i >= 0; i--) {
            taskBean = (TaskBean) taskList.get(i);

            // check if the current task has it's checkbox selected
            // if it does, remove the task (from the database as well) and update checkedCount
            if (taskBean.isSelected()) {
                taskList.remove(i);
                checkedCount--;

                // perform database level removal
                try {
                    session.delete(taskBean);
                } catch (HibernateException e) {
                    log.error("Failed to savOrUpdate tasks table - SQL error", e);
                }
            }
        }

        // finish the transaction
        session.getTransaction().commit();

        // resort list
        sort(getSort(), isAscending());
    }

    public User getValidUser() {
        return validUser;
    }

    public void setValidUser(User validUser) {
        this.validUser = validUser;
    }

    public TaskBean getEditableTaskBean() {
        // Ensure a valid non-null bean will be returned
        if (editableTaskBean == null) {
            createNewTask(null);
        }

        return editableTaskBean;
    }

    public void setEditableTaskBean(TaskBean editableTaskBean) {
        this.editableTaskBean = editableTaskBean;
    }

    /**
     * Initializes the class and retreives and need data from TaskDAO.
     */
    public void init() {
        refreshTaskList();
    }

    /**
     * Disposes resources associated with this class
     */
    public void dispose() {
        // make sure to clear the arrayList of tasks
        if (taskList != null) {
            taskList.clear();
            taskList = null;
        }

    }

    /**
     * Listener method called when a checkbox on the page is changed
     * The method will update the current checkbox count
     */
    public void changeTaskSelection(ValueChangeEvent event) {
        // Maintain a count of selected checkboxes
        if (event.getNewValue().toString().equals("true")) {
            checkedCount++;
        } else {
            checkedCount--;
        }
    }

    
        /**
     * Listener method called when a task on the page is selected
     * The method will update the current selected tasks count
     */
   public void rowSelection(RowSelectorEvent event) {
        // Maintain a count of selected messages
        if (event.isSelected()) {
            checkedCount++;
        } else {
            checkedCount--;
        }
    }
    public int getCheckedCount() {
        return checkedCount;
    }

    /**
     * Listener method to handle a change in the category dropdown menu
     */
    public void processCategoryChange(ValueChangeEvent event) {
        editableTaskBean.setCategory(Integer.parseInt(event.getNewValue().toString()));

        // resort list
        sort(getSort(), isAscending());
    }

    /**
     * Listener method to handle a change in the priority dropdown menu
     */
    public void processPriorityChange(ValueChangeEvent event) {
        editableTaskBean.setPriority(Integer.parseInt(event.getNewValue().toString()));

        // resort list
        sort(getSort(), isAscending());
    }

    /**
     * Listener method to handle a change in the status dropdown menu
     */
    public void processStatusChange(ValueChangeEvent event) {
        int newStatus = Integer.parseInt(event.getNewValue().toString());

        // Provide a "guess value" for percent complete based on the new status
        // For example, changing the status to "Finished" will put percentComplete to 100%
        if (newStatus > 0) {
            editableTaskBean.setPercentComplete(newStatus * 25);
        }

        editableTaskBean.setStatus(newStatus);

        // resort list
        sort(getSort(), isAscending());
    }

    public SelectItem[] getCategoryList() {
        return TasksManager.categoryList;
    }

    public SelectItem[] getPriorityList() {
        return TasksManager.priorityList;
    }

    public SelectItem[] getStatusList() {
        return TasksManager.statusList;
    }

    /**
     * Select all tasks in the table
     *
     * @param event
     */
    public void selectAll(ActionEvent event) {
        for (int i = 0; i < this.taskList.size(); i++) {

            if (!((Task) taskList.get(i)).isSelected()) {
                ((Task) taskList.get(i)).setSelected(true);
                checkedCount++;
            }
        }
    }

    /**
     * Deselect all tasks in the table
     * @param event
     */
    public void deselectAll(ActionEvent event) {
        for (int i = 0; i < this.taskList.size(); i++) {

            if (((Task) taskList.get(i)).isSelected()) {
                ((Task) taskList.get(i)).setSelected(false);
                checkedCount--;
            }
        }
    }
    
   /**
     * Is multiple task selection enabled in the task list table?
     * @return true if multiple task is enabled
     */

    public boolean isMultipleSelection() {
        return multipleSelection;
    }

    /**
     * Sets the state of multiple task selection.
     *
     * @param multipleSelection
     */

    public void setMultipleSelection(boolean multipleSelection) {
        this.multipleSelection = multipleSelection;
    }

}
