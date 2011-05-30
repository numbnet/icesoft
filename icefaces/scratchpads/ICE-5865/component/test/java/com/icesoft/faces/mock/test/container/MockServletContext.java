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
 * ServletContext 2.4 API
 */
 
package com.icesoft.faces.mock.test.container;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class MockServletContext implements ServletContext {

    private Hashtable attributes = new Hashtable();
    private Hashtable parameters = new Hashtable();


    public void InitParameters(String name, String value){
        parameters.put(name, value);
    }



    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration getAttributeNames() {
        return attributes.keys();
    }

    public ServletContext getContext(String uripath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getContextPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getInitParameter(String name) {
        return (String)parameters.get(name);
    }

    public Enumeration getInitParameterNames() {
        return parameters.keys();
    }

    public int getMajorVersion() {
        return 2;
    }

    public String getMimeType(String file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMinorVersion() {
        return 4;
    }

    public RequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getRealPath(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public URL getResource(String path) throws MalformedURLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InputStream getResourceAsStream(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set getResourcePaths(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getServerInfo() {
        return "MockServletContext";
    }

    public Servlet getServlet(String name) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getServletContextName() {
        return "MockServletContext";
    }

    public Enumeration getServletNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Enumeration getServlets() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void log(String msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void log(Exception exception, String msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void log(String message, Throwable throwable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public void setAttribute(String name, Object object) {
        attributes.put(name, object);
    }
}
