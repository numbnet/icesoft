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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 *
 */
package com.icesoft.faces.context;

import com.icesoft.faces.application.ViewHandlerProxy;
import com.icesoft.faces.el.ELContextImpl;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.Reload;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.core.ResourceDispatcher;
import com.icesoft.jasper.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.el.ELContext;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

//for now extend BridgeFacesContext since there are so many 'instanceof' tests
public class BridgeFacesContext extends FacesContext implements ResourceRegistry {
    private static final Log log = LogFactory.getLog(BridgeFacesContext.class);
    //todo: factor out the page template extension pattern to reuse it MainServlet.java as well (maybe in configuration)
    private static final Pattern PageTemplatePattern = Pattern.compile(".*(\\.iface$|\\.jsf$|\\.faces$|\\.jsp$|\\.jspx$|\\.xhtml$|\\.seam$)");
    private Application application;
    private BridgeExternalContext externalContext;
    private HashMap faceMessages = new HashMap();
    private FacesMessage.Severity maxSeverity;
    private boolean renderResponse;
    private boolean responseComplete;
    private ResponseStream responseStream;
    private ResponseWriter responseWriter;
    private DOMSerializer domSerializer;
    private UIViewRoot viewRoot;
    private String iceFacesId;
    private String viewNumber;
    private CommandQueue commandQueue;
    private Configuration configuration;
    private Collection jsCodeURIs = new ArrayList();
    private Collection cssRuleURIs = new ArrayList();
    private ResourceDispatcher resourceDispatcher;
    private ELContext elContext;

    public BridgeFacesContext(BridgeExternalContext externalContext, String view, String icefacesID, CommandQueue commandQueue, Configuration configuration, ResourceDispatcher resourceDispatcher) {
        setCurrentInstance(this);
        this.externalContext = externalContext;
        this.viewNumber = view;
        this.iceFacesId = icefacesID;
        this.commandQueue = commandQueue;
        this.configuration = configuration;
        this.application = ((ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY)).getApplication();
        this.externalContext = externalContext;
        this.resourceDispatcher = resourceDispatcher;
        this.switchToNormalMode();
    }

    public void setCurrentInstance() {
        setCurrentInstance(this);
    }

    public void resetCurrentInstance() {
        FacesContext.setCurrentInstance(null);
    }


    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Iterator getClientIdsWithMessages() {
        return faceMessages.keySet().iterator();
    }

    public ExternalContext getExternalContext() {
        return this.externalContext;
    }

    public void setExternalContext(ExternalContext externalContext) {
        //do nothing
    }

    public ELContext getELContext() {
        if (null != elContext) {
            return elContext;
        }
        elContext = new ELContextImpl(application);
        elContext.putContext(FacesContext.class, this);
        UIViewRoot root = getViewRoot();
        if (null != root) {
            elContext.setLocale(root.getLocale());
        }

        return elContext;
    }

    public FacesMessage.Severity getMaximumSeverity() {
        return maxSeverity;
    }

    /**
     * gets all FacesMessages whether or not associatted with clientId.
     *
     * @return list of FacesMessages
     */
    public Iterator getMessages() {

        // Jira #1358 The hashmap contains vectors of FacesMessages, not FacesMessages
        // See following method.
        ArrayList buffer = new ArrayList();
        Iterator i = faceMessages.values().iterator();
        while (i.hasNext()) {
            buffer.addAll((Vector) i.next());
        }
        return buffer.iterator();
    }

    /**
     * returns list of FacesMessages associated with a clientId. If client id is
     * null, then return all FacesMessages which are not assocaited wih any
     * clientId
     *
     * @param clientId
     * @return list of FacesMessages
     */
    public Iterator getMessages(String clientId) {
        Object obj = faceMessages.get(clientId);
        if (obj == null) {
            if (log.isTraceEnabled()) {
                log.trace(clientId + " has no FacesMessages");
            }
            return Collections.EMPTY_LIST.iterator();
        }

        return ((Vector) obj).iterator();
    }

