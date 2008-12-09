package com.icesoft.faces.webapp.command;

import java.io.IOException;
import java.io.Writer;

public class Reload implements Command {
    private String viewIdentifier;

    public Reload(String viewIdentifier) {
        this.viewIdentifier = viewIdentifier;
    }

    public Command coalesceWith(Command command) {
        return command.coalesceWith(this);
    }

    public Command coalesceWith(Redirect redirect) {
        return redirect;
    }

    public Command coalesceWith(Macro macro) {
        return this;
    }

    public Command coalesceWith(UpdateElements updateElements) {
        return this;
    }

    public Command coalesceWith(Reload reload) {
        return this;
    }

    public Command coalesceWith(SessionExpired sessionExpired) {
        return new Macro(sessionExpired, this);
    }

    public Command coalesceWith(SetCookie setCookie) {
        return new Macro(setCookie, this);
    }

    public Command coalesceWith(Pong pong) {
        return this;
    }

    public Command coalesceWith(NOOP noop) {
        return this;
    }

    public void serializeTo(Writer writer) throws IOException {
        writer.write("<reload view=\"" + viewIdentifier + "\"/>");
    }
}
