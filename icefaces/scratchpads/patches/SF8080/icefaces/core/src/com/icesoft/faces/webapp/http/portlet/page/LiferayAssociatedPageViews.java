package com.icesoft.faces.webapp.http.portlet.page;

import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.theme.ThemeDisplay;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;

public class LiferayAssociatedPageViews extends AssociatedPageViewsImpl {

    private static final String THEME_DISPLAY = "THEME_DISPLAY";

    public String getPageId() {

        String pageId = null;

        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext extContext = facesContext.getExternalContext();
            Map reqMap = extContext.getRequestMap();

            ThemeDisplay themeDisplay = (ThemeDisplay) reqMap.get(THEME_DISPLAY);
            if (themeDisplay == null) {
                if (log.isWarnEnabled()) {
                    log.warn("could not find request attribute " + THEME_DISPLAY);
                }
            }

            LayoutTypePortlet layoutTypePortlet = themeDisplay.getLayoutTypePortlet();
            Layout layout = layoutTypePortlet.getLayout();
            pageId = layout.getFriendlyURL();

        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("could not get page id", e);
            }
        }

        return pageId;
    }
}