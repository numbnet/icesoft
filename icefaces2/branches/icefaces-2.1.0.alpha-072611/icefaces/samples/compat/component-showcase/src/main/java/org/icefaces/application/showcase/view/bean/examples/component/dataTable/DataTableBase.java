/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.application.showcase.view.bean.examples.component.dataTable;

import org.icefaces.application.showcase.view.bean.BaseBean;
import org.icefaces.application.showcase.model.service.EmployeeService;
import org.icefaces.application.showcase.model.entity.Employee;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.NoneScoped;

/**
 * <p>The class DataTableBase is the base implementation for all DataTable
 * related examples.  This class should be extended for any dataTable example
 * to insure that the example has easy access to common example data.</p>
 *
 * @since 1.7
 */
@ManagedBean(eager=true)
//@NoneScoped
public class DataTableBase extends BaseBean {

    // mock service that retreives employee data
	@ManagedProperty(value="#{employeeServiceImpl}")
    protected EmployeeService employeeService;

    // internal list of retreived records.
    protected ArrayList employees;

    protected void init() {
        // build employee list form employee service.
        employees = employeeService.getEmployees(50, true,
                Employee.DEPARTMENT_NAME_COLUMN);
    }

    /**
     * Gets list of employee records retrieved from service layer.
     *
     * @return list of employees from service layer
     */
    public ArrayList getEmployees() {
        return employees;
    }

    /**
     * Sets the EmployeService reference.  Once the service has been set the
     * init method is called on this class to get the initial data for the
     * example.
     *
     * @param employeeService set the employeeService reference at which time
     *                        the init method is called on the class.
     */
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
        init();
    }
}