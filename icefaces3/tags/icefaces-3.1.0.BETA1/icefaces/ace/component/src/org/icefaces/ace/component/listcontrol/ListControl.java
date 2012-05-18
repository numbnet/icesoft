package org.icefaces.ace.component.listcontrol;

import javax.faces.context.FacesContext;

public class ListControl extends ListControlBase {
    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public String getSelector(String clientId, boolean dualListMode) {
        if (dualListMode) {
            return "#"+clientId + " ." + ListControlRenderer.firstStyleClass + ":first .if-list:first, " +
                   "#"+clientId + " ." + ListControlRenderer.secondStyleClass + ":first .if-list:first";
        }
        return super.getSelector();
    }
}
