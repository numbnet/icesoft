/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
 
/*
 * delegating to MockViewHandler
 */
package com.icesoft.faces.mock.test;

import com.icesoft.faces.application.D2DViewHandler;
import com.icesoft.faces.mock.test.container.MockExternalContext;
import com.icesoft.faces.mock.test.container.MockServletConfig;
import com.icesoft.faces.mock.test.container.MockServletContext;
import com.icesoft.faces.mock.test.container.MockViewHandler;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.mock.MockApplication;
import com.sun.faces.mock.MockFacesContext;
import com.sun.faces.mock.MockHttpServletRequest;
import com.sun.faces.mock.MockHttpServletResponse;
import com.sun.faces.mock.MockHttpSession;
import com.sun.faces.mock.MockLifecycle;
import com.sun.faces.mock.MockRenderKit;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DelegatingViewHandlerTest extends TestCase {

    public static Test suite() {
        return new TestSuite(DelegatingViewHandlerTest.class);
    }

    //Mock Container
    private MockApplication application;
    private D2DViewHandler viewHandler;
    private MockServletConfig servletConfig;
    private MockExternalContext externalContext;
    private MockFacesContext facesContext;
    private Lifecycle lifecycle;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private MockServletContext servletContext;
    private HttpSession httpSession;

    public void testViewHandler() {

        //Servlet API 2.4
        servletContext = new MockServletContext();
        servletConfig = new MockServletConfig(servletContext);
        httpSession = new MockHttpSession(servletContext);

        httpServletRequest = new MockHttpServletRequest(httpSession);
        httpServletResponse = new MockHttpServletResponse();

        String appFactoryName = "com.sun.faces.mock.MockApplicationFactory";
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY, appFactoryName);

        String renderKitFactoryName = "com.sun.faces.mock.MockRenderKitFactory";
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY, renderKitFactoryName);

        externalContext = new MockExternalContext(servletContext, httpServletRequest, httpServletResponse);

        Map myRequestParameterMap = new HashMap();
        myRequestParameterMap.put("clientid_inputText", "event");
        externalContext.setRequestParameterMap(myRequestParameterMap);
        lifecycle = new MockLifecycle();

        facesContext = new MockFacesContext(externalContext, lifecycle);

        ApplicationFactory applicationFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = (MockApplication) applicationFactory.getApplication();
        facesContext.setApplication(application);

        //TODO:
        WebConfiguration webConfig = WebConfiguration.getInstance(servletContext);

        //viewHandler = instanceViewHandler();
        viewHandler = delegatingViewHandler();

        if (viewHandler != null) {
            try {
                application.setViewHandler(viewHandler);
                //TODO verify call methods
                UIViewRoot myViewRoot = viewHandler.createView(facesContext, "dummyViewId");
                viewHandler.renderView(facesContext, myViewRoot);
            } catch (Exception e) {
                //Logger.getLogger(DelegatingViewHandlerTest.class.getName()).log(Level.INFO, "failed = " + e.getMessage());
                String message = "render exception details:"+e.getMessage();
                fail(message);
            }
        }


    //TODO:
//        RenderKitFactory renderKitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
//        RenderKit renderkit = new MockRenderKit();
//        renderKitFactory.addRenderKit(renderKitFactoryName, renderkit);
//        renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT, renderkit);
    }

    private D2DViewHandler instanceViewHandler() {
        try {
            D2DViewHandler d2dViewHandler = new D2DViewHandler();
            return d2dViewHandler;
        } catch (Exception e) {
            //Logger.getLogger(DelegatingViewHandlerTest.class.getName()).log(Level.INFO, "failed = " + e.getMessage());
            String message = "can not init instance of D2DViewHandler details:" + e.getMessage();
            fail(message);
        }
        return null;
    }

    private D2DViewHandler delegatingViewHandler() {
        try {
            D2DViewHandler d2dViewHandler = new D2DViewHandler(new MockViewHandler());
            return d2dViewHandler;
        } catch (Exception e) {
            String message = "can not init instance of D2DViewHandler with delegated ViewHandler details:" + e.getMessage();
            fail(message);
        //Logger.getLogger(DelegatingViewHandlerTest.class.getName()).log(Level.INFO, "failed = " + e.getMessage());
        }
        return null;
    }
}
