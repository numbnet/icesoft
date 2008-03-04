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

import com.icesoft.faces.application.D2DViewHandler;
import com.icesoft.faces.application.StartupTime;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.util.CoreUtils;
import com.icesoft.faces.util.DOMUtils;
import com.icesoft.faces.webapp.http.common.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.Beans;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * <p><strong>DOMResponseWriter</strong> is a DOM specific implementation of
 * <code>javax.faces.context.ResponseWriter</code>.
 */
public class DOMResponseWriter extends ResponseWriter {
    private static final Log log = LogFactory.getLog(DOMResponseWriter.class);
    public static final String DOCTYPE_PUBLIC = "com.icesoft.doctype.public";
    public static final String DOCTYPE_SYSTEM = "com.icesoft.doctype.system";
    public static final String DOCTYPE_ROOT = "com.icesoft.doctype.root";
    public static final String DOCTYPE_OUTPUT = "com.icesoft.doctype.output";
    public static final String DOCTYPE_PRETTY_PRINTING = "com.icesoft.doctype.prettyprinting";

    private static DocumentBuilder DOCUMENT_BUILDER;

    static {
        try {
            DOCUMENT_BUILDER =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.error("Cannot acquire a DocumentBuilder", e);
        }
    }

    private static boolean isStreamWritingFlag = false;
    private final Map domContexts = new HashMap();
    private final Document document = DOCUMENT_BUILDER.newDocument();
    private final BridgeFacesContext context;
    private final DOMSerializer serializer;
    private final Configuration configuration;
    private final Collection jsCode;
    private final Collection cssCode;
    private Node cursor = document;

    public DOMResponseWriter(FacesContext context, DOMSerializer serializer, Configuration configuration, Collection jsCode, Collection cssCode) {
        this.serializer = serializer;
        this.configuration = configuration;
        this.jsCode = jsCode;
        this.cssCode = cssCode;
        try {
            this.context = (BridgeFacesContext) context;
        } catch (ClassCastException e) {
            throw new IllegalStateException(
                    "ICEfaces requires the PersistentFacesServlet. " +
                            "Please check your web.xml servlet mappings");
        }
        boolean streamWritingParam = configuration.getAttributeAsBoolean("streamWriting", false);
        isStreamWritingFlag = Beans.isDesignTime() || streamWritingParam;
    }

    Map getDomContexts() {
        return domContexts;
    }

    public Node getCursorParent() {
        return cursor;
    }

    public Document getDocument() {
        return document;
    }

    public String getContentType() {
        return "text/html; charset=UTF-8";
    }

    public String getCharacterEncoding() {
        return "UTF-8";
    }

    public void startDocument() throws IOException {
    }

    public void endDocument() throws IOException {
        if (!isStreamWriting()) {
            enhanceAndFixDocument();
            serializer.serialize(document);
        }
    }

    public void flush() throws IOException {
    }

    public void startElement(String name, UIComponent componentForElement)
            throws IOException {
        moveCursorOn(appendToCursor(document.createElement(name)));
    }

    public void endElement(String name) throws IOException {
        moveCursorOn(cursor.getParentNode());
    }

    public void writeAttribute(String name, Object value,
                               String componentPropertyName)
            throws IOException {
        //name.trim() because cardemo had a leading space in an attribute name
        //which made the DOM processor choke
        Attr attribute = document.createAttribute(name.trim());
        attribute.setValue(String.valueOf(value));
        appendToCursor(attribute);
    }

    public void writeURIAttribute(String name, Object value,
                                  String componentPropertyName)
            throws IOException {
        String stringValue = String.valueOf(value);
        if (stringValue.startsWith("javascript:")) {
            writeAttribute(name, stringValue, componentPropertyName);
        } else {
            writeAttribute(name, stringValue.replace(' ', '+'), componentPropertyName);
        }
    }

    public void writeComment(Object comment) throws IOException {
        appendToCursor(document.createComment(String.valueOf(comment)));
    }

