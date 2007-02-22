package com.icesoft.faces.webapp.command;

import java.io.IOException;
import java.io.Writer;

public interface Command {

    Command coallesceWith(Command command);

    Command coallesceWith(Macro macro);

    Command coallesceWith(Updates updates);

    Command coallesceWith(Redirect redirect);

    void serializeTo(Writer writer) throws IOException;
}
