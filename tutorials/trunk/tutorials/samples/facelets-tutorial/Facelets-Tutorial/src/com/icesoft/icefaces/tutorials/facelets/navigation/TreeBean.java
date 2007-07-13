package com.icesoft.icefaces.tutorials.facelets.navigation;

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
        rootObject.setText("Facelets Tutorial");
        rootObject.setUrl("home.iface");
        rootObject.setExpanded(true);
        rootTreeNode.setUserObject(rootObject);

        // model is accessed by by the ice:tree component
        model = new DefaultTreeModel(rootTreeNode);

        // add Tag parent node
        DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode();
        UrlNodeUserObject parentObject = new UrlNodeUserObject(parentNode);
        parentObject.setText("Facelets Tags");
        parentObject.setUrl("home.iface");
        parentObject.setExpanded(true);
        parentNode.setUserObject(parentObject);
        rootTreeNode.add(parentNode);
        
        // add ui:component child node
        DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode();
        UrlNodeUserObject branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("ui:component");
        branchObject.setUrl("component.iface");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        parentNode.add(branchNode);

        //add ui:composition child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("ui:composition");
        branchObject.setUrl("composition.iface");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        parentNode.add(branchNode);
        
        //add ui:debug child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("ui:debug");
        branchObject.setUrl("debug.iface");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        parentNode.add(branchNode);
        
        //add ui:decorate child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("ui:decorate");
        branchObject.setUrl("decorate.iface");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        parentNode.add(branchNode);
        
        //add ui:define child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("ui:define");
        branchObject.setUrl("define.iface");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        parentNode.add(branchNode);
        
        //add ui:fragment child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("ui:fragment");
        branchObject.setUrl("fragment.iface");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        parentNode.add(branchNode);
        
        //add ui:include child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("ui:include");
        branchObject.setUrl("include.iface");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        parentNode.add(branchNode);
        
        //add ui:include child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("ui:insert");
        branchObject.setUrl("insert.iface");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        parentNode.add(branchNode);
        
        //add ui:param child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("ui:param");
        branchObject.setUrl("param.iface");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        parentNode.add(branchNode);
        
        //add ui:remove child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("ui:remove");
        branchObject.setUrl("remove.iface");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        parentNode.add(branchNode);
        
        //add ui:repeat child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText("ui:repeat");
        branchObject.setUrl("repeat.iface");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        parentNode.add(branchNode);
                    
        //Create new parent node for custom component tutorial
        parentNode = new DefaultMutableTreeNode();
        parentObject = new UrlNodeUserObject(parentNode);
        parentObject.setText("Custom Facelets Tag");
        parentObject.setUrl("example.iface");
        parentObject.setExpanded(true);
        parentNode.setUserObject(parentObject);
        rootTreeNode.add(parentNode);
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
