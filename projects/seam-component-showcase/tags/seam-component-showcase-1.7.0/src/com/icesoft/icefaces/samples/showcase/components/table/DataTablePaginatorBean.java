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

package com.icesoft.icefaces.samples.showcase.components.table;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import static org.jboss.seam.ScopeType.PAGE;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Manager;
import org.jboss.seam.framework.EntityQuery;

import com.icesoft.icefaces.samples.showcase.common.Person;

import java.io.Serializable;

/**
 * <p>The DataTablePaginatorBean Class is a backing bean for the
 * dataTablePaginator showcase demonstration as well as 
 * commandSortHeader. </p>
 *
 * @since 0.3.0
 */
@Scope(ScopeType.CONVERSATION)
@Name("scrollerList")
public class DataTablePaginatorBean extends EntityQuery{

    // table of person data

	private static final String EJBQL = "select p from Person p";
	
    private boolean ascending;
    private String sort;
    private String orderString;
    	
    private String paginatorLayout = "hor";
    
    private Person person = new Person();

	@Override
	public Integer getMaxResults() {
		return 25;
	}
	
    @Override
    public String getEjbql() {
        return "select person from Person person";
    }

	
	public Person getPerson() {
		return person;
	}
	
	public DataTablePaginatorBean(){
		/* need this for portlet version --otherwise already have LR conversation */
		if (!Manager.instance().isLongRunningConversation())
			Manager.instance().beginConversation();
        setEjbql(EJBQL);
    	ascending = true;
    	sort = "lastName";
    	orderString=sort+" asc";
    	buildSortedList();
	}
	
    /**
     *
     */
 
	public void buildSortedList(){
        setEjbql(EJBQL);
        setOrder(orderString);    
    }
 
    /**
     * Gets the data paginator layout.
     *
     * @return the data paginator layout
     */
    public String getPaginatorLayout() {
        return paginatorLayout;
    }

    /**
     * Sets the data paginator layout.
     *
     * @param paginatorLayout the data paginator layout
     */
    public void setPaginatorLayout(String paginatorLayout) {
        this.paginatorLayout = paginatorLayout;
    }

    /**
     * Determines if the data paginator layout is vertical.
     *
     * @return the status of the data paginator layout
     */
    public boolean isVertical() {
        return (paginatorLayout.equalsIgnoreCase("ver"));
    }


    
    /**
     * creates sorting criteria for query
     */
    public void sort(final String column, final boolean ascending) {
   //     this.refresh();   	
    	this.sort = column;
    	this.ascending = ascending;
    	if (ascending)this.orderString =sort+" "+"asc";
    	else this.orderString=sort+" "+"desc";
    	//redo query   
        this.buildSortedList();
 
    }


	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		if (!sort.equals(this.sort)){
			this.sort = sort;
			this.sort(this.sort,ascending);
		}
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		if (ascending !=this.ascending){
			this.ascending = ascending;
			this.sort(this.sort, this.ascending);
		}
	}

	@Destroy
	public void destroy(){

	}

}