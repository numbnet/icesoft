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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

class Macro extends AbstractCommand {
    //see http://krijnhoetmer.nl/stuff/javascript/maximum-cookies/
    private static final int MaxNumberOfCookies = 50;
    private Command updateElements;
    private Command pong;
    private Command redirect;
    private Command reload;
    private ArrayList setCookies = new ArrayList();

    public Command coalesceWithNext(Command command) {
        return command.coalesceWithPrevious(this);
    }

    public Command coalesceWithPrevious(UpdateElements updateElements) {
        throw new IllegalStateException("Macro commands are constructed only as result of coalescing ");
    }

    public Command coalesceWithPrevious(Redirect redirect) {
        throw new IllegalStateException("Macro commands are constructed only as result of coalescing ");
    }

    public Command coalesceWithPrevious(Reload reload) {
        throw new IllegalStateException("Macro commands are constructed only as result of coalescing ");
    }

    public Command coalesceWithPrevious(SessionExpired sessionExpired) {
        throw new IllegalStateException("Macro commands are constructed only as result of coalescing ");
    }

    public Command coalesceWithPrevious(Macro macro) {
        throw new IllegalStateException("Macro commands are constructed only as result of coalescing ");
    }

    public Command coalesceWithPrevious(SetCookie setCookie) {
        throw new IllegalStateException("Macro commands are constructed only as result of coalescing ");
    }

    public Command coalesceWithPrevious(Pong pong) {
        throw new IllegalStateException("Macro commands are constructed only as result of coalescing ");
    }

    public Command coalesceWithPrevious(NOOP noop) {
        throw new IllegalStateException("Macro commands are constructed only as result of coalescing ");
    }

    public void addCommand(UpdateElements updateElements) {
        if (redirect == null && reload == null) {
            this.updateElements = this.updateElements == null ? updateElements : this.updateElements.coalesceWithNext(updateElements);
        }
    }

    public void addCommand(Pong pong) {
        this.pong = pong;
    }

    public void addCommand(SetCookie setCookie) {
        if (setCookies.size() > MaxNumberOfCookies) {
            setCookies.remove(0);
        }
        setCookies.add(setCookie);
    }

    public void addCommand(Redirect redirect) {
        this.redirect = redirect;
        this.reload = null;
        this.pong = null;
        this.updateElements = null;
    }

    public void addCommand(Reload reload) {
        if (redirect == null) {
            this.reload = reload;
            this.pong = null;
            this.updateElements = null;
        }
    }

    public void serializeTo(Writer writer) throws IOException {
        writer.write("<macro>");
        if (updateElements != null) updateElements.serializeTo(writer);
        if (pong != null) pong.serializeTo(writer);
        if (redirect != null) redirect.serializeTo(writer);
        if (reload != null) reload.serializeTo(writer);
        Iterator i = setCookies.iterator();
        while (i.hasNext()) {
            ((Command) i.next()).serializeTo(writer);
        }
        writer.write("</macro>");
    }
}
