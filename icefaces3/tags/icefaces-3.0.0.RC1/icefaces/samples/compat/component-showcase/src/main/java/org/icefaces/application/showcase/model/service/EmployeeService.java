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

package org.icefaces.application.showcase.model.service;

import java.util.ArrayList;

/**
 * <p>Simple Employee Service inteface.  This mock service layer is intended to 
 * make our dataTable and other record based examples behave like real world
 * applications.<p>
 *
 * @since 1.7
 */
public interface EmployeeService {

    /**
     * Gets a list of Employee records that matches the supplied criteria.
     *
     * @param listSize number of records that will be retreived from service layer
     * @param isDescending true indicates a descending order; otherwise, descending
     * @param sortColumn column name set will be sorted on.
     * @return list of Employee objects. 
     */
    public ArrayList getEmployees(int listSize, boolean isDescending,
                                            final String sortColumn);

    /**
     * Gets a list of Employee records that matches the supplied criteria.
     *
     * @param listSize number of records that will be retreived from service layer
     * @return list of Employee objects.
     */
    public ArrayList getEmployees(int listSize);
}
