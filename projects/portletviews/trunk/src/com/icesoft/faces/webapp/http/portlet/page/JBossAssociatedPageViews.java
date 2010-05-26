package com.icesoft.faces.webapp.http.portlet.page;

import org.jboss.portal.api.node.PortalNode;
import org.jboss.portal.core.aspects.controller.node.Navigation;

public class JBossAssociatedPageViews extends AssociatedPageViewsImpl {

    public String getPageId() {

        try {
            PortalNode node = Navigation.getCurrentNode();
            PortalNode parent = node.getParent();
            String pageName = parent.getName();
            return pageName;
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("could not get page id", e);
            }
        }

        return null;
    }
}
