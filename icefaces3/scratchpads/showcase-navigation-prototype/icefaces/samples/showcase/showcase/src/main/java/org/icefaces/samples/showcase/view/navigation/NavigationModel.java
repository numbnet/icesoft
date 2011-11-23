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

package org.icefaces.samples.showcase.view.navigation;

import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.metadata.context.Menu;
import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.util.EnvUtils;

//import javax.faces.bean.ViewScoped; - revision 29330 version
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name = NavigationModel.BEAN_NAME)
//@ViewScoped - revision 29330 version
@CustomScoped(value = "#{window}")
public class NavigationModel implements Serializable {

    public static final String BEAN_NAME = "navigationModel";
    
    // TODO NAVIGATION controller should define this from meta data.
    public static final String DEFAULT_MENU = AceMenu.BEAN_NAME;
    public static final String GROUP_PARAM = "grp";
    public static final String EXAMPLE_PARAM = "exp";

    public static final String GROUP_KEY = "org.icefaces.samples.showcase.group";
    public static final String EXAMPLE_KEY = "org.icefaces.samples.showcase.example";

    // references to resolved menu and examples.
    private Menu currentComponentGroup;
    private ComponentExampleImpl currentComponentExample;
    // param for url
    private String componentGroup;
    private String componentExample;
    
    //if set to true Source code panel in UI will stay expanded
    private boolean sourceCodeToggleStatus = false;

    public NavigationModel() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        //If we are running in a portlet, we need to set the default group and example as declared
        //in the portlet.xml init parameters.
        if(EnvUtils.instanceofPortletRequest(ec.getRequest())){

            //Get the params for the group and the example we should start with.  The portlet bridge
            //makes these accessible via standard JSF API calls.
            String group = ec.getInitParameter(GROUP_KEY);
            String example = ec.getInitParameter(EXAMPLE_KEY);

            //Set the group(menu) we should be displaying
            setCurrentComponentGroup((Menu) FacesUtils.getManagedBean(NavigationModel.DEFAULT_MENU));
            setComponentGroup(NavigationModel.DEFAULT_MENU);

            //Determine the default example in the group and set that as well
//            String beanName = getCurrentComponentGroup().getDefaultExample().getExampleBeanName();
            ComponentExampleImpl cei = (ComponentExampleImpl) FacesUtils.getManagedBean(example);
            setCurrentComponentExample(cei);
            setComponentExample(example);
        }
    }
    
    
//    IMPLEMENTATION OF THE ACCORDION PANEL SUITE MENU METHODS
//    DO NOT REMOVE ! UNDER CONSTRUCTION    
//    public void handlePaneChange(AccordionPaneChangeEvent event)
//    {
//          String tabId = event.getTab().getId();
//          System.out.println("PANE CHANGE ID:"+tabId);
////        //Invoke redirect
////        FacesContext context = FacesContext.getCurrentInstance();
////        ExternalContext extContext = context.getExternalContext();
////        String viewId = getView();
////        try {
////            
////                String charEncoding = extContext.getRequestCharacterEncoding();
////                
////                String groupParamName = URLEncoder.encode(GROUP_PARAM, charEncoding);
////                String  groupParamValue = URLEncoder.encode(getGroupParamValue(event.getTab()), charEncoding);
////                String exampleParamName = URLEncoder.encode(EXAMPLE_PARAM, charEncoding);
////                String  exampleParamValue = URLEncoder.encode(getExampleParamValue(event.getTab()), charEncoding);
////                
////                viewId = extContext.getRequestContextPath() + viewId + '?' + groupParamName
////                                + "=" + groupParamValue + "&"+exampleParamName+ "=" + exampleParamValue;
////                
////                String urlLink = context.getExternalContext().encodeActionURL(viewId);
////                extContext.redirect(urlLink);
////        } catch (IOException e) {
////                extContext.log(getClass().getName() + ".invokeRedirect", e);
////        }
////        
//    }
//    
//    private String getView() {
//        String viewId = "/showcase.jsf"; // or look this up somewhere
//        return viewId;
//    }
//    private String getGroupParamValue(AccordionPane tab)
//    {
//        Object param = tab.getAttributes().get("groupParamValue");
//        return null;
//    }
//    private String getExampleParamValue(AccordionPane tab)
//    {
//        Object param = tab.getAttributes().get("exampleParamValue");
//        return null;
//    }

    //    public void viewEvent() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        Map map = context.getExternalContext().getRequestParameterMap();
//        componentGroup = (String) map.get("grp");
//        componentExample = (String) map.get("exp");
//    }

//    //    @PostConstruct
//    public void initMetaData() {
//        // assign default navigation
//        currentComponentGroup = (AceMenu) FacesUtils.getManagedBean(DEFAULT_MENU);
//        String beanName = currentComponentGroup.getDefaultExample().getExampleBeanName();
//        currentComponentExample = (ComponentExampleImpl) FacesUtils.getManagedBean(beanName);
//    }
//    IMPLEMENTATION OF THE ACCORDION PANEL SUITE MENU END

    public Menu getCurrentComponentGroup() {
        return currentComponentGroup;
    }

    public void setCurrentComponentGroup(Menu currentComponentGroup) {
        this.currentComponentGroup = currentComponentGroup;
    }

    public ComponentExampleImpl getCurrentComponentExample() {
        return currentComponentExample;
    }

    public void setCurrentComponentExample(ComponentExampleImpl currentComponentExample) {
        this.currentComponentExample = currentComponentExample;
    }

    public String getComponentGroup() {
        return componentGroup;
    }

    public void setComponentGroup(String componentGroup) {
        this.componentGroup = componentGroup;
    }

    public String getComponentExample() {
        return componentExample;
    }

    public void setComponentExample(String componentExample) {
        this.componentExample = componentExample;
    }

    public boolean isSourceCodeToggleStatus() {
        return sourceCodeToggleStatus;
    }

    public void setSourceCodeToggleStatus(boolean sourceCodeToggleStatus) {
        this.sourceCodeToggleStatus = sourceCodeToggleStatus;
    }
}
