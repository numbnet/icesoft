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

package com.icesoft.faces.context;

import com.icesoft.faces.util.DOMUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class NormalModeSerializer implements DOMSerializer {
    private final static Log log = LogFactory.getLog(NormalModeSerializer.class);
    private BridgeFacesContext context;
    private Writer writer;

    public NormalModeSerializer(BridgeFacesContext context, Writer writer) {
        this.context = context;
        this.writer = writer;
    }

    public void serialize(Document document) throws IOException {
        try {
            if (context.isContentIncluded()) {
                if (log.isDebugEnabled()) {
                    log.debug("treating request as a fragment");
                }

                Node body = DOMUtils.getChildByNodeName(document.getDocumentElement(), "body");
                if (null != body) {
                    //insert a containing element for bridge anchoring
                    writer.write("<div>\n");
                    DOMUtils.printChildNodes(body, writer);
                    writer.write("</div>\n");
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("treating request as a whole page (not a fragment)");
                }
                Map requestMap = context.getExternalContext().getRequestMap();
                String publicID =
                        (String) requestMap.get(DOMResponseWriter.DOCTYPE_PUBLIC);
                String systemID =
                        (String) requestMap.get(DOMResponseWriter.DOCTYPE_SYSTEM);
                String root =
                        (String) requestMap.get(DOMResponseWriter.DOCTYPE_ROOT);
                String output =
                        (String) requestMap.get(DOMResponseWriter.DOCTYPE_OUTPUT);
                boolean prettyPrinting =
                        Boolean.valueOf((String) requestMap
                                .get(DOMResponseWriter.DOCTYPE_PRETTY_PRINTING))
                                .booleanValue();

                //todo: replace this with a complete new implementation that doesn't rely on xslt but can serialize xml, xhtml, and html.
                if (output == null || ("html".equals(output) && !prettyPrinting)) {
                    if (publicID != null && systemID != null && root != null) {
                        writer.write(DOMUtils.DocumentTypetoString(publicID, systemID,
                                root));
                    }
                    DOMUtils.printNode(document, writer);
                } else {
                    //use a serializer. not as performant.
                    JAXPSerializer serializer = new JAXPSerializer(writer, publicID, systemID);
                    if ("xml".equals(output)) {
                        serializer.outputAsXML();
                    } else {
                        serializer.outputAsHTML();
                    }
                    if (prettyPrinting) {
                        serializer.printPretty();
                    }
                    serializer.serialize(document);
                }
            }

            writer.flush();
        } catch (IOException e) {
            //capture & log Tomcat specific exception
            if (e.getClass().getName().endsWith("ClientAbortException")) {
                log.debug("Browser closed the connection prematurely.");
            } else {
                throw e;
            }
        }
    }
}