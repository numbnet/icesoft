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
 * ViewHandler wrapper
 */
package com.icesoft.faces.mock.test.container;

import java.io.IOException;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;

public class MockViewHandler extends ViewHandler {

    @Override
    public Locale calculateLocale(FacesContext context) {
        System.out.println("TODO caculateLocale");
        return Locale.CANADA;
    }

    @Override
    public String calculateRenderKitId(FacesContext context) {
        return "HTML_BASIC";
        //return RenderKitFactory.HTML_BASIC_RENDER_KIT;
    }

    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {
        System.out.println("createView ");
        UIViewRoot result = new UIViewRoot();
        result.setViewId(viewId);
        //result.setRenderKitId(calculateRenderKitId(context));
        return result;
    }

    @Override
    public String getActionURL(FacesContext context, String viewId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getResourceURL(FacesContext context, String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
        System.out.println("renderView context=" + context + " UIViewRoot id=" + viewToRender.getViewId());
    }

    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeState(FacesContext context) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
