package com.icesoft.icefaces.tutorial.component.tree.dynamic;

import com.icesoft.faces.component.tree.IceUserObject;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.faces.event.ActionEvent;

/**
 * <p>The <code>NodeUserObject</code> represents a nodes user object.  This
 * particular IceUserobject implementation store extra information on how many
 * times the parent node is clicked on.  It is also responsible for copying and
 * delete its self.</p>
 * <p/>
 * <p>In this example pay particularly close attention to the
 * <code>wrapper</code> instance variable on IceUserObject.  The <code>wrapper</code>
 * allows for direct manipulations of the parent tree. </p>
 */
public class DynamicNodeUserObject extends IceUserObject {


    // panel stack which will be manipulated when a command links action is fired.
    private TreeBean treeBean;

    private static String nodeToolTip;

    /**
     * Default contsructor for a PanelSelectUserObject object.  A reference
     * is made to a backing bean with the name "panelStack", if possible.
     * @param wrapper
     */
    public DynamicNodeUserObject(DefaultMutableTreeNode wrapper, TreeBean tree) {
        super(wrapper);

        treeBean = tree;

        setLeafIcon("xmlhttp/css/xp/css-images/tree_document.gif");
        setBranchContractedIcon("xmlhttp/css/xp/css-images/tree_folder_close.gif");
        setBranchExpandedIcon("xmlhttp/css/xp/css-images/tree_folder_open.gif");
        setText(generateLabel());
        setTooltip(nodeToolTip);
        setExpanded(true);
    }


    /**
     * Generates a label for the node based on an incrementing int.
     *
     * @return the generated label (eg. 'Node 5')
     */
    private String generateLabel(){
    	return  "Node " + Integer.toString(treeBean.getIncreasedLabelCount());
    }

    /**
     * Deletes this not from the parent tree.
     *
     * @param event that fired this method
     */
    public void deleteNode(ActionEvent event) {
        ((DefaultMutableTreeNode) getWrapper().getParent()).remove(getWrapper());
    }

    /**
     * Copies this node and adds a it as a child node.
     *
     * @param event that fired this method
     */
    public void copyNode(ActionEvent event) {
        DefaultMutableTreeNode clonedWrapper = new DefaultMutableTreeNode();
        DynamicNodeUserObject clonedUserObject = new DynamicNodeUserObject(clonedWrapper, treeBean);
        DynamicNodeUserObject originalUserObject = (DynamicNodeUserObject) getWrapper().getUserObject();
        clonedUserObject.setAction(originalUserObject.getAction());
        clonedUserObject.setBranchContractedIcon(originalUserObject.getBranchContractedIcon());
        clonedUserObject.setBranchExpandedIcon(originalUserObject.getBranchExpandedIcon());
        clonedUserObject.setExpanded(originalUserObject.isExpanded());
        clonedUserObject.setLeafIcon(originalUserObject.getLeafIcon());
        clonedWrapper.setUserObject(clonedUserObject);
        getWrapper().insert(clonedWrapper, 0);
    }

    /**
     * Registers a user click with this object and updates the selected node in the TreeBean.
     *
     * @param event that fired this method
     */
    public void nodeClicked(ActionEvent event) {
    	treeBean.setSelectedNodeObject(this);
    }
}
