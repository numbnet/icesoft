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

import com.icesoft.applications.faces.webmail.WebmailBase;
import com.icesoft.applications.faces.webmail.WebmailMediator;
import com.icesoft.applications.faces.webmail.login.User;
import com.icesoft.applications.faces.webmail.navigation.NavigationContent;
import com.icesoft.applications.faces.webmail.navigation.NavigationSelectionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * <p>The <code>TaskManager</code> Class used to manage task information stored
 * in the webmail database....
 * <p/>
 *
 * @since 0.3.0
 */
public class TasksManager implements WebmailBase {

    private static Log log = LogFactory.getLog(TasksManager.class);

    // global default lists for the various task categories
    // these lists should eventually be read from a database so the user can change them
    public static final SelectItem[] categoryList = new SelectItem[]{
            new SelectItem("0", "Business"),
            new SelectItem("1", "Family"),
            new SelectItem("2", "Vacation")
    };
    public static final SelectItem[] priorityList = new SelectItem[]{
            new SelectItem("1", "Minor"),
            new SelectItem("2", "Normal"),
            new SelectItem("3", "Critical")
    };
    public static final SelectItem[] statusList = new SelectItem[]{
            new SelectItem("0", "Not Started"),
            new SelectItem("1", "Started"),
            new SelectItem("2", "In Progress"),
            new SelectItem("3", "Finished")
    };

    // link to mediator for applications mediation
    private WebmailMediator mediator;

    // list of all tasks for account
    private TaskListBean tasksBean;

    private boolean isInit = false;

    private NavigationContent taskEditNavigation;
    private NavigationContent taskListNavigation;

    public TasksManager() {

    }

    public void setMediator(WebmailMediator mediator) {
        this.mediator = mediator;
        this.mediator.setTasksManager(this);
    }

    /**
     * Navigate to the editable task panel
     */
    public String navigateToEditTask() {

        // navigate to the tasksEditViewPanel
        NavigationSelectionBean navigationSelectionBean =
                mediator.getNavigationManager().getNavigationSelectionBean();

        // apply pre-defined navigation rue.
        navigationSelectionBean.setSelectedPanel(taskEditNavigation);

        return null;
    }

    /**
     * Navigate to the task list panel
     */
    public String navigateToTaskList() {

        // navigate to the tasksEditViewPanel
        NavigationSelectionBean navigationSelectionBean =
                mediator.getNavigationManager().getNavigationSelectionBean();

        // apply pre-defined navigation rue.
        navigationSelectionBean.setSelectedPanel(taskListNavigation);

        // re-read list
        tasksBean.refreshTaskList();

        return null;
    }

    /**
     * Build all needed child object and query the DB for this process if necessary
     */
    public void init() {

        if (isInit) {
            return;
        }
        isInit = true;

        // setup default navigation
        taskEditNavigation = new NavigationContent();
        taskEditNavigation.setNavigationSelection(
                mediator.getNavigationManager().getNavigationSelectionBean());
        taskEditNavigation.setTemplateName("./inc/contentPanels/tasksEditViewPanel.jspx");
        taskEditNavigation.setMenuContentTitle("Task-edit");

        taskListNavigation = new NavigationContent();
        taskListNavigation.setNavigationSelection(
                mediator.getNavigationManager().getNavigationSelectionBean());
        taskListNavigation.setTemplateName("./inc/contentPanels/tasksViewPanel.jspx");
        taskListNavigation.setMenuContentTitle("Tasks");

        // do a little clean up in case the api is used incorrectly.
        if (tasksBean != null) {
            tasksBean.dispose();
            tasksBean = null;
        }

        // initialized the TasksBean view, we need a reference to a valid user
        // to query the DB.
        User validUser =
                mediator.getLoginManager().getLoginBeanFactory().getVerifiedUser();
        tasksBean = new TaskListBean();
        tasksBean.setValidUser(validUser);

        // load all tasks found in DB for tasks.
        tasksBean.init();
    }

    public void dispose() {
        isInit = false;

        if (log.isDebugEnabled()) {
            log.debug(" Disposing TaskManager");
        }

        if (tasksBean != null) {
            tasksBean.dispose();
        }
    }

    public TaskListBean getTasksBean() {
        return tasksBean;
    }

    public void setTasksBean(TaskListBean tasksBean) {
        this.tasksBean = tasksBean;
    }

}
