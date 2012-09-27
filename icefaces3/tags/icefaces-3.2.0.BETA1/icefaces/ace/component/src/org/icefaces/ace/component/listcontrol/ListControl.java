package org.icefaces.ace.component.listcontrol;

import javax.faces.context.FacesContext;

public class ListControl extends ListControlBase {
    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public String getSelector(String clientId, boolean dualListMode) {
        if (dualListMode) {
            return "#"+clientId + " > div.if-list-dl > span." + ListControlRenderer.firstStyleClass + " > div > div.if-list, " +
                   "#"+clientId + " > div.if-list-dl > span." + ListControlRenderer.secondStyleClass + " > div > div.if-list";
        }
        return super.getSelector();
    }
}
