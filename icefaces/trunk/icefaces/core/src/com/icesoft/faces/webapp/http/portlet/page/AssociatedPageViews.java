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

import java.util.Map;

/**
 * In a portlet environment, there can be multiple views on a single page.  It's generally desirable that, when
 * one view is disposed due to navigation or closing a window/tab that all the associated views of that portal
 * page are also disposed.  To do that, we need to track all the views associated with the page. However, since
 * there is no specified way to determine which page a portlet/view is on, the strategy for finding out is
 * specific to the container.
 * <p/>
 * To support this feature in a portal container, you should do the following:
 * <p/>
 * <ul>
 * <li>write a class that extends AssociatedPageViewsImpl (an abstract class that implements this interface)</li>
 * <li>implement the getPageId() method with the logic required</li>
 * <li>activate your implementation by specifying a context parameter that has the fully qualified name of
 * the implemention as the value</li>
 * <p/>
 * </ul>
 * <p/>
 * e.g.
 * <p/>
 * <pre>
 * &lt;context-param&gt;
 *     &lt;param-name&gt;com.icesoft.faces.portlet.associatedPageViewsImpl&lt;/param-name&gt;
 *     &lt;param-value&gt;com.icesoft.faces.webapp.http.portlet.page.JBossAssociatedPageViews&lt;/param-value&gt;
 * &lt;/context-param&gt;
 * </pre>
 * <p/>
 * The ICEfaces core framework will attempt to use the specified implementation to track associated views and, when
 * one of the views on the page is disposed, all associated views will be disposed as well.
 * <p/>
 * Note: This interface and the other members of this package are not currently considered officially supported APIs.
 * They are aimed at providing an extension point for adding portal container specific functionality without adding
 * compile or runtime dependencies on portal specific libraries.
 */
public interface AssociatedPageViews {

    /**
     * Context parameter name to use when specifying an implementation of this interface.
     */
    static final String IMPLEMENTATION_KEY = "portlet.associatedPageViewsImpl";

    /**
     * Class name for the noop implemenation of this interface.
     */
    static final String NOOP_IMPLEMENTATION = "com.icesoft.faces.webapp.http.portlet.page.NoOpAssociatedPageViews";

    /**
     * Key to associate in a request so that views on the same portal page of different tabs/windows
     * can be distinguished.
     */
    static final String VIEWS_REQUEST_MARKER = "com.icesoft.faces.viewsRequestMarker";

    /**
     * The implementation of this method will typically use portal implementation specific logic to determine the
     * unique page name/id that the current portlet resides in.
     *
     * @return The unique name or id of the page that the portlet resides in.
     */
    public String getPageId() throws Exception;

    /**
     * Associates the the specified view to the current page. Implementations should rely on the superclass
     * (AssociatedPageViewsImpl) to handle this.
     *
     * @param view The view to associate with the page.
     */
    public void add(View view);

    /**
     * Disposes the specified view as well as all other views associated with the same page. Implementations should
     * rely on the superclass (AssociatedPageViewsImpl) to handle this.
     *
     * @param views The map of all views for this session
     * @param view The view to dispose.
     */
    public void disposeAssociatedViews(Map views, View view);

}
