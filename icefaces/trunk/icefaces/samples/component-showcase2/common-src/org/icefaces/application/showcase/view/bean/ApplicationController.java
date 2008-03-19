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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icefaces.application.showcase.util.FacesUtils;
import org.icefaces.application.showcase.util.ContextUtilBean;
import org.icefaces.application.showcase.view.builder.ApplicationBuilder;
import org.icefaces.application.showcase.view.jaxb.*;

import javax.faces.event.ActionEvent;
import javax.faces.event.AbortProcessingException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;

import com.icesoft.faces.component.paneltabset.TabChangeEvent;

/**
 * <p>Application controller is responsible for managing the presentation
 * layer action and actionListener functionality.  This bean is intended
 * to stay in Application scope and act on request and session beans as
 * needed</p>
 *
 * @since 1.7
 */
public class ApplicationController {

    private static final Log logger =
            LogFactory.getLog(ApplicationController.class);

    public void navigationEvent(ActionEvent event) {

        // get id of node that was clicked on.
        String nodeId = FacesUtils.getRequestParameter("nodeId");

        // do a lookup on the parent component to get the node object
        ApplicationBuilder applicationBuilder =
                (ApplicationBuilder) FacesUtils.getManagedBean(
                        BeanNames.APPLICATION_BUILDER);

        Node node = applicationBuilder.getNode(nodeId);

        // make sure we want to follow through with the content switch/assignment
        if (node != null && node.isLink()) {

            // set the current node in the model, the view will figure out which
            // components to show
            ApplicationSessionModel applicationModel =
                    (ApplicationSessionModel) FacesUtils.getManagedBean(
                            BeanNames.APPLICATION_MODEL);
            // reset selected state on old selected node.
            setTreeNodeSelectedState(
                    applicationModel.getCurrentNode().getId(), false);

            applicationModel.setCurrentNode(node);

            // mark the new current node as selected
            setTreeNodeSelectedState(
                    applicationModel.getCurrentNode().getId(), true);
                  
            // update the tabset state for this example
            HashMap<Node, TabState> tabStates = applicationModel.getTabContents();
            TabState tabState = tabStates.get(node);

            if (tabState == null){
                tabState = new TabState();
                // set default selected tab index. 
                tabState.setTabIndex(TabState.DEMONSTRATION_TAB_INDEX);
            }
            // update selected tab and put back in session.
            tabStates.put(node, tabState);
        }
        else if (node != null && !node.isLink()){
            // toggle expanded state. 
            toggleTreeNodeExpandedState(node.getId());
        }


    }

    /**
     * Called when the table binding's tab focus changes.
     *
     * @param tabChangeEvent used to set the tab focus.
     * @throws javax.faces.event.AbortProcessingException An exception that may be thrown by event
     *                                  listeners to terminate the processing of the current event.
     */
    public void processTabChange(TabChangeEvent tabChangeEvent)
            throws AbortProcessingException {

        // update model with data to persist currently selected tab
        ApplicationSessionModel applicationModel =
                (ApplicationSessionModel) FacesUtils.getManagedBean(
                        BeanNames.APPLICATION_MODEL);

        Node currentNode = applicationModel.getCurrentNode();

        // get the tabset state for this example
        HashMap<Node, TabState> tabStates = applicationModel.getTabContents();
        TabState tabState = tabStates.get(currentNode);

        // update model with selected tab index. 
        tabState.setTabIndex(tabChangeEvent.getNewTabIndex());
    }

    /**
     * <p></p>
     *
     * @return
     */
    public String getCurrentSource(){
        // set the current node in the model
        ApplicationSessionModel applicationModel =
                (ApplicationSessionModel) FacesUtils.getManagedBean(
                        BeanNames.APPLICATION_MODEL);

        Node currentNode = applicationModel.getCurrentNode();

        // get the tabset state for this example
        HashMap<Node, TabState> tabStates = applicationModel.getTabContents();
        TabState tabState = tabStates.get(currentNode);

        if (tabState.getSourceContent() != null){
            return ContextUtilBean.generateSourceCodeUrl(
                    tabState.getSourceContent());
        }
        return ContextUtilBean.generateMarkup("");
    }

