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

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Map;

@ManagedBean
@ApplicationScoped
public class NavigationController implements Serializable {
    
    /**
     * Approach to navigation that will grab our params via the request map
     * This is meant to be used with a full page refresh as metadata
     */
    public void navigate() {
        Map<String,String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        //call to NavigationModelBean constructor
        navigate(map.get(NavigationModel.GROUP_PARAM),
                 map.get(NavigationModel.EXAMPLE_PARAM));
    }
    /**
      * Approach to navigation that will try to grab f:attributes that were specified on a command component
      * This is meant to be used with a commandLink
      */
    public void navigate(ActionEvent event) {
        Object newGroup = FacesUtils.getFAttribute(event, NavigationModel.GROUP_PARAM);
        Object newExample = FacesUtils.getFAttribute(event, NavigationModel.EXAMPLE_PARAM);
        
        navigate(newGroup == null ? null : newGroup.toString(),
                 newExample == null ? null : newExample.toString());
    }
    
    /**
     * Main controller logic.
     */
    public void navigate(String newGroup, String newExample) {
        NavigationModel navigationModel = (NavigationModel)
                FacesUtils.getManagedBean(NavigationModel.BEAN_NAME);

        // If our new component group is valid then set it into the navigationModel
        if (newGroup != null) {
            navigationModel.setComponentGroup(newGroup);
        }
        
        // If our new component example is valid then set it into the navigationModel
        if (newExample != null) {
            navigationModel.setComponentExample(newExample);
        }
        else {
            // Reset the example if the group changed and the example was null
            // This happens if the overall group menu was selected
            // Doing this will ensure that the default component example for the current group is set
            if (newGroup != null) {
                navigationModel.setComponentExample(newExample);
            }
        }
        
        // Update our group and example to the latest
        newGroup = navigationModel.getComponentGroup();
        newExample = navigationModel.getComponentExample();
        
        // Check whether we need to load the default navigation (this portion is executed during application startup)
        if (newGroup == null && newExample == null) {
            loadDefaultNavigation(navigationModel);
        }
        // Otherwise we may just need to load the example
        else if (newGroup != null && newExample == null) {
            Menu menu = (Menu)FacesUtils.getManagedBean(newGroup);
            if (menu != null) {
                navigationModel.setCurrentComponentGroup(menu);
                ComponentExampleImpl compExample = (ComponentExampleImpl)
                        FacesUtils.getManagedBean(menu.getDefaultExample().getExampleBeanName());
                if (compExample != null) {
                    navigationModel.setCurrentComponentExample(compExample);
                }
            } else {
                loadDefaultNavigation(navigationModel);
            }
        // Otherwise check if the group/example is valid at all
        } else {
            Object tmpGroup = FacesUtils.getManagedBean(newGroup);
            Object tmpExample = FacesUtils.getManagedBean(newExample);
            if ((tmpGroup != null && tmpGroup instanceof Menu) &&
                    (tmpExample != null && tmpExample instanceof ComponentExampleImpl)) {
                ComponentExampleImpl currentExample = navigationModel.getCurrentComponentExample();
                ComponentExampleImpl setExample = (ComponentExampleImpl)tmpExample;
                
                // Determine if we need to fire the effect
                // This is necessary when the overall demo was changed, so basically when the package is different
                // Although checking against packages is not the desired approach, there isn't another option for matching beans
                //  because they have no built-in idea of a parent-child heirarchy
                if (currentExample != null) {
                    if (!setExample.getClass().getPackage().equals(currentExample.getClass().getPackage())) {
                        setExample.prepareEffect();
                    }
                }
                else {
                    setExample.prepareEffect();
                }
                
                // Apply the new group and example
                navigationModel.setCurrentComponentGroup((Menu)tmpGroup);
                navigationModel.setCurrentComponentExample(setExample);
            } else {
                loadDefaultNavigation(navigationModel);
            }
        }
        //String test = navigationModel.getCurrentComponentGroup().;
        //check if default page is a suite overview page
//        if(beanName.endsWith("SuiteOverview"))
//        {
//            navigationModel.setRenderSourceCodePanel(false);
//        }
        
    }

    private void loadDefaultNavigation(NavigationModel navigationModel) {
        // load default pages.
        navigationModel.setCurrentComponentGroup((Menu)
                FacesUtils.getManagedBean(NavigationModel.DEFAULT_MENU));
        navigationModel.setComponentGroup(NavigationModel.DEFAULT_MENU);

        String beanName = navigationModel.getCurrentComponentGroup()
                .getDefaultExample().getExampleBeanName();
        navigationModel.setCurrentComponentExample((ComponentExampleImpl)
                FacesUtils.getManagedBean(beanName));
        navigationModel.setComponentExample(beanName);
    }
    
    public static void refreshPage() {
        FacesContext context = FacesContext.getCurrentInstance();
        ViewHandler handler = context.getApplication().getViewHandler();
        String viewId = context.getViewRoot().getViewId();
        UIViewRoot root = handler.createView(context, viewId);
        
        root.setViewId(viewId);
        context.setViewRoot(root);
    }
    
    public static void reloadPage() {
        NavigationModel navigationModel = (NavigationModel)
                FacesUtils.getManagedBean(NavigationModel.BEAN_NAME);
                
        // Redirect to the current page with the current params
        loadPage("?" +
                 NavigationModel.GROUP_PARAM + "=" + navigationModel.getComponentGroup() +
                 "&" +
                 NavigationModel.EXAMPLE_PARAM + "=" + navigationModel.getComponentExample());
    }
    
    public static void loadPage(String page) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        }catch (Throwable e) {
        }
    }
}
