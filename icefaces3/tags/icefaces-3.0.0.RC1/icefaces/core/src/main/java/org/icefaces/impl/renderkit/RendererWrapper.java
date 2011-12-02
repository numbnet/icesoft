/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.impl.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import java.io.IOException;

public class RendererWrapper extends Renderer {
    private Renderer renderer;

    public RendererWrapper(Renderer renderer) {
        this.renderer = renderer;
    }

    public Renderer getWrappedRenderer() {
        return renderer;
    }

    public void setWrappedRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void decode(FacesContext context, UIComponent component) {
        renderer.decode(context, component);
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        renderer.encodeBegin(context, component);
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        renderer.encodeChildren(context, component);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        renderer.encodeEnd(context, component);
    }

    public String convertClientId(FacesContext context, String clientId) {
        return renderer.convertClientId(context, clientId);
    }

    public boolean getRendersChildren() {
        return renderer.getRendersChildren();
    }

    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        return renderer.getConvertedValue(context, component, submittedValue);
    }
}
