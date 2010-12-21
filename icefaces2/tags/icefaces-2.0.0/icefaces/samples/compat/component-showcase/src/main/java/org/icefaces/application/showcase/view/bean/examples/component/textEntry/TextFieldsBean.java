/*
 * Version: MPL 1.1
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.application.showcase.view.bean.examples.component.textEntry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.application.showcase.view.bean.BaseBean;

/**
 * <p>The TextFieldsBean class is the backing bean for the Text Entry
 * demonstration. It is used to store the values of the input fields.</p>
 */
@ManagedBean(name = "textFields")
@ViewScoped
public class TextFieldsBean extends BaseBean {
    /**
     * The different kinds of text input fields.
     */
    private String name;
    private String password;
    private String comments;

    /**
     * Gets the name property.
     *
     * @return value of name property
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name property
     *
     * @param newValue new value of the name property
     */
    public void setName(String newValue) {
        name = newValue;
    }

    /**
     * Gets the password property.
     *
     * @return value of the password property
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password property.
     *
     * @param newValue new value of the password property
     */
    public void setPassword(String newValue) {
        password = newValue;
    }

    /**
     * Gets the comments property.
     *
     * @return value of the comments property
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments property.
     *
     * @param newValue new value of the comments property
     */
    public void setComments(String newValue) {
        comments = newValue;
    }
    
}