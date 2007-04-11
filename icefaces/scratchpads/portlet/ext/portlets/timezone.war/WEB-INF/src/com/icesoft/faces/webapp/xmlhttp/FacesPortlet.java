package com.icesoft.faces.webapp.xmlhttp;

//import com.icesoft.util.IdGenerator;

import java.io.IOException;
import java.util.Enumeration;
//import java.net.MalformedURLException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
//import javax.portlet.PortletSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FacesPortlet
implements Portlet {
    private static Log log = LogFactory.getLog(FacesPortlet.class);

//    private IdGenerator idGenerator;
    private PortletConfig portletConfig;

    public void destroy() {
        log.info("FacesPortlet.destroy()");
    }

    public void init(PortletConfig portletConfig)
    throws PortletException {
        log.info("FacesPortlet.init(portletConfig: [" + portletConfig + "])");
        this.portletConfig = portletConfig;
//        try {
//            idGenerator =
//                new IdGenerator(
//                    this.portletConfig.getPortletContext().getResource("/").
//                        getPath());
//        } catch (MalformedURLException exception) {
//            if (log.isFatalEnabled()) {
//                log.fatal(exception);
//                throw new PortletException(exception);
//            }
//        }
    }

    public void processAction(
        ActionRequest actionRequest, ActionResponse actionResponse)
    throws IOException, PortletException {
        log.info(
            "FacesPortlet.processAction(" +
                "actionRequest: [" + actionRequest + "], " +
                "actionResponse: [" + actionResponse + "])");
    }

    public void render(
        RenderRequest renderRequest, RenderResponse renderResponse)
    throws IOException, PortletException {
        log.info(
            "FacesPortlet.render(" +
                "renderRequest: [" + renderRequest + "], " +
                "renderResponse: [" + renderResponse + "]):: " +
                    "Portlet Session: " +
                        renderRequest.getPortletSession().getId());
        Enumeration _enumeration;
        log.info("PortletConfig's init parameters:");
        _enumeration = portletConfig.getInitParameterNames();
        while (_enumeration.hasMoreElements()) {
            String _initParameterName = (String)_enumeration.nextElement();
            log.info(
                "\t" + _initParameterName + " - " +
                    portletConfig.getInitParameter(_initParameterName));
        }
        log.info("PortletContext's attributes:");
        _enumeration =
            portletConfig.getPortletContext().getAttributeNames();
        while (_enumeration.hasMoreElements()) {
            String _attributeName = (String)_enumeration.nextElement();
            log.info(
                "\t" + _attributeName + " - " +
                    portletConfig.getPortletContext().
                        getAttribute(_attributeName));
        }
        log.info("PortletContext's init parameters:");
        _enumeration =
            portletConfig.getPortletContext().getInitParameterNames();
        while (_enumeration.hasMoreElements()) {
            String _initParameterName = (String)_enumeration.nextElement();
            log.info(
                "\t" + _initParameterName + " - " +
                    portletConfig.getPortletContext().
                        getInitParameter(_initParameterName));
        }
        log.info("PortletSession's attributes:");
        _enumeration =
            renderRequest.getPortletSession().getAttributeNames();
        while (_enumeration.hasMoreElements()) {
            String _attributeName = (String)_enumeration.nextElement();
            log.info(
                "\t" + _attributeName + " - " +
                    renderRequest.getPortletSession().
                        getAttribute(_attributeName));
        }
        log.info("RenderRequest's attributes:");
        _enumeration = renderRequest.getAttributeNames();
        while (_enumeration.hasMoreElements()) {
            String _attributeName = (String)_enumeration.nextElement();
            log.info(
                "\t" + _attributeName + " - " +
                    renderRequest.getAttribute(_attributeName));
        }
        log.info("RenderRequest's parameters:");
        _enumeration = renderRequest.getParameterNames();
        while (_enumeration.hasMoreElements()) {
            String _parameterName = (String)_enumeration.nextElement();
            log.info(
                "\t" + _parameterName + " - " +
                    renderRequest.getParameter(_parameterName));
        }
        log.info("RenderRequest's properties:");
        _enumeration = renderRequest.getPropertyNames();
        while (_enumeration.hasMoreElements()) {
            String _propertyName = (String)_enumeration.nextElement();
            log.info(
                "\t" + _propertyName + " - " +
                    renderRequest.getProperty(_propertyName));
        }
//        PortletSession _portletSession = renderRequest.getPortletSession();
//        String _iceFacesId =
//            (String)
//                _portletSession.getAttribute(
//                    ResponseStateManager.ICEFACES_ID_KEY,
//                    PortletSession.APPLICATION_SCOPE);
//        if (_iceFacesId == null) {
//            _iceFacesId = idGenerator.newIdentifier();
//            _portletSession.setAttribute(
//                ResponseStateManager.ICEFACES_ID_KEY,
//                _iceFacesId,
//                PortletSession.APPLICATION_SCOPE);
//        }
        renderRequest.setAttribute(
            "javax.servlet.include.request_uri",
            portletConfig.getInitParameter("default-view"));
        log.info("RenderRequest's attributes (after setting):");
        _enumeration = renderRequest.getAttributeNames();
        while (_enumeration.hasMoreElements()) {
            String _attributeName = (String)_enumeration.nextElement();
            log.info(
                "\t" + _attributeName + " - " +
                    renderRequest.getAttribute(_attributeName));
        }
        getPortletContext().getRequestDispatcher("/index.iface").
            include(renderRequest, renderResponse);
//        getPortletContext().getNamedDispatcher("Persistent Faces Servlet").
//            include(renderRequest, renderResponse);
    }
    
    protected PortletContext getPortletContext() {
        return portletConfig.getPortletContext();
    }
}
