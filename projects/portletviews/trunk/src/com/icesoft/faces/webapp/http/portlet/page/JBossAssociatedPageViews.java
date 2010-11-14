package com.icesoft.faces.webapp.http.portlet.page;

import org.jboss.portal.api.node.PortalNode;
import org.jboss.portal.core.aspects.controller.node.Navigation;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

public class JBossAssociatedPageViews extends AssociatedPageViewsImpl {

    /**
     * For JBoss Portal, we get and use the page name that a porlet is contained in along with
     * other processing as the page name is not unique between tabs/windows in the same browser.
     *
     * @return The name of the page that the current portlet is on.
     */
    public String getPageId() throws Exception {
        PortalNode node = Navigation.getCurrentNode();
        PortalNode parent = node.getParent();
        String pageName = parent.getName();

        HttpServletRequest req = getServletRequest();
        String id = (String)req.getAttribute(VIEW_GROUP);

        if(id == null){
            id = pageName + "-" + getNextCounter();
            req.setAttribute(VIEW_GROUP,id);
        }

        return id;
    }

    /**
     * For JBoss Portal, we use a reflection and non-public APIs to get at the original
     * HttpServletRequest that rendered the page and all the portlets it contains.  The
     * reason is that the page name by itself is not unique and there doesn't seem to be
     * any other API to provide a way to uniquely identify each page.  We need this so that
     * multiple tabs or windows pointed at the same page do not group all their views for
     * disposal.  In this case, using the original servlet request, each created view is
     * grouped according to the
     *
     * @return The original HttpServletRequest that triggered the rendering of the current portlet.
     * @throws Exception
     */
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
        Method getRealReqMethod = superClass.getDeclaredMethod("getRealRequest", new Class[0]);
        if (getRealReqMethod == null) {
            log.error("could not get method 'getRealRequest'" +
                    "\n  original request class is: " + origPortletReqClass +
                    "\n     expected              : org.jboss.portal.portlet.impl.jsr168.api.RenderRequestImpl" +
                    "\n  super class is           : " + superClass +
                    "\n     expected              : org.jboss.portal.portlet.impl.jsr168.api.PortletRequestImpl"
            );
        }

        Object realRequestObj = getRealReqMethod.invoke(origPortletReqObj, new Object[0]);

        if (realRequestObj instanceof HttpServletRequest) {
            return (HttpServletRequest) realRequestObj;
        }

        log.error("request is not an instance of HttpServletRequest [" + realRequestObj + "]");
        return null;
    }
}
