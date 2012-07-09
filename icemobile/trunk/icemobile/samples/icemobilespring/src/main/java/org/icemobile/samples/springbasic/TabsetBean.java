package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This is a sample backing bean for the MVC supported Tabset
 */
@SessionAttributes("tabsetBean")
public class TabsetBean {


    private int tabsetOneIndex;

    public int getTabsetOne() {
        return tabsetOneIndex;
    }

    public void setTabsetOne(int tabsetOneIndex) {
        this.tabsetOneIndex = tabsetOneIndex;
    }
}
