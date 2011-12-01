package com.icesoft.icefaces.tutorial.component.tree.selection;

/**
 * <p>
 * The PanelStackBean is responsible for storing the name of the panel in the
 * panelStack which should be displayed when rendered. The default panel stack
 * panel name is "splash".
 * </p>
 * <p>
 * If a selectedPanel name is not found in the panel stack the panelStack
 * comonent will not change the current selected PanelStack
 * </p>
 */
public class PanelStackBean {

    // default panel name
    private String selectedPanel = "splash";

    /**
     * Gets the name of the selected panel stack.
     * @return selected panel stack name
     */
    public String getSelectedPanel() {
        return selectedPanel;
    }

    /**
     * 
     * @param selectedPanel
     */
    public void setSelectedPanel(String selectedPanel) {
        this.selectedPanel = selectedPanel;
    }
}
