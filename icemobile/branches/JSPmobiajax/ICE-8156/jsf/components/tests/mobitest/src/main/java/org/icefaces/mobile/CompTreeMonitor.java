package org.icefaces.mobile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Iterator;

@ManagedBean

@SessionScoped
public class CompTreeMonitor {

    public int getTreeSize() {
        return countChildComponents(FacesContext.getCurrentInstance()
                .getViewRoot());
    }
    private static int countChildComponents(UIComponent uic) {
        int children = 0;
        if (uic.getChildCount() > 0 || uic.getFacetCount() > 0) {
            Iterator<UIComponent> iter = uic.getFacetsAndChildren();
            while (iter.hasNext()) {
                children += countChildComponents(iter.next());
            }
        } else {
            children = 1;
        }
        return children;
    }
}
