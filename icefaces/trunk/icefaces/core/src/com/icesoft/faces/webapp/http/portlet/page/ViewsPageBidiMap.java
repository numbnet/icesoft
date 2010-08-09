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

import java.util.*;

/**
 * This is a specific utility class for maintaining a bidirection relationship
 * between views and the pages that they reside on. This optimizes our ability
 * to get all the views for a particular page when we want to dispose of them.
 */
class ViewsPageBidiMap {

    private Map viewsOnPage = new HashMap();
    private Map pageForView = new WeakHashMap();

    public ViewsPageBidiMap() {
    }

    public void put(String pageId, View view) {
        Object viewMapObj = viewsOnPage.get(pageId);
        if (viewMapObj == null) {
            WeakHashMap viewMap = new WeakHashMap();
            viewMap.put(view, null);
            viewsOnPage.put(pageId, viewMap);
        } else {
            WeakHashMap viewMap = (WeakHashMap) viewMapObj;
            viewMap.put(view, null);
        }
        pageForView.put(view, pageId);
    }

    public Set getAssociatedViews(View view) {
        Object pageId = pageForView.get(view);
        if (pageId == null) {
            return Collections.EMPTY_SET;
        }

        Object associatedViewsObj = viewsOnPage.get(pageId);
        if (associatedViewsObj == null) {
            return Collections.EMPTY_SET;
        }

        WeakHashMap associatedViews = (WeakHashMap) associatedViewsObj;
        return associatedViews.keySet();
    }

    public void clear() {
        viewsOnPage.clear();
        pageForView.clear();
    }

    public String toString() {
        return super.toString() + 
                "\n  views on page: " + viewsOnPage +
                "\n  page for view: " + pageForView;
    }

    public void remove(View view) {
        Object pageId = pageForView.remove(view);
        if( pageId != null ){
            viewsOnPage.remove(pageId);
        }

    }
}
