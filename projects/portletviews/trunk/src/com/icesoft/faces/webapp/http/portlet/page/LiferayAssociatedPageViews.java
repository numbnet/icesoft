package com.icesoft.faces.webapp.http.portlet.page;

import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.theme.ThemeDisplay;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class LiferayAssociatedPageViews extends AssociatedPageViewsImpl {

    private static final String THEME_DISPLAY = "THEME_DISPLAY";

    public String getPageId() throws Exception {

        String pageId = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = facesContext.getExternalContext();
        Map reqMap = extContext.getRequestMap();

        ThemeDisplay themeDisplay = (ThemeDisplay) reqMap.get(THEME_DISPLAY);
        LayoutTypePortlet layoutTypePortlet = themeDisplay.getLayoutTypePortlet();
        Layout layout = layoutTypePortlet.getLayout();
        pageId = layout.getFriendlyURL();

        return pageId;
    }

    public HttpServletRequest getServletRequest() throws Exception {
        return null;
    }
}