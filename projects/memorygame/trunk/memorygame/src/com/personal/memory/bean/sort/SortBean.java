package com.personal.memory.bean.sort;

public abstract class SortBean {
    protected String sortColumnName;
    protected boolean ascending = true;
    protected String oldSort;
    protected boolean oldAscending;
    
    protected SortBean(String defaultSortColumn) {
        sortColumnName = defaultSortColumn;
        ascending = isDefaultAscending(defaultSortColumn);
        oldSort = sortColumnName;
        oldAscending = !ascending;
    }
    
    public String getSortColumnName() {
        return sortColumnName;
    }
    
    public void setSortColumnName(String sortColumnName) {
        oldSort = this.sortColumnName;
        
        this.sortColumnName = sortColumnName;
    }
    
    public boolean isAscending() {
        return ascending;
    }
    
    public void setAscending(boolean ascending) {
        oldAscending = this.ascending;
        
        this.ascending = ascending;
    }
    
    /**
     * Method to sort the contents of the list
     */
    protected abstract void sort();
    
    /**
     * Method to determine if the default column should be ascending or descending
     *
     * @param defaultSortColumn to check
     * @return true to use an ascending sort
     */
    protected abstract boolean isDefaultAscending(String defaultSortColumn);
}