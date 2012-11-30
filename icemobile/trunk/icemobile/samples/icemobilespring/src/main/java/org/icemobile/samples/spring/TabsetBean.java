package org.icemobile.samples.spring;

import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This is a sample backing bean for the MVC supported Tabset
 */
@SessionAttributes("tabsetBean")
public class TabsetBean {


    private Integer tabsetOneIndex = Integer.valueOf(0);

    public Integer getTabsetOne() {
        return tabsetOneIndex;
    }

    public void setTabsetOne(Integer tabsetOneIndex) {
    	if( tabsetOneIndex != null ){
    		this.tabsetOneIndex = tabsetOneIndex;
    	}
    }
}
