/*
 * limited scope mock objects test 
 */
package com.icesoft.faces.mock.test;

import com.sun.faces.mock.MockApplication;
import com.sun.faces.mock.MockExternalContext;
import com.sun.faces.mock.MockFacesContext;
import com.sun.faces.mock.MockHttpServletRequest;
import com.sun.faces.mock.MockHttpServletResponse;
import com.sun.faces.mock.MockHttpSession;
import com.sun.faces.mock.MockLifecycle;
import com.sun.faces.mock.MockServletConfig;
import com.sun.faces.mock.MockServletContext;
import com.sun.faces.mock.MockViewHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import junit.framework.TestCase;

/**
 *
 * @author fye
 */
public class MockTestCase extends TestCase {

    private Application application;
    private ViewHandler viewHandler;
    private ServletConfig servletConfig;
    private ExternalContext externalContext;
    private FacesContext facesContext;
    private Lifecycle lifecycle;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private ServletContext servletContext;
    private HttpSession httpSession;

    //TODO
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    protected void defaultMockContainer() {

        servletContext = new MockServletContext();
        servletConfig = new MockServletConfig(servletContext);
        httpSession = new MockHttpSession();

        httpServletRequest = new MockHttpServletRequest(httpSession);
        httpServletResponse = new MockHttpServletResponse();

        String appFactoryName = "com.sun.faces.mock.MockApplicationFactory";
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY, appFactoryName);

        String renderKitFactoryName = "com.sun.faces.mock.MockRenderKitFactory";
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY, renderKitFactoryName);

        externalContext = new MockExternalContext(servletContext, httpServletRequest, httpServletResponse);
        lifecycle = new MockLifecycle();

        facesContext = new MockFacesContext(externalContext, lifecycle);

        ApplicationFactory applicationFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = (MockApplication) applicationFactory.getApplication();
        viewHandler = new MockViewHandler();
        application.setViewHandler(new MockViewHandler());

    //TODO:
//        RenderKitFactory renderKitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
//        RenderKit renderkit = new MockRenderKit();
//        renderKitFactory.addRenderKit(renderKitFactoryName, renderkit);
    }

    //TODO: other containers
    @Override
    protected void setUp() throws Exception {

        defaultMockContainer();
    }

    @Override
    protected void tearDown() throws Exception {
        application = null;
        viewHandler = null;
        servletConfig = null;
        externalContext = null;
        facesContext = null;
        lifecycle = null;
        httpServletRequest = null;
        httpServletResponse = null;
        servletContext = null;
        httpSession = null;
        properties = null;
    }

    public ViewHandler getViewHandler() {
        return viewHandler;
    }

    //todo External Context
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    protected Object invokePrivateMethod(String methodName,
            Class[] params,
            Object[] args,
            Class className,
            Object invocationTarget) {
        try {
            Method method = className.getDeclaredMethod(methodName, params);
            method.setAccessible(true);
            return method.invoke(invocationTarget, args);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MockTestCase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(MockTestCase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(MockTestCase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MockTestCase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected void booleanAttribute(UIComponent uiComponent, String attributeName, boolean expectedValue) {

        Object value = uiComponent.getAttributes().get(attributeName);
        String message = null;
        if (value != null) {
            Boolean booleanValue = (Boolean) value;
            message = " component=" + uiComponent + " property " + attributeName + " value=" + booleanValue.toString();
            Logger.getLogger(CommandButtonSaveStateTest.class.getName()).log(Level.INFO, message);
        } else {
            message = " component=" + uiComponent + " property " + attributeName + "=null";
            Logger.getLogger(CommandButtonSaveStateTest.class.getName()).log(Level.INFO, message);
        }
    }
}
