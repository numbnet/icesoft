package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.application.D2DViewHandler;
import com.icesoft.faces.context.BridgeFacesContext;
import com.icesoft.faces.context.DOMResponseWriter;
import com.icesoft.faces.context.DOMSerializer;
import com.icesoft.faces.context.NormalModeSerializer;
import com.icesoft.faces.context.PushModeSerializer;
import com.icesoft.faces.el.ELContextImpl;
import com.icesoft.faces.webapp.command.CommandQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.el.ELContext;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

//for now extend BridgeFacesContext since there are so many bloody 'instanceof' tests
public class ServletFacesContext extends BridgeFacesContext {
    private static final Log log = LogFactory.getLog(ServletFacesContext.class);
    private Application application;
    private ExternalContext externalContext;
    private HashMap faceMessages = new HashMap();
    private boolean renderResponse;
    private boolean responseComplete;
    private ResponseStream responseStream;
    private DOMResponseWriter responseWriter;
    private DOMSerializer domSerializer;
    private UIViewRoot viewRoot;
    private String iceFacesId;
    private String viewNumber;
    private CommandQueue commandQueue;

    public ServletFacesContext(ExternalContext externalContext, String view, String icefacesID, CommandQueue commandQueue) {
        setCurrentInstance(this);
        setExternalContext(externalContext);
        this.viewNumber = view;
        this.iceFacesId = icefacesID;
        this.commandQueue = commandQueue;
        this.application = ((ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY)).getApplication();
        this.externalContext = externalContext;
        this.switchToNormalMode();
    }

    public void setCurrentInstance() {
        setCurrentInstance(this);
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
        this.externalContext = externalContext;
    }

    public ELContext getELContext() {
        ELContext elContext = new ELContextImpl(application);
        elContext.putContext(FacesContext.class, this);
        UIViewRoot root = getViewRoot();
        if (null != root) {
            elContext.setLocale(root.getLocale());
        }

        return elContext;
    }

    public FacesMessage.Severity getMaximumSeverity() {
        throw new UnsupportedOperationException();
    }

    /**
     * gets all FacesMessages whether or not associatted with clientId.
     *
     * @return list of FacesMessages
     */
    public Iterator getMessages() {
        return faceMessages.values().iterator();
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
        try {
            return ((Vector) faceMessages.get(clientId)).iterator();
        } catch (NullPointerException e) {
            if (log.isDebugEnabled()) {
                log.debug("Cannot find clientId " + clientId +
                          "from facesMessages");
            }
            return Collections.EMPTY_LIST.iterator();
        }
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
        //do nothing.
    }

    public ResponseWriter createAndSetResponseWriter() throws IOException {
        return responseWriter = new DOMResponseWriter(this, domSerializer);
    }

    public void switchToNormalMode() {
        try {
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            Writer writer = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            domSerializer = new NormalModeSerializer(this, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchToPushMode() {
        //todo: pull document in this class
        domSerializer = new PushModeSerializer(responseWriter.getDocument(), commandQueue);
    }

    public UIViewRoot getViewRoot() {
        if (null == viewRoot) {
            Map contextServletTable = D2DViewHandler.getContextServletTable(this);
            if (null != contextServletTable) {
                viewRoot = (UIViewRoot) contextServletTable
                        .get(DOMResponseWriter.RESPONSE_VIEWROOT);
            }
        }

        return this.viewRoot;
    }

    public void setViewRoot(UIViewRoot viewRoot) {
        //pointing this FacesContext to the new view
        Map contextServletTable = D2DViewHandler.getContextServletTable(this);
        if (null != contextServletTable) {
            if (viewRoot != null) {
                contextServletTable
                        .put(DOMResponseWriter.RESPONSE_VIEWROOT, viewRoot);
            } else {
                contextServletTable.remove(DOMResponseWriter.RESPONSE_VIEWROOT);
            }
        }
        responseWriter = null;
        this.viewRoot = viewRoot;
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
        return (String) (map.containsKey("focus") ? map.get("focus") : "");
    }

    /**
     * Sets the id of the Element that should get focus in the browser.
     */
    public void setFocusId(String focusId) {
        externalContext.getRequestParameterMap().put("focus", focusId);
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
    }

    public void renderResponse() {
        this.renderResponse = true;
    }

    public void responseComplete() {
        this.responseComplete = true;
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
        setCurrentInstance(null);
    }

    public void applyBrowserDOMChanges() {
        if (responseWriter == null) return;
        Document document = responseWriter.getDocument();
        if (document == null) return;
        Map parameters = externalContext.getRequestParameterValuesMap();

        NodeList inputElements = document.getElementsByTagName("input");
        int inputElementsLength = inputElements.getLength();
        for (int i = 0; i < inputElementsLength; i++) {
            Element inputElement = (Element) inputElements.item(i);
            String id = inputElement.getAttribute("id");
            if (parameters.containsKey(id)) {
                String value = ((String[]) parameters.get(id))[0];
                inputElement.setAttribute("value", value);
            }
        }

        NodeList textareaElements = document.getElementsByTagName("textarea");
        int textareaElementsLength = textareaElements.getLength();
        for (int i = 0; i < textareaElementsLength; i++) {
            Element textareaElement = (Element) textareaElements.item(i);
            String id = textareaElement.getAttribute("id");
            if (parameters.containsKey(id)) {
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
            if (parameters.containsKey(id)) {
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
}
