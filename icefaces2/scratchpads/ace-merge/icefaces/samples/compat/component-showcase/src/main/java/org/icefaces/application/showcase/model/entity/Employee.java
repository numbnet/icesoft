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

package org.icefaces.application.showcase.model.entity;

/**
 * <p>The Person class is used in many of the examples to show simple data
 * interactions in a business context.</p>
 *
 * @since 1.0
 */
public class Employee extends Person {

    public static final String DEPARTMENT_NAME_COLUMN = "departmentName";
    public static final String SUB_DEPARTMENT_NAME_COLUMN = "subDepartmentName";
    public static final String ID_COLUMN = "employeeId";

    // department names
    private String departmentName;
    private String subDepartmentName;
    // employee id
    protected int id;

    public Employee(){
        
    }

    public Employee(int id, String departmentName, String subDepartmentName,
                    String firstName, String lastName, String phone ) {
        super(firstName, lastName, phone);
        this.departmentName = departmentName;
        this.subDepartmentName = subDepartmentName;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSubDepartmentName() {
        return subDepartmentName;
    }

    public void setSubDepartmentName(String subDepartmentName) {
        this.subDepartmentName = subDepartmentName;
    }
}
