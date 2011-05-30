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

package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.DisposableBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class DisposeBeans {
    private static final Log log = LogFactory.getLog(DisposeBeans.class);

    public static void in(ServletContext context) {
        Enumeration enumeration = context.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            dispose(context.getAttribute((String) enumeration.nextElement()));
        }
    }

    public static void in(HttpSession session) {
        Enumeration enumeration = session.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            dispose(session.getAttribute((String) enumeration.nextElement()));
        }
    }

    public static void in(Map map) {
        Iterator iterator = new ArrayList(map.values()).iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            dispose(object);
        }
    }

    private static void dispose(Object object) {
        if (object instanceof DisposableBean) {
            try {
                ((DisposableBean) object).dispose();
            } catch (Exception e) {
                log.error("Failed to properly dispose " + object + " bean.", e);
            }
        }
    }
}
