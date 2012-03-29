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

package com.icesoft.faces.webapp.http.portlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

/**
 * http://jira.icesoft.org/browse/ICE-6929
 *
 * When a portlet is first rendered, the initial request/response objects returned by ExternalContext
 * methods are valid and can be used normally.  Subsequent Ajax requests to the same view typically
 * occur in what we refer to as "extended request scope".  There are a couple of implications with these
 * Ajax requests:
 *
 * 1) They don't go through the portal container so are therefore HttpServletRequests rather than one
 * of the PortletRequest types.
 *
 * 2) As part of the fix for ICE-6197 to deal with memory leaks, we release the reference to the response
 * after the initial request.  This means that ExternalContext.getResponse() returns null.
 *
 * While it's not generally recommended to get the raw request/response objects and call methods on them
 * (the JSF API should be used when possible), it's sometimes unavoidable.  What we attempt to do then is
 * provide this proxy response.  While mostly unimplemented and originally designed to only provide the
 * getNameSpace() method as outlined in the original JIRA, it could potentially be expanded to provide
 * more functionality.
 */
public class ProxyPortletResponse implements RenderResponse {

    private static final Log log = LogFactory.getLog(ProxyPortletResponse.class);

    private static final String UNSUPPORTED_MESSAGE = "method currently unsupported in ProxyPortletResponse";

    private HttpServletResponse res;

    public ProxyPortletResponse(Object rawResponse) {
        if(rawResponse!=null && rawResponse instanceof HttpServletResponse){
            res = (HttpServletResponse)rawResponse;
        }
    }

    public void addProperty(String s, String s1) {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public void setProperty(String s, String s1) {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public String encodeURL(String s) {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public String getContentType() {
        if( res != null ){
            return res.getContentType();
        }
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public PortletURL createRenderURL() {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public PortletURL createActionURL() {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public String getNamespace() {
        Map requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        Object namespace = requestMap.get("com.icesoft.faces.NAMESPACE");
        if (namespace != null) {
            return (String) namespace;
        }
        return null;
    }

    public void setTitle(String s) {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public void setContentType(String s) {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public String getCharacterEncoding() {
        if( res != null ){
            return res.getCharacterEncoding();
        }
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public PrintWriter getWriter() throws IOException {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public Locale getLocale() {
        if( res != null ){
            return res.getLocale();
        }
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public void setBufferSize(int i) {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public int getBufferSize() {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public void flushBuffer() throws IOException {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public void resetBuffer() {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public boolean isCommitted() {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public void reset() {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }

    public OutputStream getPortletOutputStream() throws IOException {
        log.error(UNSUPPORTED_MESSAGE);
        throw new UnsupportedOperationException();
    }
}
