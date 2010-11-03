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

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SetCookie extends AbstractCommand {
    private final static DateFormat CookieDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z");

    static {
        CookieDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private Cookie cookie;

    public SetCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public Command coalesceWithNext(Command command) {
        return command.coalesceWithPrevious(this);
    }

    public Command coalesceWithPrevious(Macro macro) {
        macro.addCommand(this);
        return macro;
    }

    public Command coalesceWithPrevious(UpdateElements updateElements) {
        Macro macro = new Macro();
        macro.addCommand(this);
        macro.addCommand(updateElements);
        return macro;
    }

    public Command coalesceWithPrevious(Redirect redirect) {
        Macro macro = new Macro();
        macro.addCommand(this);
        macro.addCommand(redirect);
        return macro;
    }

    public Command coalesceWithPrevious(Reload reload) {
        Macro macro = new Macro();
        macro.addCommand(this);
        macro.addCommand(reload);
        return macro;
    }

    public Command coalesceWithPrevious(SessionExpired sessionExpired) {
        return sessionExpired;
    }

    public Command coalesceWithPrevious(SetCookie setCookie) {
        if (setCookie.cookie.getName().equals(cookie.getName())) {
            return this;
        } else {
            Macro macro = new Macro();
            macro.addCommand(setCookie);
            macro.addCommand(this);
            return macro;
        }
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
        writer.write("<set-cookie>");
        writer.write(cookie.getName());
        writer.write("=");
        writer.write(cookie.getValue());
        writer.write("; ");
        int maxAge = cookie.getMaxAge();
        if (maxAge >= 0) {
            Date expiryDate = new Date(System.currentTimeMillis() + maxAge * 1000l);
            writer.write("expires=");
            writer.write(CookieDateFormat.format(expiryDate));
            writer.write("; ");
        }
        String path = cookie.getPath();
        if (path != null) {
            writer.write("path=");
            writer.write(path);
            writer.write("; ");
        }
        String domain = cookie.getDomain();
        if (domain != null) {
            writer.write("domain=");
            writer.write(domain);
            writer.write("; ");
        }
        if (cookie.getSecure()) {
            writer.write("secure;");
        }
        writer.write("</set-cookie>");
    }
}
