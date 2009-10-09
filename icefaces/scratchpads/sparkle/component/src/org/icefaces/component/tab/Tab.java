package org.icefaces.component.tab;

import javax.faces.component.UIComponent;

public class Tab extends TabBase {
    public UIComponent getHeadFacet() {
        return (UIComponent) getFacet("header");
    }

    public UIComponent getBodyFacet() {
        return (UIComponent) getFacet("body");
    }
    
    public UIComponent getFooterFacet() {
        return (UIComponent) getFacet("footer");
    }

    public UIComponent getLabelFacet() {
        return (UIComponent) getFacet("label");
    }
}
