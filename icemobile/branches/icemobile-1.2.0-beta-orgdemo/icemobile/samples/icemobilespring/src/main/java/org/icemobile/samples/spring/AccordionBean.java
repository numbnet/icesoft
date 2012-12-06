package org.icemobile.samples.spring;

import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This is a sample backing bean for the MVC supported state
 * By convention, he name of the properties should be the same as the ids of
 * the jsp tags.
 */
@SessionAttributes("accordionBean")
public class AccordionBean {

    private String selectedId1 = "accordionPane1";
    private String selectedId2 = "accordionPane4";
    public String getSelectedId1() {
        return selectedId1;
    }
    public void setSelectedId1(String selectedId1) {
        this.selectedId1 = selectedId1;
    }
    public String getSelectedId2() {
        return selectedId2;
    }
    public void setSelectedId2(String selectedId2) {
        this.selectedId2 = selectedId2;
    }

    
}
