package com.icesoft.faces.webapp.command;

import com.icesoft.faces.util.DOMUtils;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Updates implements Command {
    private final static Pattern START_CDATA = Pattern.compile("<\\!\\[CDATA\\[");
    private final static Pattern END_CDATA = Pattern.compile("\\]\\]>");
    private Element[] updates;

    public Updates(Element[] updates) {
        this.updates = updates;
    }

    public Command coallesceWith(Updates updatesCommand) {
        Set coallescedUpdates = new HashSet();
        Element[] previousUpdates = updatesCommand.updates;
        
        for (int i = 0; i < previousUpdates.length; i++) {
            Element previousUpdate = previousUpdates[i];
            Element selectedUpdate = previousUpdate;
            for (int j = 0; j < updates.length; j++) {
                Element update = updates[j];
                if (update.getAttribute("id").equals(previousUpdate.getAttribute("id"))) {
                    selectedUpdate = update;
                    break;
                }
            }
            coallescedUpdates.add(selectedUpdate);
        }
        coallescedUpdates.addAll(Arrays.asList(updates));

        return new Updates((Element[]) coallescedUpdates.toArray(new Element[coallescedUpdates.size()]));
    }

    public Command coallesceWith(Command command) {
        return command.coallesceWith(this);
    }

    public Command coallesceWith(Macro macro) {
        return new Macro(macro, this);
    }

    public Command coallesceWith(Redirect redirect) {
        return new Macro(redirect, this);
    }

    public void serializeTo(Writer writer) throws IOException {
        for (int i = 0; i < updates.length; i++) {
            Element update = updates[i];
            if (update == null) continue;
            writer.write("<update address=\"");
            writer.write(update.getAttribute("id"));
            writer.write("\"><![CDATA[");
            String content = DOMUtils.nodeToString(update);
            content = START_CDATA.matcher(content).replaceAll("<!#cdata#");
            content = END_CDATA.matcher(content).replaceAll("##>");            
            writer.write(content);
            writer.write("]]></update>");
        }
    }

    public String toString() {
        try {
            StringWriter writer = new StringWriter();
            serializeTo(writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
