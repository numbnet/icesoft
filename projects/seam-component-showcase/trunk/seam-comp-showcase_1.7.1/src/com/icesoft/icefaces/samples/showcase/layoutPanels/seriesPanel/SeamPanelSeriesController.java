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

package com.icesoft.icefaces.samples.showcase.layoutPanels.seriesPanel;


import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.ScopeType;
import java.util.List;

import javax.faces.event.ActionEvent;

/**
 * <p>The Cashier Class is used to back the List (panelSeries) Component
 * example.</p>
 */
@Scope(ScopeType.PAGE)
@Name("panelSeriesController")
public class SeamPanelSeriesController extends EntityQuery{
	private static final String EJBQL = "select e from Employee e";
    // start off with 3 employees.
	private int employeeCount = 3;
	
	@Override
	public Integer getMaxResults() {
		return employeeCount;
	}
	
	@Override
	public String getEjbql(){
		return EJBQL;
	}

	public List getEmployees(){
		return this.getResultList();
	}
	

    /**
     * Adds a new employee to the list of employees.
     * @param event jsf action event
     */
    public void addEmployee(ActionEvent event){
        employeeCount++;
    }

    /**
     * Removes a new employee to the list of employees.
     * @param event jsf action event
     */
    public void removeEmployee(ActionEvent event){
        employeeCount--;
    }

    public int getEmployeeCount(){
        return employeeCount;
    }

}