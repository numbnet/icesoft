package org.icefaces.impl.event;

import javax.faces.component.UIOutput;

public class ResourceOutput extends UIOutput {
    public ResourceOutput() {
    }

    public ResourceOutput(String rendererType, String name, String library) {
        setRendererType(rendererType);
        if (name != null && name.length() > 0) {
            getAttributes().put("name", name);
        }
        if (library != null && library.length() > 0) {
            getAttributes().put("library", library);
        }
    }
}