    public void writeText(Object text, String componentPropertyName)
            throws IOException {
        appendToCursor(document.createTextNode(String.valueOf(text)));
    }

    public void writeText(char text[], int off, int len) throws IOException {
        appendToCursor(document.createTextNode(new String(text, off, len)));
    }

    public ResponseWriter cloneWithWriter(Writer writer) {
        //FIXME: This is a hack for DOM rendering but JSF currently clones the writer
        //just as the components are complete
        if (null != document) {
            try {
                endDocument();
            } catch (IOException e) {
                throw new IllegalStateException(e.toString());
            }
        }
        try {
            return new DOMResponseWriter(context, serializer, configuration, jsCode, cssCode);
        } catch (FacesException e) {
            throw new IllegalStateException();
        }
    }

    public void close() throws IOException {
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        appendToCursor(document.createTextNode(new String(cbuf, off, len)));
    }

    public void write(int c) throws IOException {
        appendToCursor(document.createTextNode(String.valueOf((char) c)));
    }

    public void write(String str) throws IOException {
        appendToCursor(document.createTextNode(str));
    }

    public void write(String str, int off, int len) throws IOException {
        appendToCursor(document.createTextNode(str.substring(off, len)));
    }

    private void enhanceAndFixDocument() {
        Element html = (Element) document.getDocumentElement();
        enhanceHtml(html = "html".equals(html.getTagName()) ? html : fixHtml());

        Element head = (Element) document.getElementsByTagName("head").item(0);
        enhanceHead(head == null ? fixHead() : head);

        Element body = (Element) document.getElementsByTagName("body").item(0);
        enhanceBody(body == null ? fixBody() : body);
    }

    private void enhanceHtml(Element html) {
        //add lang attribute
        Locale locale = context.getApplication().getViewHandler().calculateLocale(context);
        //id required for forwarded (server-side) redirects
        html.setAttribute("id", "document:html");
        html.setAttribute("lang", locale.getLanguage());
    }

    private void enhanceBody(Element body) {
        //id required for forwarded (server-side) redirects
        body.setAttribute("id", "document:body");
        Element iframe = document.createElement("iframe");
        body.insertBefore(iframe, body.getFirstChild());
        iframe.setAttribute("id", "history-frame");
        Object request = context.getExternalContext().getRequest();

        final String frameURI;
        //another "workaround" to resolve the iframe URI
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            if (httpRequest.getRequestURI() == null) {
                frameURI = "about:blank";
            } else {
                frameURI = CoreUtils.resolveResourceURL(FacesContext.getCurrentInstance(),
                        "/xmlhttp/blank");
            }
        } else {
            frameURI = CoreUtils.resolveResourceURL(FacesContext.getCurrentInstance(), "/xmlhttp/blank"); // ICE-2553
        }
        iframe.setAttribute("title", "Icefaces Redirect");
        iframe.setAttribute("src", frameURI);
        iframe.setAttribute("frameborder", "0");
        iframe.setAttribute("style",
                "z-index: 10000; visibility: hidden; width: 0; height: 0; position: absolute; opacity: 0.22; filter: alpha(opacity=22);");

        // TODO This is only meant to be a transitional focus retention(management) solution.
        String focusId = context.getFocusId();
        if (focusId != null && !focusId.equals("null")) {
            JavascriptContext.focus(context, focusId);
        }
        ViewHandler handler = context.getApplication().getViewHandler();
        Map session = context.getExternalContext().getSessionMap();
        String sessionIdentifier = context.getIceFacesId();
        String viewIdentifier = context.getViewNumber();
        String prefix = sessionIdentifier + ':' + viewIdentifier + ':';

        Element script = (Element) body.appendChild(document.createElement("script"));
        script.setAttribute("id", prefix + "dynamic-code");
        script.setAttribute("type", "text/javascript");
        String calls = JavascriptContext.getJavascriptCalls(context);
        script.appendChild(document.createTextNode(calls));

