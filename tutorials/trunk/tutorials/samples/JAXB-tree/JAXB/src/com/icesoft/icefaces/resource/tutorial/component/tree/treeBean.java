/*
 * treeBean.java
 *
 * Created on April 18, 2007, 1:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.icesoft.icefaces.resource.tutorial.component.tree;

/**
 *
 * @author dnorthcott
 */
import com.icesoft.icefaces.resource.tutorial.component.jaxb.Fleet;
import com.icesoft.icefaces.resource.tutorial.component.jaxb.ShipFleetType;
import com.icesoft.icefaces.resource.tutorial.component.jaxb.ShipType;

import java.util.ArrayList;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import java.io.File;

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
public class treeBean{

    // tree default model, used as a value for the tree component
    private DefaultTreeModel model;
    private DefaultMutableTreeNode rootTreeNode;
    private static Fleet fleet;
    private static JAXBContext jc;
    private static Unmarshaller unmarshaller;
    private ShipType ship1;
    private ShipType ship2;
    static{
        try {

            jc = JAXBContext.newInstance("com.icesoft.icefaces.resource.tutorial.component.jaxb");

            unmarshaller = (Unmarshaller)jc.createUnmarshaller();
            String metaDataPath = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("treeDocument.xml");
            fleet = (Fleet)unmarshaller.unmarshal(new File(metaDataPath));

        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

    }

    public treeBean() {

        Fleet.Ships shipType = fleet.getShips();
        ArrayList shipList = (ArrayList)shipType.getShip();
        ship1 = (ShipType)shipList.get(0);
        ship2 = (ShipType)shipList.get(1);

        // create root node with its children expanded
        rootTreeNode = new DefaultMutableTreeNode();
        UrlNodeUserObject rootObject = new UrlNodeUserObject(rootTreeNode);
        rootObject.setText("Fleet");
        rootObject.setUrl("");
        rootObject.setExpanded(true);
        rootTreeNode.setUserObject(rootObject);

        // model is accessed by by the ice:tree component
        model = new DefaultTreeModel(rootTreeNode);

        // add Ship1 child node
        DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode();
        UrlNodeUserObject branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText(ship1.getName());

        branchNode.setUserObject(branchObject);
        rootTreeNode.add(branchNode);

        // add Ship2 child node
        branchNode = new DefaultMutableTreeNode();
        branchObject = new UrlNodeUserObject(branchNode);
        branchObject.setText(ship2.getName());

        branchNode.setUserObject(branchObject);
        rootTreeNode.add(branchNode);


    }

    public void addNode(Object adder){
        DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode();
        UrlNodeUserObject branchObject = new UrlNodeUserObject(branchNode);
        //branchObject.setText(adder.getName());

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
    public ShipType getShip1(){
        return ship1;
    }
    public ShipType getShip2(){
        return ship2;
    }

}
