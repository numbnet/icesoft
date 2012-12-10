package org.icefaces.mobile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.Iterator;

@ManagedBean

@SessionScoped
public class CompTreeMonitor implements java.io.Serializable {

    public int getTreeSize() {
        String stackId= "stack1";
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        UIComponent stack = root.findComponent(stackId);
        if (null==stack)return -1;
        return countChildComponents(stack);
    }
    private static int countChildComponents(UIComponent uic) {
        int children = 1;
        if (uic.getChildCount() > 0 || uic.getFacetCount() > 0) {
            Iterator<UIComponent> iter = uic.getFacetsAndChildren();
            while (iter.hasNext()) {
                children += countChildComponents(iter.next());
            }
        }
        return children;
    }
}
