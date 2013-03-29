/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icemobile.samples.mobileshowcase.view.navigation;


import org.icefaces.util.JavaScriptRunner;
import org.icemobile.samples.mobileshowcase.util.FacesUtils;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Simple controller to manage the navigation stack.  One can go forward
 * or back in the stack one panel at a time. .  This is to facilitate the
 * mobile stack navigation model.
 * <p/>
 * tree dynamic insertion.
 */
@ManagedBean
@ApplicationScoped
public class NavigationController implements Serializable {

    public static final String NAV_DESTINATION = "destBean";

    /**
     * Navigate to the page specified by the request parameter
     * ParameterNames.NAV_DESTINATION which should have a value of a Navigation
     * objects key.  The new navigation panel is added to the stack.
     *
     * @return null, no jsf navigation string.
     */
    public String navigateToPage() {
        NavigationModel navigationModel = getNavigationModel();
        String beanName =
                FacesUtils.getRequestParameter(NAV_DESTINATION);
        ExampleImpl example = (ExampleImpl)
                FacesUtils.getManagedBean(beanName);

        // store the example bean reference so we can get more easily at the
        // the example resources.
        navigationModel.setCurrentExampleBean(example);
        // do the actual navigation.
        navigationModel.goForward(example.getDestination());

        scrollToTopOfPage();

        return null;
    }

    /**
     * Navigate to the page specified by the request parameter
     * ParameterNames.NAV_DESTINATION which in this case should be the
     * path to the content we want to load.
     *
     * @return null, no jsf navigation string.
     */
    public String navigateToExampleResource() {
        NavigationModel navigationModel = getNavigationModel();
        String resourcePath =
                FacesUtils.getRequestParameter(NAV_DESTINATION);

        // store the path in session scope so that our servlet can load the
        // resource file in source view.
        navigationModel.setCurrentExampleResource(resourcePath);

        Destination destination = new Destination(
                "menu.source.title", "menu.source.title", "menu.source.title",
                "/WEB-INF/includes/examples/example-resource.xhtml");

        navigationModel.goForward(destination);

        return null;
    }

    /**
     * Navigate to the page specified by the request parameter
     * ParameterNames.NAV_DESTINATION which should have a value of a Navigation
     * objects key.  The new navigation panel is added to the stack.
     *
     * @return "refresh-redirect", navigation rule to redirect and refresh page.
     */
    public String refreshTheme() {
        navigateToPage();
        scrollToTopOfPage();
        return "refresh-redirect";
    }

    /**
     * Navigate back on to the previously visited panel .
     *
     * @return null
     */
    public String navigateBack() {
        NavigationModel navigationModel = getNavigationModel();
        navigationModel.goBack();
        scrollToTopOfPage();
        return null;
    }

    public boolean getCanGoBack() {
        return getNavigationModel().canGoBack();
    }

    protected NavigationModel getNavigationModel() {
        return (NavigationModel) FacesUtils.getManagedBean("navigationModel");
    }

    public String getNavDestination() {
        return NAV_DESTINATION;
    }

    // make sure we scroll to the top of the page.
    private void scrollToTopOfPage(){
        JavaScriptRunner.runScript(FacesContext.getCurrentInstance(), "javascript:scroll(0,0);");
    }
}
