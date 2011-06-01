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

package org.icefaces.ace.component.tab;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.util.HTML;

public class TabSetProxy extends TabSetProxyBase{

	
    public void encodeBegin(FacesContext context) throws IOException {
    	super.encodeBegin(context);
    	ResponseWriter writer = context.getResponseWriter();
    	String id = getFor() + "_tsc";
    	writer.startElement(HTML.INPUT_ELEM, this); 	
    	writer.writeAttribute(HTML.ID_ATTR, id, HTML.ID_ATTR);
    	writer.writeAttribute(HTML.NAME_ATTR, id, HTML.NAME_ATTR);    	
    	writer.writeAttribute(HTML.TYPE_ATTR, "hidden", HTML.TYPE_ATTR);    	
    	writer.endElement(HTML.INPUT_ELEM);
    }
}
