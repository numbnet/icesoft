package org.icefaces.ace.component.tabset;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import java.util.*;

/**
 * Utility code that  TabPaneCache relies on for making the auto mode work
 */
public class TabPaneUtil {
    public static boolean isAutoDynamic(FacesContext facesContext, UIComponent tab) {
        // If render=@all and src component within tabPane, then promote static to dynamic
        // But for now not the rest:
        // If any render regions are specific, and are equal to or within the
        // tabPane, then promote static to dynamic
        Set<String> affectedRegions = new HashSet<String>(6);
        if (facesContext.getPartialViewContext().isRenderAll()) {
            Map<String, String> rpm = facesContext.getExternalContext().getRequestParameterMap();
            if (rpm.containsKey("javax.faces.source")) {
                String srcCompClientId = rpm.get("javax.faces.source");
                if (!affectedRegions.contains(srcCompClientId)) {
                    affectedRegions.add(srcCompClientId);
                }
            }
            if (rpm.containsKey("ice.event.captured")) {
                String srcCompClientId = rpm.get("ice.event.captured");
                if (!affectedRegions.contains(srcCompClientId)) {
                    affectedRegions.add(srcCompClientId);
                }
            }
        } else {
            /*
            Collection<String> renderIds = facesContext.getPartialViewContext().getRenderIds();
            if (renderIds != null && renderIds.size() > 0) {
                for (String rid : renderIds) {
                    if (!affectedRegions.contains(rid)) {
                        affectedRegions.add(rid);
                    }
                }
            }
            */
        }
        if (affectedRegions.size() > 0) {
//System.out.println("affectedRegions: " + affectedRegions);

            // Need to determine if any of the affected regions are within this tabPane
            //Set<String> clientIds = clientId2FacesEvent.keySet();
            EnumSet<VisitHint> hints = EnumSet.of(
                VisitHint.SKIP_UNRENDERED);
            VisitContext visitContext = VisitContext.createVisitContext(
                facesContext, affectedRegions, hints);
            RegionInTabPane vcall = new RegionInTabPane(tab);
            facesContext.getViewRoot().visitTree(visitContext, vcall);
            boolean ret = vcall.isEqualOrInside();
//System.out.println("equalOrInside: " + ret);
            return ret;
        }
        return false;
    }

    
    private static class RegionInTabPane implements VisitCallback {
        private UIComponent tabPane;
        private boolean equalOrInside;

        RegionInTabPane(UIComponent tabPane) {
            this.tabPane = tabPane;
        }

        boolean isEqualOrInside() {
            return equalOrInside;
        }

        public VisitResult visit(VisitContext visitContext,
                                 UIComponent uiComponent) {
            FacesContext facesContext = visitContext.getFacesContext();
            String clientId = uiComponent.getClientId(facesContext);
//System.out.println("visit()  clientId: " + clientId);
            for (UIComponent seek = uiComponent; seek != null; seek = seek.getParent()) {
                if (seek == tabPane) {
                    equalOrInside = true;
//System.out.println("visit()    MATCH  parent");
                    return VisitResult.COMPLETE;
                }
            }
            //if (visitContext.getIdsToVisit().contains(clientId)) {
            //    System.out.println("visit()    MATCH  clientId");
            //}
/*
ACCEPT
This result indicates that the tree visit should descend into current component's subtree.

COMPLETE
This result indicates that the tree visit should be terminated.

REJECT
This result indicates that the tree visit should continue, but should skip the current component's subtree.
*/
            return VisitResult.ACCEPT;
        }
    }
}
