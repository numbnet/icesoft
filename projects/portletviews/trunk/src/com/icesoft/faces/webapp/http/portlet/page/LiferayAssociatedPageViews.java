package com.icesoft.faces.webapp.http.portlet.page;

import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.theme.ThemeDisplay;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class LiferayAssociatedPageViews extends AssociatedPageViewsImpl {

    private static final String THEME_DISPLAY = "THEME_DISPLAY";
    
    private static final Class[] paramTypes = new Class[3];

    static{
        paramTypes[0] = String.class;
        paramTypes[1] = Object.class;
        paramTypes[2] = Boolean.TYPE;
    }

    /**
     * For Liferay Portal, we get and use the friendly page URL that a porlet is contained in along with
     * other processing as the page name is not unique between tabs/windows in the same browser.
     *
     * @return The name of the page that the current portlet is on.
     */
    public String getPageId() throws Exception {

        String pageURL = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = facesContext.getExternalContext();
        Map reqMap = extContext.getRequestMap();

        ThemeDisplay themeDisplay = (ThemeDisplay) reqMap.get(THEME_DISPLAY);
        LayoutTypePortlet layoutTypePortlet = themeDisplay.getLayoutTypePortlet();
        Layout layout = layoutTypePortlet.getLayout();
        pageURL = layout.getFriendlyURL();

        HttpServletRequest req = getServletRequest();
        String pageId = (String)req.getAttribute(VIEW_GROUP);

        if(pageId == null){
            pageId = pageURL + "-" + getNextCounter();
            setAttribute(req, VIEW_GROUP, pageId);
        }
        return pageId;
    }

    private HttpServletRequest getServletRequest() throws Exception {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map requestMap = ec.getRequestMap();

        //The spec allows access to the original portlet request from the using
        //this attribute in the request map.
        Object origPortletReqObj = requestMap.get("javax.portlet.request");

        //The original portlet request class is a specific JBoss implementation which at the
        //time this was written was org.jboss.portal.portlet.impl.jsr168.api.RenderRequestImpl
        Class origPortletReqClass = origPortletReqObj.getClass();

        //To get at the original HttpServletRequest, we're interested in the superclass
        //org.jboss.portal.portlet.impl.jsr168.api.PortletRequestImpl which has the
        //getRealRequest() method.
        Class superClass = origPortletReqClass.getSuperclass();

        //Provided we've made all the right assumptions above, we can get and invoke
        //the getRealRequest() method and send back the HttpServletRequest.
        Method getRealReqMethod = superClass.getDeclaredMethod("getHttpServletRequest", new Class[0]);
        if (getRealReqMethod == null) {
            log.error("could not get method 'getRealRequest'" +
                    "\n  original request class is: " + origPortletReqClass +
                    "\n     expected              : com.liferay.portlet.RenderRequestImpl" +
                    "\n  super class is           : " + superClass +
                    "\n     expected              : com.liferay.portal.kernel.portlet.LiferayPortletRequest"
            );
        }

        Object realRequestObj = getRealReqMethod.invoke(origPortletReqObj, new Object[0]);

        if (realRequestObj instanceof HttpServletRequest) {
            return (HttpServletRequest) realRequestObj;
        }

        log.error("request is not an instance of HttpServletRequest [" + realRequestObj + "]");
        return null;
    }

    /**
     * Liferay's original request object namespaces request attributes by default meaning that
     * whatever we add for one portlet is not visible to other portlets on the same page.  They
     * do however provide a method for setting attributes that allows you to disable the namespacing
     * if you wish so we do that, reflectively, in this method.
     *
     * @param req
     * @param viewGroup
     * @param pageId
     */
    private void setAttribute(HttpServletRequest req, String viewGroup, String pageId) {

        try {
            Class clazz = req.getClass();
            Method meth = req.getClass().getDeclaredMethod("setAttribute", paramTypes);
            Object[] params = new Object[3];
            params[0] = viewGroup;
            params[1] = pageId;
            params[2] = Boolean.FALSE; //Indicates that we don't want the attribute namespaced
            
            Object result = meth.invoke(req,params);
        } catch (Exception e) {
            log.error("could not get method 'setAttribute'" +
                    "\n  original request class is: " + req +
                    "\n     expected              : com.liferay.portal.servlet.NamespaceServletRequest",
                    e
            );
        }

    }
}