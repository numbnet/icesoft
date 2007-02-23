package com.icesoft.faces.webapp.command;

import java.io.IOException;
import java.io.Writer;

public interface Command {

    Command coallesceWith(Command command);

    Command coallesceWith(Macro macro);

    Command coallesceWith(UpdateElements updateElements);

    Command coallesceWith(Redirect redirect);

    Command coallesceWith(SetCookie setCookie);

    void serializeTo(Writer writer) throws IOException;
}
