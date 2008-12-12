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

import com.icesoft.applications.faces.webmail.common.SortableList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>The <code>Tasks</code> class represents the <code>TasksBean</code>'s model.
 * This class is responsible for storing the tasklist associated with the
 * <code>TasksBean</code> view.<p>
 * <p/>
 * <p>This class implements SortableList so that it can be easily used with
 * a ICE:dataTable and a ICE:commandSortHeader.  </p>
 *
 * @since 0.3.0
 */
public class TaskList extends SortableList {

    protected static Log log = LogFactory.getLog(TaskList.class);

    // array of tasks associated with this tasks view.
    protected List taskList;

    public TaskList() {
        super("startDateColumn");
    }

    /**
     * Gets the tasks list after being sorted.
     *
     * @return tasks list
     */
    public List getTaskList() {
        // sort the list as needed
        if (!oldSort.equals(sort) ||
                oldAscending != ascending){
             sort(getSort(), isAscending());
             oldSort = sort;
             oldAscending = ascending;
        }
        return taskList;
    }

    /**
     * Sets the task list
     *
     * @param taskList
     */
    public void setTaskList(ArrayList taskList) {
        this.taskList = taskList;
    }

    /**
     * Sort the list.
     */
    protected void sort(final String column, final boolean ascending) {

        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Task c1 = (Task) o1;
                Task c2 = (Task) o2;
                if (column == null) {
                    return 0;
                }
                // Sort check box column
                if (column.equals("checked")) {
                    return ascending ?
                            (c1.isSelected() == c2.isSelected() ? 0 : 1) :
                            (c2.isSelected() == c1.isSelected() ? 0 : 1);
                }
                // Sort priority column (based on the value, not the label)
                else if (column.equals("priorityColumn")) {
                    if (ascending) {
                        if (c1.getPriority() > c2.getPriority())
                            return 1;
                        else if (c1.getPriority() < c2.getPriority())
                            return -1;
                        else
                            return 0;
                    } else {
                        if (c1.getPriority() > c2.getPriority())
                            return -1;
                        else if (c1.getPriority() < c2.getPriority())
                            return 1;
                        else
                            return 0;
                    }
                }
                // Sort task name column
                else if (column.equals("nameColumn")) {
                    return ascending ?
                            c1.getName().compareTo(c2.getName()) :
                            c2.getName().compareTo(c1.getName());
                }
                // Sort start date column
                else if (column.equals("startDateColumn")) {
                    return ascending ?
                            c1.getStartDate().compareTo(c2.getStartDate()) :
                            c2.getStartDate().compareTo(c1.getStartDate());
                }
                // Sort end date column
                else if (column.equals("endDateColumn")) {
                    return ascending ?
                            c1.getEndDate().compareTo(c2.getEndDate()) :
                            c2.getEndDate().compareTo(c1.getEndDate());
                }
                // Sort status column (based on the value, not the label)
                else if (column.equals("statusColumn")) {
                    if (ascending) {
                        if (c1.getStatus() > c2.getStatus())
                            return 1;
                        else if (c1.getStatus() < c2.getStatus())
                            return -1;
                        else
                            return 0;
                    } else {
                        if (c1.getStatus() > c2.getStatus())
                            return -1;
                        else if (c1.getStatus() < c2.getStatus())
                            return 1;
                        else
                            return 0;
                    }
                }
                // Sort the percent complete column
                else if (column.equals("percentColumn")) {
                    if (ascending) {
                        if (c1.getPercentComplete() > c2.getPercentComplete())
                            return 1;
                        else
                        if (c1.getPercentComplete() < c2.getPercentComplete())
                            return -1;
                        else
                            return 0;
                    } else {
                        if (c1.getPercentComplete() > c2.getPercentComplete())
                            return -1;
                        else
                        if (c1.getPercentComplete() < c2.getPercentComplete())
                            return 1;
                        else
                            return 0;
                    }
                } else
                    return 0;
            }
        };

        // Perform the actual sort using the above comparator
        Collections.sort(taskList, comparator);
    }

    /**
     * Is the default sort direction for the given column "ascending" ?
     */
    protected boolean isDefaultAscending(String sortColumn) {
        return isAscending();
    }
}
