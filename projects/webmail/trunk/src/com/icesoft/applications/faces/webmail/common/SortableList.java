/**
 * Copyright (C) 2005, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.common;

/**
 * The SortableList class is a utility class used by the  data table
 * paginator example.
 *
 * @since 0.3.0
 */
public abstract class SortableList {

    protected String sort;
    protected boolean ascending;

    protected String oldSort;
    protected boolean oldAscending;

    public SortableList() {

    }

    protected SortableList(String defaultSortColumn) {
        sort = defaultSortColumn;
        ascending = isDefaultAscending(defaultSortColumn);
        oldSort = sort;
        // disorder so that we can get a sort the first load.   
        oldAscending = !ascending;
    }

    /**
     * Sort the list.
     */
    protected abstract void sort(String column, boolean ascending);

    /**
     * Is the default sort direction for the given column "ascending" ?
     */
    protected abstract boolean isDefaultAscending(String sortColumn);

//    /**
//     * Sort the given column
//     *
//     * @param sortColumn column to sort
//     */
//    public void sort(String sortColumn) {
//        if (sortColumn == null) {
//            throw new IllegalArgumentException("Argument sortColumn must not be null.");
//        }
//
//        if (sort.equals(sortColumn)) {
//            //current sort equals new sortColumn -> reverse sort order
//            ascending = !ascending;
//        } else {
//            //sort new column in default direction
//            sort = sortColumn;
//            ascending = isDefaultAscending(sort);
//        }
//
//        sort(sort, ascending);
//    }

    /**
     * Gets the sort column.
     *
     * @return column to sort
     */
    public String getSort() {
        return sort;
    }

    /**
     * Sets the sort column
     *
     * @param sort column to sort
     */
    public void setSort(String sort) {
        oldSort = this.sort;
        this.sort = sort;
    }

    /**
     * Is the sort ascending.
     *
     * @return true if the ascending sort otherwise false.
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * Set sort type.
     *
     * @param ascending true for ascending sort, false for desending sort.
     */
    public void setAscending(boolean ascending) {
        oldAscending = this.ascending;
        this.ascending = ascending;
    }
}