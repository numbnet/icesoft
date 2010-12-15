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

package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.webapp.http.core.SessionExpiredException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

public class ProxyHttpSession implements HttpSession {
    protected HttpSession session;

    public ProxyHttpSession(HttpSession session) {
        this.session = session;
    }

    public long getCreationTime() {
        try {
            return session.getCreationTime();
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession getCreationTime throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public String getId() {
        try {
            return session.getId();
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession getId throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public long getLastAccessedTime() {
        try {
            return session.getLastAccessedTime();
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession getLastAccessedTime throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public ServletContext getServletContext() {
        try {
            return session.getServletContext();
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession getServletContext throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public void setMaxInactiveInterval(int i) {
        try {
            session.setMaxInactiveInterval(i);
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession setMaxInactiveInterval throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public int getMaxInactiveInterval() {
        try {
            return session.getMaxInactiveInterval();
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession getMaxInactiveInterval throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public HttpSessionContext getSessionContext() {
        try {
            return session.getSessionContext();
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession getSessionContext throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public Object getAttribute(String string) {
        try {
            return session.getAttribute(string);
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession getAttribute throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public Object getValue(String string) {
        try {
            return session.getValue(string);
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession getValue throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public Enumeration getAttributeNames() {
        try {
            return session.getAttributeNames();
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession getAttributeNames throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public String[] getValueNames() {
        try {
            return session.getValueNames();
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession getValueNames throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public void setAttribute(String string, Object object) {
        try {
            session.setAttribute(string, object);
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession setAttribute throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public void putValue(String string, Object object) {
        try {
            session.putValue(string, object);
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession putValue throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public void removeAttribute(String string) {
        try {
            session.removeAttribute(string);
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession removeAttribute throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public void removeValue(String string) {
        try {
            session.removeValue(string);
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession removeValue throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public void invalidate() {
        try {
            session.invalidate();
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession invalidate throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }

    public boolean isNew() {
        try {
            return session.isNew();
        } catch (IllegalStateException e) {
System.out.println("ProxyHttpSession isNew throwing SessionExpiredException");
            throw new SessionExpiredException(e);
        }
    }
}
