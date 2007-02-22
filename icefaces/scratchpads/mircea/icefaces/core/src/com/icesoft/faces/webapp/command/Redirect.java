package com.icesoft.faces.webapp.command;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;

public class Redirect implements Command {
    private URI uri;

    public Redirect(URI uri) {
        this.uri = uri;
    }

    public Redirect(String uri) {
        this.uri = URI.create(uri);
    }

    public Command coallesceWith(Command command) {
        return command.coallesceWith(this);
    }

    public Command coallesceWith(Macro macro) {
        return this;
    }

    public Command coallesceWith(Updates updates) {
        return this;
    }

    public Command coallesceWith(Redirect redirect) {
        return this;
    }

    public void serializeTo(Writer writer) throws IOException {
        writer.write("<redirect url=\"" + uri + "\"/>");
    }
}