        ElementController.from(session).addInto(prefix, body);
        String contextPath = handler.getResourceURL(context, "/");
        String asyncServerContextPath = "/" + configuration.getAttribute("blockingRequestHandlerContext", configuration.getAttribute("asyncServerContext", contextPath.replaceAll("/", ""))) + "/";
        String configurationID = prefix + "configuration-script";
        //add viewIdentifier property to the container element ("body" for servlet env., any element for the portlet env.)
        String startupScript =
                "if (!window.sessions) window.sessions = []; window.sessions.push('" + sessionIdentifier + "');\n" +
                        "var container = '" + configurationID + "'.asExtendedElement().findContainerFor('bridge');\n" +
                        "container.bridge = new Ice.Community.Application({" +
                        "session: '" + sessionIdentifier + "'," +
                        "view: " + viewIdentifier + "," +
                        "synchronous: " + configuration.getAttribute("synchronousUpdate", "false") + "," +
                        "redirectURI: " + configuration.getAttribute("connectionLostRedirectURI", "null") + "," +
                        "connection: {" +
                        "context: {" +
                        "current: '" + contextPath + "'," +
                        "async: '" + asyncServerContextPath + "'}," +
                        "timeout: " + configuration.getAttributeAsLong("connectionTimeout", 30000) + "," +
                        "heartbeat: {" +
                        "interval: " + configuration.getAttributeAsLong("heartbeatInterval", 50000) + "," +
                        "timeout: " + configuration.getAttributeAsLong("heartbeatTimeout", 30000) + "," +
                        "retries: " + configuration.getAttributeAsLong("heartbeatRetries", 3) +
                        "}" +
                        "}" +
                        "}, container);";

        Element configurationElement = (Element) body.appendChild(document.createElement("script"));
        configurationElement.setAttribute("id", configurationID);
        configurationElement.setAttribute("type", "text/javascript");
        configurationElement.appendChild(document.createTextNode(startupScript));
        body.appendChild(configurationElement);

        Element noscript = (Element) body.appendChild(document.createElement("noscript"));
        Element noscriptMeta = (Element) noscript.appendChild(document.createElement("meta"));
        noscriptMeta.setAttribute("http-equiv", "refresh");
        noscriptMeta.setAttribute("content", "0;url=" + handler.getResourceURL(context, "/xmlhttp/javascript-blocked"));

        String markerID = prefix + "marker-script";
        Element markerElement = (Element) body.appendChild(document.createElement("script"));
        markerElement.setAttribute("id", markerID);
        markerElement.setAttribute("type", "text/javascript");
        markerElement.appendChild(document.createTextNode("'" + markerID + "'.asElement().parentNode.bridge = 'placeholder';"));
        body.insertBefore(markerElement, body.getFirstChild());

