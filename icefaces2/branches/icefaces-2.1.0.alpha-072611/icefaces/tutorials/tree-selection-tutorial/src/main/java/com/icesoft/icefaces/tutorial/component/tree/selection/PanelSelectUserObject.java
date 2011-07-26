package com.icesoft.icefaces.tutorial.component.tree.selection;

import com.icesoft.faces.component.tree.IceUserObject;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;

/**
 * <p>The PanelSelectUserObject is responsible for selcting a known panel in a
 * panelStack when #selectPanelStackPanel is called.  When the PanelSelectUserObject
 * is constructed a reference is set via the FacesContext to the backing
 * bean which is responsible for setting the selected panel in the panelStack
 * component</p>
 */
public class PanelSelectUserObject extends IceUserObject {

    // displayPanel to show when a node is clicked
    private String displayPanel;

    // panel stack which will be manipulated when a command links action is fired.
    private PanelStackBean panelStack;

    /**
     * Default contsructor for a PanelSelectUserObject object.  A reference
     * is made to a backing bean with the name "panelStack", if possible.
     * @param wrapper
     */
    public PanelSelectUserObject(DefaultMutableTreeNode wrapper) {
        super(wrapper);

        // get a reference to the PanelStackBean from the faces context
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Object panelStackObject =
                facesContext.getApplication()
                        .createValueBinding("#{panelStack}")
                        .getValue(facesContext);
        if (panelStackObject instanceof PanelStackBean){
            panelStack = (PanelStackBean)panelStackObject;
        }
    }

    /**
     * Gets the name of a panel in the panel stack which is associated with
     * this object.
     *
     * @return name of a panel in the panel stack
     */
    public String getDisplayPanel() {
        return displayPanel;
    }

    /**
     * Sets the name of a panelStack panel, assumed to be valid.
     *
     * @param displayPanel panelStack panel name.
     */
    public void setDisplayPanel(String displayPanel) {
        this.displayPanel = displayPanel;
    }

    /**
     * ActionListener method called when a node in the tree is clicked.  Sets
     * the selected panel of the reference panelStack to the value of the instance
     * variable #displayPanel.   
     *
     * @param action JSF action event.
     */
    public void selectPanelStackPanel(ActionEvent action){
        if (panelStack != null){
            panelStack.setSelectedPanel(displayPanel);
        }
    }

}
