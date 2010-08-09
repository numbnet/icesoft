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

package com.icesoft.faces.webapp.http.portlet.page;

import com.icesoft.faces.context.View;
import com.icesoft.faces.webapp.http.common.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicLong;

/**
 * This is the default implementation of the AssociatedPageViews interface.  It has logic for handling
 * all aspects of the associated views except for determining the portal page name/id.  Implementations
 * should extend this class and implement the getPageId() method with logic specific to the container.
 */
public abstract class AssociatedPageViewsImpl implements AssociatedPageViews {

    protected static final Log log = LogFactory.getLog(AssociatedPageViews.class);

    private ViewsPageBidiMap bidi = new ViewsPageBidiMap();
    private static Class impl;

    public static final String VIEW_GROUP = "com.icesoft.faces.portlet.viewId";
    private AtomicLong counter = new AtomicLong(0);

    public abstract String getPageId() throws Exception;

    public static AssociatedPageViews getImplementation(Configuration config) {

        //First detect the default implementation to use for all further requests based
        //on an context parameter. If the parameter is not set, default to the noop
        //implementation.
        if (impl == null) {
            String implName = config.getAttribute(IMPLEMENTATION_KEY, NOOP_IMPLEMENTATION);
            try {
                impl = Class.forName(implName);
            } catch (ClassNotFoundException cnfe1) {

                //If an implementation was specified but it can't be loaded, then
                //just use the noop implementation.
                if (log.isWarnEnabled()) {
                    log.warn("could not load " + implName);
                }
                impl = NoOpAssociatedPageViews.class;
            }
            if (log.isInfoEnabled()) {
                log.info("using " + impl.getName());
            }
        }

        try {
            Object inst = impl.newInstance();
            return (AssociatedPageViews) inst;
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("could not create an instance of " + impl.getName(), e);
            }
            return new NoOpAssociatedPageViews();
        }
    }

    public void add(View view) {
        String pageId = null;
        try {
            pageId = getPageId();
            bidi.put(pageId, view);
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("could not get a page id " + e);
            }
        }
    }

    public void disposeAssociatedViews(Map views, View view) {
        Set associatedViews = bidi.getAssociatedViews(view);
        Iterator relatedView = associatedViews.iterator();
        while (relatedView.hasNext()) {
            View v = (View) relatedView.next();
            v.dispose();
            views.remove(v.getViewIdentifier());
            if (log.isDebugEnabled()) {
                log.debug("disposed " + v.toString());
            }
        }
        bidi.remove(view);
    }

    protected long getNextCounter(){
        return counter.getAndIncrement();    
    }
}

