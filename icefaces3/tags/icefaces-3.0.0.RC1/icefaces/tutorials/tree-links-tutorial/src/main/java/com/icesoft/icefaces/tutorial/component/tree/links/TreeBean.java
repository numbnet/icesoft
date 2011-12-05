package com.icesoft.icefaces.tutorial.component.tree.links;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

/**
 * <p/>
 * A basic backing bean for a ice:tree component.  The only instance variable
 * needed is a DefaultTreeModel Object which is bound to the icefaces tree
 * component in the jspx code.</p>
 * <p/>
 * The tree created by this backing bean is very simple, containing only text
 * nodes. The plus and minus icons which expand the tree are rendered because
 * of attributes set at the component level.
 * </p>
 */
public class TreeBean {

    // tree default model, used as a value for the tree component
    private DefaultTreeModel model;

    public TreeBean() {
        // create root node with its children expanded
        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode();
        UrlNodeUserObject rootObject = new UrlNodeUserObject(rootTreeNode);
        rootObject.setText("Root Node");
        rootObject.setUrl("");
        rootObject.setExpanded(true);
        rootTreeNode.setUserObject(rootObject);

        // model is accessed by by the ice:tree component
        model = new DefaultTreeModel(rootTreeNode);

        // add ICEsoft URL child node
        DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode();
        UrlNodeUserObject branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("ICEsoft Technologies Inc.");
        branchObject.setUrl("http://www.icesoft.com");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        rootTreeNode.add(branchNode);

        // add Google URL child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("Google Search");
        branchObject.setUrl("http://www.google.com");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        rootTreeNode.add(branchNode);

        // add Google URL child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("Google News");
        branchObject.setUrl("http://news.google.com");
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

    public void checkChanged(ValueChangeEvent event){
        System.out.println("checkboxed selected " + event.getComponent().getId());
    }

}
