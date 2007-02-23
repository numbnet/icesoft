package com.icesoft.faces.webapp.command;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.Writer;

public class SetCookie implements Command {
    private Cookie cookie;

    public SetCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public Command coallesceWith(Command command) {
        return command.coallesceWith(this);
    }

    public Command coallesceWith(Macro macro) {
        macro.addCommand(this);
        return macro;
    }

    public Command coallesceWith(UpdateElements updateElements) {
        return new Macro(updateElements, this);
    }

    public Command coallesceWith(Redirect redirect) {
        return new Macro(redirect, this);
    }

    public Command coallesceWith(SetCookie setCookie) {
        return new Macro(setCookie, this);
    }

    public void serializeTo(Writer writer) throws IOException {
        writer.write("<set-cookie>" + cookie.toString() + "</set-cookie>");
    }
}
