/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
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
