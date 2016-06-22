package org.icesoft.util.servlet;

import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExtensionRegistryEvent
extends EventObject {
    private static final Logger LOGGER = Logger.getLogger(ExtensionRegistryEvent.class.getName());

    private final Object extension;
    private final String name;
    private final int quality;

    public ExtensionRegistryEvent(final String name, final Object extension, final int quality, final Object source) {
        super(source);
        this.name = name;
        this.extension = extension;
        this.quality = quality;
    }

    public ExtensionRegistryEvent(final String name, final Object extension, final Object source) {
        this(name, extension, 0, source);
    }

    public Object getExtension() {
        return extension;
    }

    public String getName() {
        return name;
    }

    public int getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return
            new StringBuilder().
                append("ExtensionRegistryEvent[").
                    append(classMembersToString()).
                append("]").
                    toString();
    }

    protected String classMembersToString() {
        return
            new StringBuilder().
                append("extension: '").append(getExtension()).append("', ").
                append("name: '").append(getName()).append("', ").
                append("quality: '").append(getQuality()).append("'").
                    toString();
    }
}