    public RenderKit getRenderKit() {
        UIViewRoot viewRoot = getViewRoot();
        if (null == viewRoot) {
            return (null);
        }
        String renderKitId = viewRoot.getRenderKitId();
        if (null == renderKitId) {
            return (null);
        }

        RenderKitFactory renderKitFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderKitFactory.getRenderKit(this, renderKitId);
        return (renderKit);
    }

    public boolean getRenderResponse() {
        return this.renderResponse;
    }

    public boolean getResponseComplete() {
        return this.responseComplete;
    }

    public ResponseStream getResponseStream() {
        return this.responseStream;
    }

    public void setResponseStream(ResponseStream responseStream) {
        this.responseStream = responseStream;
    }

    public ResponseWriter getResponseWriter() {
        return responseWriter;
    }

    public void setResponseWriter(ResponseWriter responseWriter) {
        this.responseWriter = responseWriter;
    }

    public ResponseWriter createAndSetResponseWriter() throws IOException {
        return responseWriter = new DOMResponseWriter(this, domSerializer, configuration, jsCodeURIs, cssRuleURIs);
    }

    public void switchToNormalMode() {
        try {
            domSerializer = new NormalModeSerializer(this, externalContext.getWriter("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchToPushMode() {
        //todo: pull document in this class

        // Jira #1330.
        // Normally, just masking a null object just leads to
        // a bunch of further null testing later. Except, at the time of writing,
        // a) there is no (well, not much of a) later, and
        // b) For the problem at hand, there's no easy way to create a Noop responseWriter
        //
        // The problem arises when Seam uses a Get request to logout. A Seam link tag
        // is written with the actionMethod hack to get the Identity object to logout.
        // As a result of the Get, a new ViewRoot is created, and in our code, the
        // createAndSetResponseWriter method is not called until the renderResponse phase,
        // but when the result of a Seam actionMethod hack is a redirect, renderResponse
        // is not called, and the responseWriter will not have a value.
        //
        // Trying to create a Noop DomResponseWriter is problematic since the constructor
        // of DRW does lots of initialization which needs something more than can
        // be faked. Look in the initialize method in the DOMResponseWriter class
        //
        if (responseWriter != null) {
            Document document = ((DOMResponseWriter) responseWriter).getDocument();
            domSerializer = new PushModeSerializer(document, commandQueue, viewNumber);
        }
    }

    public UIViewRoot getViewRoot() {
        if (externalContext.isSeamLifecycleShortcut()) {
            //ViewRoot and attributes being cached interferes with PAGE scope
            return null;
        }

        return this.viewRoot;
    }

    public void setViewRoot(UIViewRoot viewRoot) {
        final String path = viewRoot.getViewId();
        if (PageTemplatePattern.matcher(path).matches()) {
            //pointing this FacesContext to the new view
            responseWriter = null;
            this.viewRoot = viewRoot;
        } else {
            commandQueue.put(new Reload(viewNumber));
            application.setViewHandler(new SwitchViewHandler(application.getViewHandler(), path));
        }
    }

    public String getIceFacesId() {
        return iceFacesId;
    }

    /**
     * Return the unique identifier associated with each browser window
     * associated with a single user.
     */
    public String getViewNumber() {
        return viewNumber;
    }

    /**
     * Return the id of the Element that currently has focus in the browser.
     *
     * @return String
     */
    public String getFocusId() {
        Map map = externalContext.getRequestParameterMap();
        return (String) (map.containsKey("ice.focus") ? map.get("ice.focus") : "");
    }

    /**
     * Sets the id of the Element that should get focus in the browser.
     */
    public void setFocusId(String focusId) {
        externalContext.getRequestParameterMap().put("ice.focus", focusId);
    }

    /**
     * add a FacesMessage to the set of message associated with the clientId, if
     * clientId is not null.
     *
     * @param clientId
     * @param message
     */
    public void addMessage(String clientId, FacesMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("Message is null");
        }
        if (faceMessages.containsKey(clientId)) {
            ((Vector) faceMessages.get(clientId)).addElement(message);
        } else {
            Vector vector = new Vector();
            vector.add(message);
            faceMessages.put(clientId, vector);
        }
        if (maxSeverity == null ||
                message.getSeverity().getOrdinal() > maxSeverity.getOrdinal()) {
            maxSeverity = message.getSeverity();
        }
    }

    public void renderResponse() {
        this.renderResponse = true;
    }

    public void responseComplete() {
        this.responseComplete = true;
    }

    public void resetRenderResponse() {
        this.renderResponse = false;
    }

    /**
     * Necessary for ICE-2478. This method should only be called from an
     * environment where server push is active. For normal JSF lifecycle
     * execution, once the flag is set it should remain set.
     */
    public void resetResponseComplete() {
        this.responseComplete = false;
    }

    public static boolean isThreadLocalNull() {
        return getCurrentInstance() == null;
    } 

    /**
     * The release() found in FacesContextImpl is more comprehensive: since they
     * blow away the context instance after a response, they null/false out much
     * more than we do. We chose to keep the context instance around across
     * requests so we need to keep some of our state intact.
     */
    public void release() {
        faceMessages.clear();
        renderResponse = false;
        responseComplete = false;
        //Spring Web Flow 2 releases the FacesContext in between lifecycle
        //phases
        if (com.icesoft.util.SeamUtilities.isSpring2Environment()) {
            this.viewRoot = null;
        }
        // ICE-2807 clear threadLocal references
        setCurrentInstance(null);
    }

    public void dispose() {
    }

    public void applyBrowserDOMChanges() {
        if (responseWriter == null) return;
        Document document = ((DOMResponseWriter) responseWriter).getDocument();
        if (document == null) return;
        Map parameters = externalContext.getRequestParameterValuesMap();

        NodeList inputElements = document.getElementsByTagName("input");
        int inputElementsLength = inputElements.getLength();
        for (int i = 0; i < inputElementsLength; i++) {
            Element inputElement = (Element) inputElements.item(i);
            String id = inputElement.getAttribute("id");
            if (!"".equals(id) && parameters.containsKey(id)) {
                String value = ((String[]) parameters.get(id))[0];
                //empty string is implied (default) when 'value' attribute is missing
                if (!"".equals(value) && inputElement.hasAttribute("value")) {
                    inputElement.setAttribute("value", value);
                }
            }
        }

        NodeList textareaElements = document.getElementsByTagName("textarea");
        int textareaElementsLength = textareaElements.getLength();
        for (int i = 0; i < textareaElementsLength; i++) {
            Element textareaElement = (Element) textareaElements.item(i);
            String id = textareaElement.getAttribute("id");
            if (!"".equals(id) && parameters.containsKey(id)) {
                String value = ((String[]) parameters.get(id))[0];
                textareaElement.getFirstChild()
                        .setNodeValue(value);//set value on the Text node
            }
        }

        NodeList selectElements = document.getElementsByTagName("select");
        int selectElementsLength = selectElements.getLength();
        for (int i = 0; i < selectElementsLength; i++) {
            Element selectElement = (Element) selectElements.item(i);
            String id = selectElement.getAttribute("id");
            if (!"".equals(id) && parameters.containsKey(id)) {
                List values = Arrays.asList((String[]) parameters.get(id));

                NodeList optionElements =
                        selectElement.getElementsByTagName("option");
                int optionElementsLength = optionElements.getLength();
                for (int j = 0; j < optionElementsLength; j++) {
                    Element optionElement = (Element) optionElements.item(j);
                    if (values.contains(optionElement.getAttribute("value"))) {
                        optionElement.setAttribute("selected", "selected");
                    } else {
                        optionElement.removeAttribute("selected");
                    }
                }
            }
        }
    }

    public URI loadJavascriptCode(final Resource resource) {
        String uri = resourceDispatcher.registerResource("text/javascript", resource).toString();
        if (!jsCodeURIs.contains(uri)) {
            jsCodeURIs.add(uri);
        }
        return resolve(uri);
    }

    public URI loadJavascriptCode(Resource resource, ResourceLinker.Handler linkerHandler) {
        String uri = resourceDispatcher.registerResource("text/javascript", resource, linkerHandler).toString();
        if (!jsCodeURIs.contains(uri)) {
            jsCodeURIs.add(uri);
        }
        return resolve(uri);
    }

    public URI loadCSSRules(Resource resource) {
        String uri = resourceDispatcher.registerResource("text/css", resource).toString();
        if (!cssRuleURIs.contains(uri)) {
            cssRuleURIs.add(uri);
        }
        return resolve(uri);
    }


    public URI loadCSSRules(Resource resource, ResourceLinker.Handler linkerHandler) {
        String uri = resourceDispatcher.registerResource("text/css", resource, linkerHandler).toString();
        if (!cssRuleURIs.contains(uri)) {
            cssRuleURIs.add(uri);
        }
        return resolve(uri);
    }

    public URI registerResource(String mimeType, Resource resource) {
        return resolve(resourceDispatcher.registerResource(mimeType, resource).toString());
    }

    public URI registerResource(String mimeType, Resource resource, ResourceLinker.Handler linkerHandler) {
        return resolve(resourceDispatcher.registerResource(mimeType, resource, linkerHandler).toString());
    }

    public URI registerNamedResource(String name, Resource resource) {
        return resolve(resourceDispatcher.registerNamedResource(name, resource).toString());
    }

    public URI registerNamedResource(String name, Resource resource, ResourceLinker.Handler linkerHandler) {
        return resolve(resourceDispatcher.registerNamedResource(name, resource, linkerHandler).toString());
    }

    private URI resolve(String uri) {
        return URI.create(application.getViewHandler().getResourceURL(this, uri));
    }

    /**
     * Check the delegation chain for a BridgeFacesContext wrapped by another
     *
     * @param facesContext return BridgeFacesContext (if found) or input FacesContext
     */
    public static FacesContext unwrap(FacesContext facesContext) {
        if (facesContext instanceof BridgeFacesContext) {
            return facesContext;
        }
        FacesContext result = facesContext;
        try {
            Method delegateMethod = facesContext.getClass()
                    .getDeclaredMethod("getDelegate", new Class[]{});
            delegateMethod.setAccessible(true);
            Object delegate = delegateMethod
                    .invoke(facesContext, (Object[]) null);
            if (delegate instanceof BridgeFacesContext) {
                result = (FacesContext) delegate;
                if (log.isDebugEnabled()) {
                    log.debug("BridgeFacesContext delegate of " + facesContext);
                }
            }
        } catch (Exception e) {
        }

        return result;
    }

    private class DispatchingViewHandler extends ViewHandlerProxy {
        private final String path;

        public DispatchingViewHandler(ViewHandler originalHandler, String path) {
            super(originalHandler);
            this.path = path;
        }

        public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
            application.setViewHandler(handler);
            externalContext.dispatch(path);
        }
    }

    private class SwitchViewHandler extends ViewHandlerProxy {
        private final String path;

        public SwitchViewHandler(ViewHandler originalHandler, String path) {
            super(originalHandler);
            this.path = path;
        }

        public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
            application.setViewHandler(new DispatchingViewHandler(handler, path));
            handler.renderView(context, viewToRender);
        }
    }


    public boolean isContentIncluded() {
        //TODO - assuming we can handle portlets just like includes, then
        //we can probably reduce the attributes that we check for.  We need
        //to be specific about when to use request URI and when to use servlet
        //path.
        Map requestMap = externalContext.getRequestMap();
        String frag = (String) requestMap.get(Constants.INC_REQUEST_URI);
        if (log.isDebugEnabled()) {
            log.debug(Constants.INC_REQUEST_URI + " = " + frag);
        }
        if (frag != null) {
            return true;
        }

        frag = (String) requestMap.get(Constants.INC_SERVLET_PATH);
        if (log.isDebugEnabled()) {
            log.debug(Constants.INC_SERVLET_PATH + " = " + frag);
        }
        if (frag != null) {
            return true;
        }

        //This type of check should no longer be required.  If we need
        //to put a portlet specific attribute back in, then we should
        //define our own.
        frag = (String) requestMap.get("com.sun.faces.portlet.INIT");
        if (frag != null) {
            return true;
        }

        return false;
    }
}
