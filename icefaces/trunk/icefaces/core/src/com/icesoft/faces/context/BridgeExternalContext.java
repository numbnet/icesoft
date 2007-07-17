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

/*
 * BridgeExternalContext.java
 */

package com.icesoft.faces.context;

import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.Redirect;
import com.icesoft.faces.webapp.command.SetCookie;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesCommonlet;
import com.icesoft.util.SeamUtilities;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is supposed to provide a generic interface to the
 * environment that we're running in (e.g. servlets, portlets).
 */
public abstract class BridgeExternalContext extends ExternalContext {
    private static String PostBackKey;

    static {
        //We will place VIEW_STATE_PARAM in the requestMap so that
        //JSF 1.2 doesn't think the request is a postback and skip
        //execution
        try {
            Field field = ResponseStateManager.class.getField("VIEW_STATE_PARAM");
            if (null != field) {
                PostBackKey = (String) field.get(ResponseStateManager.class);
            }
        } catch (Exception e) {
        }
    }

    protected String viewIdentifier;
    protected CommandQueue commandQueue;
    protected boolean standardScope;
    protected Map applicationMap;
    protected Map sessionMap;
    protected Map requestMap;
    protected Map initParameterMap;
    protected Redirector redirector;
    protected CookieTransporter cookieTransporter;
    protected String requestServletPath;
    protected String requestPathInfo;
    protected Map requestParameterMap;
    protected Map requestParameterValuesMap;
    protected Map requestCookieMap;
    protected Map responseCookieMap;

    protected BridgeExternalContext(String viewIdentifier, CommandQueue commandQueue, Configuration configuration) {
        this.viewIdentifier = viewIdentifier;
        this.commandQueue = commandQueue;
        this.standardScope = configuration.getAttributeAsBoolean("standardRequestScope", false);
    }

    public abstract String getRequestURI();

    public abstract Writer getWriter(String encoding) throws IOException;

    public abstract void switchToNormalMode();

    public abstract void switchToPushMode();

    public abstract void update(HttpServletRequest request, HttpServletResponse response);

    public abstract void updateOnReload(Object request, Object response);

    public void addCookie(Cookie cookie) {
        responseCookieMap.put(cookie.getName(), cookie);
        cookieTransporter.send(cookie);
    }

    public void setRequestPathInfo(String viewId) {
        requestPathInfo = viewId;
    }

    public void setRequestServletPath(String viewId) {
        requestServletPath = viewId;
    }

    public Map getApplicationSessionMap() {
        return sessionMap;
    }

    /**
     * This method is not necessary. The application developer can keep track
     * of the added cookies.
     *
     * @deprecated
     */
    public Map getResponseCookieMap() {
        return responseCookieMap;
    }

    //todo: see if we can execute full JSP cycle all the time (not only when page is parsed)
    //todo: this way the bundles are put into the request map every time, so we don't have to carry
    //todo: them between requests
    public Map collectBundles() {
        Map result = new HashMap();
        Iterator entries = requestMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            Object value = entry.getValue();
            if (value != null) {
                String className = value.getClass().getName();
                if ((className.indexOf("LoadBundleTag") > 0) ||  //Sun RI
                        (className.indexOf("BundleMap") > 0)) {     //MyFaces
                    result.put(entry.getKey(), value);
                }
            }
        }

        return result;
    }

    public void injectBundles(Map bundles) {
        requestMap.putAll(bundles);
    }

    protected void insertPostbackKey() {
        if (null != PostBackKey) {
            requestParameterMap.put(PostBackKey, "not reload");
            requestParameterValuesMap.put(PostBackKey, new String[]{"not reload"});
        }
    }

    /**
     * Any GET request performed by the browser is a non-faces request to the framework.
     * (JSF-spec chapter 2, introduction). Given this, the framework must create a new
     * viewRoot for the request, even if the viewId has already been visited. (Spec
     * section 2.1.1) <p>
     * <p/>
     * Only during GET's remember, not during partial submits, where the JSF framework must
     * be allowed to attempt to restore the view. There is a great deal of Seam related code
     * that depends on this happening. So put in a token that allows the D2DViewHandler
     * to differentiate between the non-faces request, and the postbacks, for this
     * request, which will allow the ViewHandler to make the right choice, since we keep
     * the view around for all types of requests
     */
    protected void insertNewViewrootToken() {
        if (SeamUtilities.isSeamEnvironment()) {
            requestParameterMap.put(
                    PersistentFacesCommonlet.SEAM_LIFECYCLE_SHORTCUT,
                    Boolean.TRUE);
        }
    }

    /**
     * If in Standard request scope mode, remove all parameters from
     * the Request Map.
     */
    public void resetRequestMap() {
        if (standardScope) {
            requestMap.clear();
        }
    }

    public void dispose() {
        requestMap.clear();
        commandQueue.take();
    }

    protected void applySeamLifecycleShortcut(boolean persistSeamKey) {
        if (persistSeamKey) {
            requestParameterMap.put(
                    PersistentFacesCommonlet.SEAM_LIFECYCLE_SHORTCUT,
                    Boolean.TRUE);
        }
    }

    protected boolean isSeamLifecycleShortcut() {
        boolean persistSeamKey = false;
        if (requestParameterMap != null) {
            persistSeamKey = requestParameterMap.containsKey(PersistentFacesCommonlet.SEAM_LIFECYCLE_SHORTCUT);
        }

        return persistSeamKey;
    }

    public Map getApplicationMap() {
        return applicationMap;
    }

    public Map getSessionMap() {
        return sessionMap;
    }

    public Map getRequestMap() {
        return requestMap;
    }

    public String getInitParameter(String name) {
        return (String) initParameterMap.get(name);
    }

    public Map getInitParameterMap() {
        return initParameterMap;
    }

    public void redirect(String requestURI) throws IOException {
        URI uri = URI.create(SeamUtilities.encodeSeamConversationId(requestURI, viewIdentifier));
        String query = uri.getQuery();
        if (query == null) {
            redirector.redirect(uri + "?rvn=" + viewIdentifier);
        } else if (query.matches(".*rvn=.*")) {
            redirector.redirect(uri.toString());
        } else {
            redirector.redirect(uri + "&rvn=" + viewIdentifier);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public Map getRequestParameterMap() {
        return requestParameterMap;
    }

    public Map getRequestParameterValuesMap() {
        return requestParameterValuesMap;
    }

    public Iterator getRequestParameterNames() {
        return requestParameterMap.keySet().iterator();
    }

    public Map getRequestCookieMap() {
        return requestCookieMap;
    }

    public interface Redirector {
        void redirect(String uri);
    }

    public interface CookieTransporter {
        void send(Cookie cookie);
    }

    public class CommandQueueRedirector implements Redirector {
        public void redirect(String uri) {
            commandQueue.put(new Redirect(uri));
        }
    }

    public class CommandQueueCookieTransporter implements CookieTransporter {
        public void send(Cookie cookie) {
            commandQueue.put(new SetCookie(cookie));
        }
    }

}
