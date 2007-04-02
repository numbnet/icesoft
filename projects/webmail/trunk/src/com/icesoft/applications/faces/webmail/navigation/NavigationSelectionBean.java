/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.navigation;

/**
 * <p>The NavigationSelectionBean class is responsible for storing the state of the
 * two panel stacks which display dynamic content.  </p>
 *
 * @since 1.0
 */
public class NavigationSelectionBean {

    // todo: build history linked list, to allow navigation control in page

    // selected page content bean.
    private NavigationContent selectedPanel;

    /**
     * Gest the currently selected content panel.
     *
     * @return currently selected content panel.
     */
    public NavigationContent getSelectedPanel() {
        return selectedPanel;
    }

    /**
     * Sets the selected panel to the specified panel.
     *
     * @param selectedPanel a none null page content bean.
     */
    public void setSelectedPanel(NavigationContent selectedPanel) {
        if (selectedPanel != null && selectedPanel.isPageContent()) {
            // set the default icons only if the page content changed
            if (this.selectedPanel != null &&
                    !selectedPanel.equals(this.selectedPanel) ){
                this.selectedPanel.restoreDefaultIcons();
            }
            this.selectedPanel = selectedPanel;
        }
    }
}