     public String getCurrentDocumentSource(){
        // set the current node in the model
        ApplicationSessionModel applicationModel =
                (ApplicationSessionModel) FacesUtils.getManagedBean(
                        BeanNames.APPLICATION_MODEL);

        Node currentNode = applicationModel.getCurrentNode();

        // get the tabset state for this example
        HashMap<Node, TabState> tabStates = applicationModel.getTabContents();
        TabState tabState = tabStates.get(currentNode);

        if (tabState.getDescriptionContent() != null){
            return ContextUtilBean.generateDocumentUrl(
                    tabState.getDescriptionContent());
        }
        return ContextUtilBean.generateMarkup("");
    }

    /**
     * <p>Loads the source code specified byt he request parameters includePath
     * and resourceId.</p>
     *
     * @param event jsf action event
     */
    public void viewSourceEvent(ActionEvent event){
        // get path of include
        String includePath = FacesUtils.getRequestParameter("includePath");

        // set the current node in the model
        ApplicationSessionModel applicationModel =
                (ApplicationSessionModel) FacesUtils.getManagedBean(
                        BeanNames.APPLICATION_MODEL);

        Node currentNode = applicationModel.getCurrentNode();

        // get the tabset state for this example
        HashMap<Node, TabState> tabStates = applicationModel.getTabContents();
        TabState tabState = tabStates.get(currentNode);

        // update tabset state
        tabState.setSourceContent(includePath);

        // put state back in session scope map.
        tabStates.put(currentNode, tabState);

    }

    public void viewIncludeEvent(ActionEvent event){
        // get id of node that was clicked on.
        String includePath = FacesUtils.getRequestParameter("includePath");

        // set the current node in the model
        ApplicationSessionModel applicationModel =
                (ApplicationSessionModel) FacesUtils.getManagedBean(
                        BeanNames.APPLICATION_MODEL);

        Node currentNode = applicationModel.getCurrentNode();

        // get the tabset state for this example
        HashMap<Node, TabState> tabStates = applicationModel.getTabContents();
        TabState tabState = tabStates.get(currentNode);

        // update tabset state
        tabState.setDescriptionContent(includePath);

        // put state back in session scope map.
        tabStates.put(currentNode, tabState);
    }

    public ContentDescriptor getCurrentContextDescriptor() {
        ApplicationSessionModel applicationModel =
                (ApplicationSessionModel) FacesUtils.getManagedBean(
                        BeanNames.APPLICATION_MODEL);
        ApplicationBuilder applicationBuilder =
                (ApplicationBuilder) FacesUtils.getManagedBean(
                        BeanNames.APPLICATION_BUILDER);

        Node currentNode = applicationModel.getCurrentNode();

        return applicationBuilder.getContextDescriptor(currentNode);
    }


    private void setTreeNodeSelectedState(String nodeId, boolean value) {
        DefaultMutableTreeNode defaultNode = findTreeNode(nodeId);
        if (defaultNode != null){
            NavigationTreeNode node = (NavigationTreeNode) defaultNode.getUserObject();
            node.setSelected(value);
        }
    }

    private void toggleTreeNodeExpandedState(String nodeId) {
        DefaultMutableTreeNode defaultNode = findTreeNode(nodeId);
        if (defaultNode != null){
            NavigationTreeNode node = (NavigationTreeNode) defaultNode.getUserObject();
            node.setExpanded(!node.isExpanded());
        }
    }

    private DefaultMutableTreeNode findTreeNode(String nodeId) {
        ApplicationSessionModel applicationModel =
                (ApplicationSessionModel) FacesUtils.getManagedBean(
                        BeanNames.APPLICATION_MODEL);
        Collection<DefaultTreeModel> trees =
                applicationModel.getNavigationTrees().values();

        DefaultMutableTreeNode node;
        for (DefaultTreeModel treeModel : trees) {
            DefaultMutableTreeNode rootNode =
                    (DefaultMutableTreeNode) treeModel.getRoot();
            Enumeration nodes = rootNode.depthFirstEnumeration();
            while (nodes.hasMoreElements()) {
                node = (DefaultMutableTreeNode) nodes.nextElement();
                NavigationTreeNode tmp = (NavigationTreeNode) node.getUserObject();
                if (tmp.getNodeId() != null && tmp.getNodeId().equals(nodeId)) {
                    return node;
                }
            }
        }
        return null;
    }
}
