/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icesoft.util.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ExtensionRegistry
implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(ExtensionRegistry.class.getName());

    private static final String EXTENSION_REGISTRY_MAP_NAME = ExtensionRegistry.class.getName() + "$Map";

    public void contextInitialized(final ServletContextEvent event) {
        setExtensionRegistryMapIfNeeded(new HashMap<String, List<ExtensionRegistryEntry>>(), event.getServletContext());
    }

    public void contextDestroyed(final ServletContextEvent event) {
        // Do nothing.
    }

    /**
     * Get the extension of the specified name with the highest quality.
     *
     * @param name
     * @param servletContext
     * @return
     */
    public static Object getBestExtension(final String name, final ServletContext servletContext) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Getting best Extension '" + name + "'.");
        }
        List<ExtensionRegistryEntry> _extensionRegistryEntryList = getExtensionRegistryEntryList(name, servletContext);
        if (_extensionRegistryEntryList != null) {
            Object _extension = _extensionRegistryEntryList.get(_extensionRegistryEntryList.size() - 1).getExtension();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "Successfully retrieved best Extension '" + name + "': " + _extension
                );
            }
            return _extension;
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "Failed to retrieve best Extension '" + name + "'."
                );
            }
            return null;
        }
    }

    /**
     * Get an array of extensions ordered by quality.
     *
     * @param name
     * @param servletContext
     * @return
     */
    public static Object[] getExtensions(final String name, final ServletContext servletContext) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Getting Extensions '" + name + "'.");
        }
        List<ExtensionRegistryEntry> _extensionRegistryEntryList = getExtensionRegistryEntryList(name, servletContext);
        if (_extensionRegistryEntryList != null)  {
            Object[] _extensions = new Object[_extensionRegistryEntryList.size()];
            int _index = 0;
            for (final ExtensionRegistryEntry _extensionRegistryEntry : _extensionRegistryEntryList) {
                _extensions[_index++] = _extensionRegistryEntry.getExtension();
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "Successfully retrieved Extensions '" + name + "': " + Arrays.asList(_extensions)
                );
            }
            return _extensions;
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "Failed to retrieve Extensions '" + name + "'."
                );
            }
            return null;
        }
    }

    /**
     * Register an extension under the specified name with the specified quality.
     *
     * @param name
     * @param extension
     * @param quality
     * @param servletContext
     */
    public static void registerExtension(
        final String name, final Object extension, final int quality, final ServletContext servletContext) {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "Registering Extension '" + extension + "' with quality '" + quality + "'."
            );
        }
        Map<String, List<ExtensionRegistryEntry>> _extensionRegistryMap = getExtensionRegistryMap(servletContext);
        List<ExtensionRegistryEntry> _extensionRegistryEntryList;
        if (_extensionRegistryMap.containsKey(name)) {
            _extensionRegistryEntryList = _extensionRegistryMap.get(name);
        } else {
            _extensionRegistryEntryList = new ArrayList<ExtensionRegistryEntry>();
            _extensionRegistryMap.put(name, _extensionRegistryEntryList);
        }
        for (final ExtensionRegistryEntry _extensionRegistryEntry : _extensionRegistryEntryList) {
            if (_extensionRegistryEntry.getExtension() == extension) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(
                        Level.FINE,
                        "Extension '" + extension + "' has already been registered."
                    );
                }
                return;
            }
        }
        _extensionRegistryEntryList.add(new ExtensionRegistryEntry(extension, quality));
        Collections.sort(_extensionRegistryEntryList);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "Successfully registered Extension '" + extension + "' with quality '" + quality + "'."
            );
        }
    }

    public static void unregisterExtension(
        final String name, final Object extension, final ServletContext servletContext) {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Unregistering Extension '" + extension + "'.");
        }
        Map<String, List<ExtensionRegistryEntry>> _extensionRegistryMap = getExtensionRegistryMap(servletContext);
        List<ExtensionRegistryEntry> _extensionRegistryEntryList;
        if (_extensionRegistryMap.containsKey(name)) {
            _extensionRegistryEntryList = _extensionRegistryMap.get(name);
            for (final ExtensionRegistryEntry _extensionRegistryEntry : _extensionRegistryEntryList) {
                if (_extensionRegistryEntry.getExtension() == extension) {
                    _extensionRegistryEntryList.remove(_extensionRegistryEntry);
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(
                            Level.FINE,
                            "Successfully unregistered Extension '" + extension + "'."
                        );
                    }
                    return;
                }
            }
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "Extension '" + extension + "' has not been registered."
            );
        }
    }

    private static List<ExtensionRegistryEntry> getExtensionRegistryEntryList(
        final String name, final ServletContext servletContext) {

        return getExtensionRegistryMap(servletContext).get(name);
    }

    private static Map<String, List<ExtensionRegistryEntry>> getExtensionRegistryMap(
        final ServletContext servletContext) {

        setExtensionRegistryMapIfNeeded(new HashMap<String, List<ExtensionRegistryEntry>>(), servletContext);
        return (Map<String, List<ExtensionRegistryEntry>>)servletContext.getAttribute(EXTENSION_REGISTRY_MAP_NAME);
    }

    private static void setExtensionRegistryMapIfNeeded(
        final Map<String, List<ExtensionRegistryEntry>> extensionRegistryMap, final ServletContext servletContext) {

        if (servletContext.getAttribute(EXTENSION_REGISTRY_MAP_NAME) == null) {
            servletContext.setAttribute(EXTENSION_REGISTRY_MAP_NAME, extensionRegistryMap);
        }
    }

    private static class ExtensionRegistryEntry
    implements Comparable {
        private final Object extension;
        private final int quality;

        private ExtensionRegistryEntry(final Object extension, final int quality) {
            this.extension = extension;
            this.quality = quality;
        }

        public int compareTo(final Object object)
        throws ClassCastException, NullPointerException {
            return getQuality() - ((ExtensionRegistryEntry)object).getQuality();
        }

        @Override
        public String toString() {
            return
                new StringBuilder().
                    append("ExtensionRegistryEntry[").
                        append("extension: '").append(getExtension()).append("', ").
                        append("quality: '").append(getQuality()).append("', ").
                    append("]").
                        toString();
        }

        protected Object getExtension() {
            return extension;
        }

        protected int getQuality() {
            return quality;
        }
    }
}