        if (context.isContentIncluded()) {
            Element element = (Element) body.insertBefore(document.createElement("div"), markerElement);
            element.setAttribute("style", "display: none;");
            appendContentReferences(element);
        }
    }

    private void enhanceHead(Element head) {
        Element meta = (Element) head.appendChild(document.createElement("meta"));
        meta.setAttribute("name", "icefaces");
        meta.setAttribute("content", "Rendered by ICEFaces D2D");

        Element noscript = (Element) head.appendChild(document.createElement("noscript"));
        Element noscriptMeta = (Element) noscript.appendChild(document.createElement("meta"));
        noscriptMeta.setAttribute("http-equiv", "refresh");
        String jsBlockedURL = context.getApplication().getViewHandler().getResourceURL(context, "/xmlhttp/javascript-blocked");
        noscriptMeta.setAttribute("content", "0;jsBlockedURL=" + jsBlockedURL);

        if (!context.isContentIncluded()) {
            appendContentReferences(head);
        }
    }

    private void appendContentReferences(Element container) {
        //load libraries
        Collection libs = new ArrayList();
        if (context.getExternalContext().getInitParameter(D2DViewHandler.INCLUDE_OPEN_AJAX_HUB) != null) {
            libs.add("/xmlhttp/openajax.js");
        }
        libs.add("/xmlhttp" + StartupTime.getStartupInc() + "icefaces-d2d.js");
        //todo: refactor how external libraries are loaded into the bridge; always include extra libraries for now
        libs.add("/xmlhttp" + StartupTime.getStartupInc() + "ice-extras.js");

        String[] componentLibs = JavascriptContext.getIncludedLibs(context);
        for (int i = 0; i < componentLibs.length; i++) {
            String componentLib = componentLibs[i];
            if (!libs.contains(componentLib)) {
                libs.add(componentLib);
            }
        }

        libs.addAll(jsCode);

        ViewHandler handler = context.getApplication().getViewHandler();
        Iterator libIterator = libs.iterator();
        while (libIterator.hasNext()) {
            String lib = (String) libIterator.next();
            Element script = (Element) container.appendChild(document.createElement("script"));
            script.setAttribute("type", "text/javascript");
            script.setAttribute("src", handler.getResourceURL(context, lib));
        }

        Iterator cssIterator = cssCode.iterator();
        while (cssIterator.hasNext()) {
            String css = (String) cssIterator.next();
            Element link = (Element) container.appendChild(document.createElement("link"));
            link.setAttribute("rel", "stylesheet");
            link.setAttribute("type", "text/css");
            link.setAttribute("href", handler.getResourceURL(context, css));
        }
    }

    private Element fixHtml() {
        Element root = document.getDocumentElement();
        Element html = document.createElement("html");
        document.replaceChild(html, root);
        html.appendChild(root);

        return html;
    }

    private Element fixBody() {
        Element html = document.getDocumentElement();
        Element body = document.createElement("body");
        NodeList children = html.getChildNodes();
        int length = children.getLength();
        Node[] nodes = new Node[length];
        //copy the children first, since NodeList is live
        for (int i = 0; i < nodes.length; i++) nodes[i] = children.item(i);
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            if (!(node instanceof Element &&
                    "head".equals(((Element) node).getTagName())))
                body.appendChild(node);
        }
        html.appendChild(body);

        return body;
    }

    private Element fixHead() {
        Element html = document.getDocumentElement();
        Element head = document.createElement("head");
        html.insertBefore(head, html.getFirstChild());

        return head;
    }

    /**
     * This method sets the write cursor for DOM modifications.  Subsequent DOM
     * modifications will take place below the cursor element.
     *
     * @param cursorParent parent node for subsequent modifications to the DOM
     */
    protected void setCursorParent(Node cursorParent) {
        this.cursor = cursorParent;
    }

    public static boolean isStreamWriting() {
        return isStreamWritingFlag;
    }

    private void moveCursorOn(Node node) {
        if (log.isTraceEnabled()) {
            log.trace("moving cursor on " + DOMUtils.toDebugString(node));
        }
        cursor = node;
    }

    private Node appendToCursor(Node node) {
        try {
            if (log.isTraceEnabled()) {
                log.trace("Appending " + DOMUtils.toDebugString(node) + " into " + DOMUtils.toDebugString(cursor));
            }
            return cursor.appendChild(node);
        } catch (DOMException e) {
            String message = "Failed to append " + DOMUtils.toDebugString(node) + " into " + DOMUtils.toDebugString(cursor);
            log.error(message);
            throw new RuntimeException(message, e);
        }
    }

    private Node appendToCursor(Attr node) {
        try {
            if (log.isTraceEnabled()) {
                log.trace("Appending " + DOMUtils.toDebugString(node) + " into " + DOMUtils.toDebugString(cursor));
            }
            return ((Element) cursor).setAttributeNode(node);
        } catch (DOMException e) {
            String message = "Failed to append " + DOMUtils.toDebugString(node) + " into " + DOMUtils.toDebugString(cursor);
            log.error(message);
            throw new RuntimeException(message, e);
        } catch (ClassCastException e) {
            String message = "The cursor is not an element: " + DOMUtils.toDebugString(cursor);
            log.error(message);
            throw new RuntimeException(message, e);
        }
    }
}
