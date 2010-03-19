/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
 
package com.icesoft.faces.facelets;

import java.io.IOException;
import java.net.URL;
import java.lang.reflect.Method;

import javax.faces.context.FacesContext;

import com.sun.facelets.impl.ResourceResolver;
import com.sun.facelets.impl.DefaultResourceResolver;
import com.icesoft.util.SeamUtilities;

/**
 * Intercepts any request for a path like /debug.xxx and renders
 * the Seam debug page using facelets.
 *
 */
public class SeamDebugResourceResolver implements ResourceResolver {
    private static final String Init_className =
        "org.jboss.seam.core.Init";
    private static final String SeamDebugPhaseListener_className =
        "org.jboss.seam.debug.jsf.SeamDebugPhaseListener";
    private static Class Init_class;
    private static Method Init_instance_method;
    private static Method Init_isDebug_method;
    private static Class SeamDebugPhaseListener_class;
    private static boolean loaded = false;
    
    public static ResourceResolver build(ResourceResolver delegate) {
        if( delegate == null ) {
            throw new IllegalArgumentException(
                "SeamDebugResourceResolver must have valid delegate ResourceResolver");
        }
        if( !loadSeamDebugClasses() )
            return null;
        return new SeamDebugResourceResolver( delegate );
    }
    
    private static boolean loadSeamDebugClasses() {
        if( !loaded ) {
            try {
                ClassLoader dbgClassLoader =
                    SeamUtilities.getSeamDebugPhaseListenerClassLoader();
                if( dbgClassLoader == null ) {
                    dbgClassLoader =
                        Thread.currentThread().getContextClassLoader();
                }
                Init_class = Class.forName(
                    Init_className, true, dbgClassLoader);
                Init_instance_method = Init_class.getMethod(
                    "instance", new Class[0]);
                Init_isDebug_method = Init_class.getMethod(
                    "isDebug", new Class[0]);
                SeamDebugPhaseListener_class = Class.forName(
                    SeamDebugPhaseListener_className, true, dbgClassLoader);
                loaded = true;
            }
            catch(Exception e) {
//e.printStackTrace();
                // Silently fail, since it's valid to not be in Seam,
                //  or not have the Seam debug JAR available
                Init_class = null;
                Init_instance_method = null;
                Init_isDebug_method = null;
                SeamDebugPhaseListener_class = null;
                loaded = false;
            }
        }
        return loaded;
    }

    private ResourceResolver delegate;

    private SeamDebugResourceResolver(ResourceResolver delegate) {
        this.delegate = delegate;
    }

    public URL resolveUrl(String path) {
//System.out.println("SeamDebugResourceResolver.resolveUrl()  path: " + path);
        if ( path!=null && path.startsWith("/debug.") && Init_instance_isDebug() )
        {
            URL url = SeamDebugPhaseListener_class.getClassLoader().getResource("META-INF/debug.xhtml");
//System.out.println("SeamDebugResourceResolver.resolveUrl()  url: " + url);
            return url;
        }
        return delegate.resolveUrl(path);
    }
    
    private static boolean Init_instance_isDebug() {
        try {
            Object instance = Init_instance_method.invoke(null, new Object[0]);
            Object isDebug = Init_isDebug_method.invoke(instance, new Object[0]);
            return ((Boolean) isDebug).booleanValue();
        }
        catch(Exception e) {
//e.printStackTrace();
            return false;
        }
    }
}
