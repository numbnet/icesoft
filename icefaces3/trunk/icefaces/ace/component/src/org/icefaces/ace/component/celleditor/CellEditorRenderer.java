/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.component.celleditor;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.component.datatable.DataTableConstants;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.render.MandatoryResourceComponent;

@MandatoryResourceComponent(tagName="cellEditor", value="org.icefaces.ace.component.celleditor.CellEditor")
public class CellEditorRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        CellEditor editor = (CellEditor) component;
        
        writer.startElement("span", null);
        writer.writeAttribute("id", component.getClientId(context), null);
        writer.writeAttribute("class", DataTableConstants.CELL_EDITOR_CLASS, null);

        writer.startElement("span", null);
        writer.writeAttribute("class", DataTableConstants.CELL_EDITOR_OUTPUT_CLASS, null);
        editor.getFacet("output").encodeAll(context);
        writer.endElement("span");

        writer.startElement("span", null);
        writer.writeAttribute("class", DataTableConstants.CELL_EDITOR_INPUT_CLASS, null);
        editor.getFacet("input").encodeAll(context);
        writer.endElement("span");

        writer.endElement("span");
    }

    @Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		//Rendering happens on encodeEnd
	}

    @Override
	public boolean getRendersChildren() {
		return true;
	}

}
