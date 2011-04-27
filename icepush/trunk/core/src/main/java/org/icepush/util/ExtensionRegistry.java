/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icepush.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class ExtensionRegistry implements ServletContextListener {
    private static final String NAME = ExtensionRegistry.class.getName();

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute(NAME, new HashMap());
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    /**
     * Add an extension under the specified name with the specified "quality"
     *
     * @param context
     * @param quality
     * @param name
     * @param extension
     */
    public static void addExtension(ServletContext context, int quality, String name, Object extension) {
        Map extensions = (Map) context.getAttribute(NAME);
        TreeSet namedExtensions = (TreeSet) extensions.get(name);
        if (namedExtensions == null) {
            namedExtensions = new TreeSet();
            extensions.put(name, namedExtensions);
        }

        namedExtensions.add(new ExtensionEntry(quality, extension));
    }

    /**
     * Return the extension of the specified name with the highest "quality".
     *
     * @param context
     * @param name
     * @return
     */
    public static Object getBestExtension(ServletContext context, String name) {
        Map extensions = (Map) context.getAttribute(NAME);
        TreeSet namedExtensions = (TreeSet) extensions.get(name);
        return namedExtensions == null ? null : ((ExtensionEntry) namedExtensions.last()).extension;
    }

    /**
     * Return a Map keyed by "quality" of extensions with the specified name.
     *
     * @param context
     * @param name
     * @return
     */
    public static Map getExtensions(ServletContext context, String name) {
        Map extensions = (Map) context.getAttribute(NAME);
        TreeSet<ExtensionEntry> namedExtensions = (TreeSet) extensions.get(name);
        Map result = new HashMap();
        for (ExtensionEntry entry : namedExtensions) {
            result.put(entry.quality, entry.extension);
        }
        return result;
    }

    private static class ExtensionEntry implements Comparable {
        private int quality;
        private Object extension;

        private ExtensionEntry(int quality, Object extension) {
            this.quality = quality;
            this.extension = extension;
        }

        public int compareTo(Object o) {
            return quality - ((ExtensionEntry) o).quality;
        }
    }
}
