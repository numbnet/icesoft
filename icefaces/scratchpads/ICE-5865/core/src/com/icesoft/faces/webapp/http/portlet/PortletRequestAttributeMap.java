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

import com.icesoft.faces.context.AbstractCopyingAttributeMap;

import javax.portlet.PortletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;

public class PortletRequestAttributeMap extends AbstractCopyingAttributeMap {
    private Collection localAttributes = new HashSet();
    private PortletRequest request;

    public PortletRequestAttributeMap(PortletRequest request) {
        this.request = request;
        initialize();
        localAttributes.add("javax.portlet.request");
        localAttributes.add("javax.portlet.response");
        localAttributes.add("com.liferay.portal.kernel.servlet.PortletServletResponse");
        localAttributes.add("com.liferay.portal.kernel.servlet.PortletServletRequest");
    }

    public Enumeration getAttributeNames() {
        return request.getAttributeNames();
    }

    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        localAttributes.add(name);
        request.setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        localAttributes.remove(name);
        request.removeAttribute(name);
    }

    public void clear() {
        Iterator i = new ArrayList(localAttributes).iterator();
        while (i.hasNext()) {
            Object name = i.next();
            super.remove(name);
        }
        localAttributes.clear();
    }
}
