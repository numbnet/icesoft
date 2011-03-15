package com.icesoft.icefaces.tutorial.component.tree.selection;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * <p/>
 * A basic backing bean for a ice:tree component.  The only instance variable
 * needed is a DefaultTreeModel Object which is bound to the icefaces tree
 * component in the jspx code.</p>
 * <p/>
 * The tree created by this backing bean is used to control the selected
 * panel in a ice:panelStack.
 * </p>
 */
public class TreeBean {

    // tree default model, used as a value for the tree component
    private DefaultTreeModel model;


    public TreeBean() {

        // create root node with its children expanded
        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode();
        PanelSelectUserObject rootObject = new PanelSelectUserObject(rootTreeNode);
        rootObject.setText("ICEsoft Products");
        rootObject.setDisplayPanel("splash");
        rootObject.setExpanded(true);
        rootTreeNode.setUserObject(rootObject);

        // model is accessed by by the ice:tree component
        model = new DefaultTreeModel(rootTreeNode);

        // add a node to sue ICEfaces information
        DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode();
        PanelSelectUserObject branchObject = new PanelSelectUserObject(branchNode);
        branchObject.setText("ICEfaces");
        branchObject.setDisplayPanel("icefaces");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        rootTreeNode.add(branchNode);

        // add a node to sue ICEbrowser information
        branchNode = new DefaultMutableTreeNode();
        branchObject = new PanelSelectUserObject(branchNode);
        branchObject.setText("ICEbrowser");
        branchObject.setDisplayPanel("icebrowser");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        rootTreeNode.add(branchNode);

        // add a node to sue ICEpdf information
        branchNode = new DefaultMutableTreeNode();
        branchObject = new PanelSelectUserObject(branchNode);
        branchObject.setText("ICEpdf");
        branchObject.setDisplayPanel("icepdf");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        rootTreeNode.add(branchNode);
    }

    /**
     * Gets the tree's default model.
     *
     * @return tree model.
     */
    public DefaultTreeModel getModel() {
        return model;
    }
}
