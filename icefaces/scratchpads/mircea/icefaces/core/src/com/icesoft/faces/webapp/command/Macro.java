package com.icesoft.faces.webapp.command;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Macro implements Command {
    private Collection commands = new ArrayList();

    public Macro(Command commandA, Command commandB) {
        commands.add(commandA);
        commands.add(commandB);
    }

    public Command coallesceWith(Command command) {
        return command.coallesceWith(this);
    }

    public Command coallesceWith(UpdateElements updateElements) {
        commands.add(updateElements);
        return this;
    }

    public Command coallesceWith(Redirect redirect) {
        commands.add(redirect);
        return this;
    }

    public Command coallesceWith(Macro macro) {
        commands.addAll(macro.commands);
        return this;
    }

    public void addCommand(Command command) {
        commands.add(command);        
    }

    public Command coallesceWith(SetCookie setCookie) {
        commands.add(setCookie);
        return this;
    }

    public void serializeTo(Writer writer) throws IOException {
        Iterator i = commands.iterator();
        while (i.hasNext()) {
            Command command = (Command) i.next();
            command.serializeTo(writer);
        }
    }
}
