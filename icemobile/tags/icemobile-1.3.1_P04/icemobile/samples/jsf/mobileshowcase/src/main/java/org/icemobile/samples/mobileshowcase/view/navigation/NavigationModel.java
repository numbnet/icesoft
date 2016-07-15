/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icemobile.samples.mobileshowcase.view.navigation;

import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icefaces.util.JavaScriptRunner;
import org.icemobile.samples.mobileshowcase.util.FacesUtils;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Stack;
import java.util.Map;

/**
 * Navigation stack test for using a content Stack component.  Normally only the
 * selectedPanel instance variable would be needed to manipulate the selected
 * panel in the panelStack.  But in this example we want to also keep have
 * the notion of a back button on a small display which requires us to also
 * keep a stack of recently visited panel id's.
 */
@ManagedBean(name = NavigationModel.BEAN_NAME)
@SessionScoped
public class NavigationModel implements Serializable {

    public static final String BEAN_NAME = "mobiNavigationModel";

    // currently selected panel and stack to keep track of history
    private String selectedPanel = "splash";
    private Stack<String> history = new Stack<String>();

    // Destination stores title information for display purposes.
    private Destination selectedDestination = DESTINATION_SPLASH;
    // Selected example represents the currently selected example bean.
    private ExampleImpl selectedExample;
    // home destination, if stack is empty
    public static final Destination DESTINATION_SPLASH =
            new Destination(
                    "content.home.title", "content.home.title", "content.home.title");
    public static final Destination DESTINATION_MENU =
            new Destination("content.menu.title", "content.menu.title", "content.menu.title");

    public String navigateToParam()  {
        Map<String,String> params = 
            FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
     
        String panelID = params.get("panelID");
        String beanName = params.get("beanName");
        String redirect = params.get("redirect");
        
        return navigateTo(panelID, beanName, "true".equalsIgnoreCase(redirect));
    }

    /**
     * Navigate to the selectedPanelId as well as save a reference to the current
     * Example bean defined by beanName.
     *
     * @param selectedPanelId panelId to to make visible
     * @param beanName        bean name of the currently selected example
     * @return always null, no jsf navigation.
     */
    public String navigateTo(String selectedPanelId, String beanName, boolean redirect) {
        // add previous location to history.
        history.push(this.selectedPanel);
        this.selectedPanel = selectedPanelId;
        // assign the selected example
        selectedExample = (ExampleImpl) FacesUtils.getManagedBean(beanName);
        if (selectedExample != null) {
            selectedDestination = selectedExample.getDestination();
        } else {
            selectedDestination = DESTINATION_SPLASH;
        }
        if( redirect ){
            StringBuffer val = new StringBuffer("showcase?faces-redirect=true&cache="+System.currentTimeMillis());
            if (MobiJSFUtils.getClientDescriptor().isBlackBerry10OS()) {
                val.append( "&random=").append( Integer.toString( (int) (Math.random() * 1000)));
            }
            return val.toString();
        }
        else{
            scrollToTopOfPage();
            return null;
        }
        
    }

    /**
     * Checks to see if back navigation is possible.
     *
     * @return true if back navigation is possible, otherwise false.
     */
    public boolean isCanGoBack() {
        return !history.isEmpty();
    }

    /**
     * Checks to see if the history stack has a state that can be restored.  If
     * true the selected panel is set accordingly.
     *
     * @return null, no JSF navigation.
     */
    public String goBack() {
        if (!history.isEmpty()) {
            selectedPanel = history.pop();
        }
        if (history.isEmpty()) {
            selectedDestination = DESTINATION_SPLASH;
        }
        scrollToTopOfPage();
        return null;
    }
    
    public String goToHome(){
        selectedPanel = "splash";
        history.clear();
        scrollToTopOfPage();
        return null;
    }
    
    public String goToMenu(){
        selectedPanel = "menu";
        scrollToTopOfPage();
        return null;
    }

    /**
     * Gets the currently selected panel.
     *
     * @return selected panel.
     */
    public String getSelectedPanel() {
        return selectedPanel;
    }

    public void setSelectedPanel(String selectedPanel) {
        this.selectedPanel = selectedPanel;
    }

    /**
     * Gets the currently selected destination, if none then the home destination
     * is returned by default.
     *
     * @return gets the selected destination, if null, the DESTINATION_HOME
     *         constant is resturned.
     */
    public Destination getSelectedDestination() {
        if (selectedDestination != null) {
            return selectedDestination;
        } else {
            return DESTINATION_SPLASH;
        }
    }

    public ExampleImpl getSelectedExample() {
        return selectedExample;
    }

    // make sure we scroll to the top of the page.
    private void scrollToTopOfPage() {
        JavaScriptRunner.runScript(FacesContext.getCurrentInstance(), "javascript:scroll(0,0);");
    }
}
