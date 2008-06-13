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
@Name("sortHeaderModel")
public class SeamSortHeaderModel extends SeamResizableColumnBean{
	private static final String EJBQL = "select e from Employee e";
    // table of person data

    private boolean descending;
    private String columnName;
    private String orderString;
  

	public SeamSortHeaderModel(){
		/* need this for portlet version --otherwise already have LR conversation */
		if (!Manager.instance().isLongRunningConversation())
			Manager.instance().beginConversation();
        setEjbql(EJBQL);
    	descending = true;
    	columnName = "lastName";
    	orderString=columnName+" asc";
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
     * creates sorting criteria for query
     */
    public void sort(final String column, final boolean descending) {
   //     this.refresh();   	
    	this.columnName = column;
    	this.descending = descending;
    	if (descending)this.orderString =columnName+" "+"asc";
    	else this.orderString=columnName+" "+"desc";
    	//redo query   
        this.buildSortedList();
 
    }


	public String getColumnName() {
		return columnName;
	}

	public void setcolumnName(String sort) {
		if (!sort.equals(this.columnName)){
			this.columnName = sort;
			sort(this.columnName,descending);
		}
	}

	public boolean isdescending() {
		return descending;
	}

	public void setdescending(boolean descending) {
		if (descending !=this.descending){
			this.descending = descending;
			sort(this.columnName, this.descending);
		}
	}

	@Destroy
	public void destroy(){

	}

}