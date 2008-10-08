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



import java.util.List;

import org.jboss.seam.ScopeType;

import org.jboss.seam.log.*;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Events;
import org.icefaces.application.showcase.view.bean.examples.component.dataPaginator.DataScrollingModel;


import com.icesoft.faces.component.datapaginator.DataPaginator;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import javax.faces.event.ValueChangeEvent;
/**
 * <p>The DataTablePaginatorBean Class is a backing bean for the
 * dataTablePaginator showcase demonstration as well as 
 * commandSortHeader. </p>
 *
 * @since 0.3.0
 */
@Scope(ScopeType.PAGE)
@Name("dataScrollingModel")
public class SeamDataScrollingModel implements Serializable{
	@Logger Log log;
	
	@In("#{employeesListAll.resultList}")
	private List employees;
	
	@In(value="dataPaginator",required=false)
	DataPaginator dataPaginatorBinding;
	
	   /**
     * dataTable will have no pagging or scrolling enabled.
     */
    public static final String NO_SCROLLING = "none";
    /**
     * dataTable will have scrolling enabled.
     */
    public static final String SCROLLING_SCROLLING = "scrolling";
    /**
     * dataTable will have paging enabled.
     */
    public static final String PAGINATOR_SCROLLING = "paging";

    // currently select scrolling state select by user.
    private String selectedDataScrollMode=PAGINATOR_SCROLLING;
    private static HashMap selectedDataScrollModes;

	
    @Create
	public void init(){
    	  if (employees==null)employees= new ArrayList();
	      selectedDataScrollMode = PAGINATOR_SCROLLING;

	      selectedDataScrollModes = new HashMap();

	        // default data table setting
	      selectedDataScrollModes.put(NO_SCROLLING,
	                new DataScrollMode(0, false, false));

	        // scrolling data table settings
	      selectedDataScrollModes.put(SCROLLING_SCROLLING,
	                new DataScrollMode(0, true, false));

	        // paging data table settings
	      selectedDataScrollModes.put(PAGINATOR_SCROLLING,
	                new DataScrollMode(9, false, true));
	}
 

 
    public String getSelectedDataScrollMode() {
        return selectedDataScrollMode;
    }

    public void setSelectedDataScrollMode(String selectedDataScrollMode) {
        this.selectedDataScrollMode = selectedDataScrollMode;
    }

    public HashMap getSelectedDataScrollModes() {
        return selectedDataScrollModes;
    }
    
    public List getEmployees(){
    	return this.employees;
    }

	@Destroy
	public void destroy(){
		log.info("destroying datapaginator bean");
	}
	
   /**
    * Currently using component binding for resetting the datamodel to
    * first page of data
    * @param event
    */
	public void dataModelChangeListener(ValueChangeEvent event){
		
       String oldPagingValue = (String)event.getOldValue();
       String newPagingValue= (String)event.getNewValue();

       if (newPagingValue.equals("scrolling")){
    	   setSelectedDataScrollMode(SCROLLING_SCROLLING); 
    	   this.dataPaginatorBinding.gotoFirstPage();
       }
       if (newPagingValue.equals("none")){
    	   selectedDataScrollMode=NO_SCROLLING;
    	   this.dataPaginatorBinding.gotoFirstPage();
       }
       else selectedDataScrollMode=PAGINATOR_SCROLLING;
    }
	

    /**
     * Utility method for storing the states of the different scrolling modes.
     * This class is used alone with standard JSF Map notation to retreive
     * specific properties.
     */
    public class DataScrollMode {
        // number of rows to display when paging, default value (0) shows
        // all records.
        private int rows;
        // scrolling enabled
        private boolean scrollingEnabled;
        // paging enabled.
        private boolean pagingEnabled;

        public DataScrollMode(int rows, boolean scrollingEnabled,
                              boolean pagingEnabled) {
            this.rows = rows;
            this.scrollingEnabled = scrollingEnabled;
            this.pagingEnabled = pagingEnabled;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public boolean isScrollingEnabled() {
            return scrollingEnabled;
        }

        public void setScrollingEnabled(boolean scrollingEnabled) {
            this.scrollingEnabled = scrollingEnabled;
        }

        public boolean isPagingEnabled() {
            return pagingEnabled;
        }

        public void setPagingEnabled(boolean pagingEnabled) {
            this.pagingEnabled = pagingEnabled;
        }

    } 
 
}