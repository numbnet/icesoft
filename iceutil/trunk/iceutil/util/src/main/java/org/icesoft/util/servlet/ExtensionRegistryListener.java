package org.icesoft.util.servlet;

import java.util.EventListener;

public interface ExtensionRegistryListener
extends EventListener {
    void registered(ExtensionRegistryEvent event);

    void unregistered(ExtensionRegistryEvent event);
}
