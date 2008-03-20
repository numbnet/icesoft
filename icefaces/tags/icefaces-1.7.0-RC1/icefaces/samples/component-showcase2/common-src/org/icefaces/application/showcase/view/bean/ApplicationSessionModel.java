/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package org.icefaces.application.showcase.view.bean;

import org.icefaces.application.showcase.view.jaxb.Node;
import org.icefaces.application.showcase.util.FacesUtils;

import java.util.HashMap;

/**
 * <p>The purpose of the bean is to store user session data for the navigation
 * and content presentation layer of the application.  The intention is
 * to keep this class as lean as possible and thus only persist between
 * requests the absolute minimum amount of data. </p>
 *
 * @since 1.7
 */
public class ApplicationSessionModel {

    // current node, and thus content the user is viewing
    private Node currentNode;

    // navigation model for each sub tree
    private NavigationTreeFactory navigationTreeFactory;

    // map of tab content visited by users, lazy loaded
    private HashMap<Node, TabState> tabContents;

    public ApplicationSessionModel() {
        navigationTreeFactory = new NavigationTreeFactory();
        tabContents = new HashMap<Node, TabState>();
    }

    public int getSelectedTabIndex(){

        // get the tabset state for this example
        TabState tabState = tabContents.get(currentNode);
        return tabState.getTabIndex();
    }

    public void setSelectedTabIndex(int tabIndex){
        TabState tabState = tabContents.get(currentNode);
        tabState.setTabIndex(tabIndex);
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public NavigationTreeFactory getNavigationTrees() {
        return navigationTreeFactory;
    }

    public HashMap<Node, TabState> getTabContents() {
        return tabContents;
    }

}
