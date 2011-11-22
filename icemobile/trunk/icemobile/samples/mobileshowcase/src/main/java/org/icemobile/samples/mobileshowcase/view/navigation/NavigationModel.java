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

import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 */
@org.icemobile.samples.mobileshowcase.view.metadata.annotation.Destination(
        title = "content.home.title",
        titleBack = "content.home.back.title",
        contentPath = "/WEB-INF/includes/navigation/menu.xhtml"
)

@ManagedBean
@SessionScoped
public class NavigationModel implements Serializable {

    // stack of currently visited panels.
    private Stack<Destination> navigationStack;

    // the previously visited page if any.
    private Destination backDestination;

    // example resource path to load
    private String currentExampleResource;

    // reference to current example bean
    private ExampleImpl currentExampleBean;

    // Static list of destinations not defined by annotations.
    public static final Destination DESTINATION_HOME =
            new Destination(
                "content.home.title", "content.home.title", "content.home.title",
                "/WEB-INF/includes/navigation/splash.xhtml");

    public static final Destination DESTINATION_MENU =
            new Destination(
                "content.home.back.title", "content.home.back.title", "content.home.back.title",
                "/WEB-INF/includes/navigation/menu.xhtml");

    private static HashMap<String, Destination> destinationMap =
            new HashMap<String, Destination>(2);

    static {
        destinationMap.put(DESTINATION_HOME.toString(), DESTINATION_HOME);
    }

    public NavigationModel() {
        navigationStack = new Stack<Destination>();
        if (getClass().isAnnotationPresent(
                org.icemobile.samples.mobileshowcase.view.metadata.annotation.Destination.class)) {
            org.icemobile.samples.mobileshowcase.view.metadata.annotation.Destination destinationAnnotation =
                    getClass().getAnnotation(org.icemobile.samples.mobileshowcase.view.metadata.annotation.Destination.class);
            navigationStack.add(new Destination(
                    destinationAnnotation.title(),
                    destinationAnnotation.titleExt(),
                    destinationAnnotation.titleBack(),
                    destinationAnnotation.contentPath()));
        }
    }

    /**
     * Navigated to a known navigation location in the panel stack.  If the
     * previous destination does not equal the next destination it is added
     * to the navigation stack.
     *
     * @param newDestination new destination to navigate to.
     */
    public void goForward(Destination newDestination) {
        backDestination = navigationStack.peek();
        if (!newDestination.equals(backDestination)) {
            navigationStack.push(newDestination);
        }
    }

    public void goForward(String key) {
        backDestination = navigationStack.peek();
        navigationStack.push(destinationMap.get(key));
    }

    /**
     * Navigates to the previously viewed
     */
    public void goBack() {
        navigationStack.pop();
        if (navigationStack.size() > 1) {
            backDestination = navigationStack.get(navigationStack.size() - 2);
        } else {
            backDestination = navigationStack.firstElement();
        }
    }

    public boolean canGoBack() {
        return navigationStack != null && navigationStack.size() > 1;
    }

    public Destination getCurrentSmallViewDestination() {
        return navigationStack.peek();
    }

    public Destination getCurrentLargeViewDestination() {

        Destination currentDestination = navigationStack.peek();
        // check for handheld content load
        if (currentDestination.getContentPath().equals(
                DESTINATION_MENU.getContentPath())){
            return DESTINATION_HOME;
        }
        return navigationStack.peek();
    }

    public Destination getBackDestination() {
        return backDestination;
    }

    public String getCurrentExampleResource() {
        return currentExampleResource;
    }

    public void setCurrentExampleResource(String currentExampleResource) {
        this.currentExampleResource = currentExampleResource;
    }

    public ExampleImpl getCurrentExampleBean() {
        return currentExampleBean;
    }

    public void setCurrentExampleBean(ExampleImpl currentExampleBean) {
        this.currentExampleBean = currentExampleBean;
    }

    public Destination getDestinationHome() {
        return DESTINATION_HOME;
    }
}
