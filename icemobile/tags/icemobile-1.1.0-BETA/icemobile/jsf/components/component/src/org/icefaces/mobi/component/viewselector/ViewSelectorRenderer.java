/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.mobi.component.viewselector;

import org.icefaces.mobi.utils.Utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Renders the child view type that matches the detected view type.  If the
 * view type can not be detected then the defaultType is rendered.  It is
 * encouraged to use Facelet Includes to promote code sharing between views and
 * reuse.
 * <p/>
 * There are currently only two device view types: small and large.  In the future
 * it may be necessary to create a third "medium" view type.
 */
public class ViewSelectorRenderer extends Renderer {

    private static Logger logger = Logger.getLogger(ViewSelectorRenderer.class.getName());

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ViewSelector viewSelector = (ViewSelector) component;
        Map<Object, Object> contextMap = facesContext.getAttributes();
        String name;
        // check the session context map for the MOBILE_DEVICE_TYPE_KEY, if found
        // there is now point rechecking for for the device type.
        if (contextMap.containsKey(ViewSelector.VIEW_TYPE_KEY)) {
            name = (String) contextMap.get(ViewSelector.VIEW_TYPE_KEY);
        } else {
            Utils.DeviceType deviceType = Utils.getDeviceType(facesContext);
            if (deviceType.equals(Utils.DeviceType.ipad) ||
                    deviceType.equals(Utils.DeviceType.honeycomb)) {
                name = ViewSelector.LARGE_VIEW_TYPE;
            } else if (deviceType.equals(Utils.DeviceType.android) ||
                    deviceType.equals(Utils.DeviceType.iphone) ||
                    deviceType.equals(Utils.DeviceType.bberry)) {
                name = ViewSelector.SMALL_VIEW_TYPE;
            } else {
                name = viewSelector.getDefaultView();
            }
        }
        // store in session map for use later.
        contextMap.put(ViewSelector.VIEW_TYPE_KEY, name);

        // write out the facet name that was detected
        if (ViewSelector.SMALL_VIEW_TYPE.equals(name)) {
            UIComponent viewFacet = viewSelector.getFacet(ViewSelector.SMALL_FACET);
            if (viewFacet != null) {
                Utils.renderChild(facesContext, viewFacet);
            } else {
                logger.warning("Small view detected but no small Facet defined in viewSelector element.");
            }
        } else if (ViewSelector.LARGE_VIEW_TYPE.equals(name)) {
            UIComponent viewFacet = viewSelector.getFacet(ViewSelector.LARGE_FACET);
            if (viewFacet != null) {
                Utils.renderChild(facesContext, viewFacet);
            } else {
                logger.warning("Large view detected but no large Facet defined in viewSelector element.");
            }
        }
    }

    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
