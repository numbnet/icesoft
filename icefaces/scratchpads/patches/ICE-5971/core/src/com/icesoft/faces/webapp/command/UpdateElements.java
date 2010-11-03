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

package com.icesoft.faces.webapp.command;

import com.icesoft.faces.util.DOMUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class UpdateElements extends AbstractCommand {
    private final static Pattern START_CDATA = Pattern.compile("<\\!\\[CDATA\\[");
    private final static Pattern END_CDATA = Pattern.compile("\\]\\]>");
    private Element[] updates;
    private boolean coalesce = true;

    public UpdateElements(boolean coalesce, Element[] updates) {
        this.updates = updates;
        this.coalesce = coalesce;
    }

    public Command coalesceWithPrevious(UpdateElements updateElementsCommand) {
        ArrayList coallescedUpdates = new ArrayList();
        Element[] previousUpdates = updateElementsCommand.updates;

        for (int i = 0; i < previousUpdates.length; i++) {
            Element previousUpdate = previousUpdates[i];
            boolean overriden = false;
            //test if any of the new updates is replacing the same element
            for (int j = 0; j < updates.length; j++) {
                Element update = updates[j];
                if (update.getAttribute("id").equals(previousUpdate.getAttribute("id"))) {
                    overriden = true; break;
                }
            }
            //drop overriden updates
            if (!overriden) {
                coallescedUpdates.add(previousUpdate);
            }
        }
        coallescedUpdates.addAll(Arrays.asList(updates));

        return new UpdateElements(coalesce, (Element[]) coallescedUpdates.toArray(new Element[coallescedUpdates.size()]));
    }

    public Command coalesceWithNext(Command command) {
        if (!coalesce)  {
            return command;
        }
        return command.coalesceWithPrevious(this);
    }

    public Command coalesceWithPrevious(Macro macro) {
        macro.addCommand(this);
        return macro;
    }

    public Command coalesceWithPrevious(Redirect redirect) {
        return redirect;
    }

    public Command coalesceWithPrevious(Reload reload) {
        return reload;
    }

    public Command coalesceWithPrevious(SessionExpired sessionExpired) {
        return sessionExpired;
    }

    public Command coalesceWithPrevious(SetCookie setCookie) {
        Macro macro = new Macro();
        macro.addCommand(this);
        macro.addCommand(setCookie);
        return macro;
    }

    public Command coalesceWithPrevious(Pong pong) {
        Macro macro = new Macro();
        macro.addCommand(this);
        macro.addCommand(pong);
        return macro;
    }

    public Command coalesceWithPrevious(NOOP noop) {
        return this;
    }

    public void serializeTo(Writer writer) throws IOException {
        writer.write("<updates>");
        for (int i = 0; i < updates.length; i++) {
            Element update = updates[i];
            if (update == null) continue;
            writer.write("<update address=\"");
            writer.write(update.getAttribute("id"));
            writer.write("\" tag=\"" + update.getTagName() + "\">");

            NamedNodeMap attributes = update.getAttributes();
            for (int j = 0; j < attributes.getLength(); j++) {
                Attr attribute = (Attr) attributes.item(j);
                writer.write("<attribute name=\"");
                writer.write(attribute.getName());
                String value = attribute.getValue();
                if ("".equals(value)) {
                    writer.write("\"/>");
                } else {
                    writer.write("\"><![CDATA[");
                    writer.write(DOMUtils.escapeAnsi(value));
                    writer.write("]]></attribute>");
                }
            }

            String content = DOMUtils.childrenToString(update);
            if ("".equals(content)) {
                writer.write("<content/>");
            } else {
                writer.write("<content><![CDATA[");
                content = START_CDATA.matcher(content).replaceAll("<!#cdata#");
                content = END_CDATA.matcher(content).replaceAll("##>");
                writer.write(content);
                writer.write("]]></content>");
            }
            writer.write("</update>");
        }
        writer.write("</updates>");
    }
}
