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

package org.icefaces.navigation;

import javax.faces.event.ActionEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.Map;
/**
 * <p>The NavigationBean class is responsible for storing the state of the
 * included dynamic content for display.  </p>
 */
@ManagedBean (name="navigation")
@SessionScoped
public class NavigationBean {

    // selected include contents.
    @ManagedProperty(value = "#{selectedIncludePath}")
    private String selectedIncludePath="content/splash.xhtml";

    @ManagedProperty(value="includeExample")
    private String includeExample;
    


	/**
     * Gets the currently selected content include path.
     *
     * @return currently selected content include path.
     */
    public String getSelectedIncludePath() {
        //check for a currently selected path to be ready for ui:include
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        String requestedPath = (String) map.get("includePath");
        if ((null != requestedPath) && (requestedPath.length() > 0))  {
            selectedIncludePath = requestedPath;    
        }else selectedIncludePath="content/splash.xhtml";
            return selectedIncludePath;
    }

    /**
     * Sets the selected content include path to the specified path.
     *
     * @param selectedIncludePath the specified content include path.
     */
    public void setSelectedIncludePath(String selectedIncludePath) {
        this.selectedIncludePath = selectedIncludePath;
    }

    /**
     * Action Listener for the changes the selected content path.
     *
     * @param event JSF Action Event.
     */
    public void navigationPathChange(ActionEvent event){

        // Retrieve content include path from the context.
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        selectedIncludePath = (String) map.get("includePath");
    }
    public String getIncludeExample() {
		return includeExample;
	}

	public void setIncludeExample(String includeExample) {
		this.includeExample = includeExample;
	}

}
