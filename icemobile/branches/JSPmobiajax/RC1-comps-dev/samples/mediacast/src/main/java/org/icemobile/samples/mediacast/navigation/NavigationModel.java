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

package org.icemobile.samples.mediacast.navigation;


import org.icefaces.application.PushRenderer;
import org.icemobile.samples.mediacast.MediaController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Stack;

/**
 * Model for a mobile navigation.  Composed of a stack of the navigation
 * hierarchy of the app.  Navigation in mobile application is generally linear
 * with no branching.  There are branch exception around application errors
 * which might corrupt the navigation state.
 */
@ManagedBean(name = NavigationModel.BEAN_NAME)
@SessionScoped
public class NavigationModel implements Serializable {

    public static final String BEAN_NAME = "navigationModel";

    public static final Destination DESTINATION_HOME =
            new Destination("1", "Mediacast", "home", "/WEB-INF/includes/content/home.xhtml", true);
    public static final Destination DESTINATION_GALLERY =
            new Destination("2", "Gallery", "Gallery", "/WEB-INF/includes/content/gallery.xhtml");
    public static final Destination DESTINATION_VIEWER =
            new Destination("3", "Viewer", "Viewer", "/WEB-INF/includes/content/media-viewer.xhtml");


    private static HashMap<String, Destination> destinationMap =
            new HashMap<String, Destination>(2);

    static {
        destinationMap.put(DESTINATION_HOME.getKey(), DESTINATION_HOME);
        destinationMap.put(DESTINATION_GALLERY.getKey(), DESTINATION_GALLERY);
        destinationMap.put(DESTINATION_VIEWER.getKey(), DESTINATION_VIEWER);
    }

    private Stack<Destination> navigationStack;

    private Destination backDestination;

    public NavigationModel() {
        navigationStack = new Stack<Destination>();
        navigationStack.push(DESTINATION_HOME);
        // add the current view the session group
        PushRenderer.addCurrentSession(MediaController.RENDER_GROUP);
    }

    public void goForward(String key) {
        backDestination = navigationStack.peek();
        navigationStack.push(destinationMap.get(key));
    }

    public void goBack() {
        navigationStack.pop();
        if (navigationStack.size() > 1) {
            backDestination = navigationStack.get(navigationStack.size() - 2);
        }
    }

    public Destination getCurrentDestination() {
        return navigationStack.peek();
    }

    public Destination getBackDestination() {
        return backDestination;
    }

    public Destination getDestinationHome() {
        return DESTINATION_HOME;
    }

    public Destination getDestinationGallery() {
        return DESTINATION_GALLERY;
    }

    public Destination getDestinationViewer() {
        return DESTINATION_VIEWER;
    }
}
