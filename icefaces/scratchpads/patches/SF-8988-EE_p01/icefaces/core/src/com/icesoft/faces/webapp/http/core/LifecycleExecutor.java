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

package com.icesoft.faces.webapp.http.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;

import java.util.Map;

import com.icesoft.util.SeamUtilities;

/**
 * This class refactored for ICE-5212. The SwfLifecycleExector class wasn't threadsafe
 * so don't cache a single copy. 
 */
public abstract class LifecycleExecutor {
    private static Log log = LogFactory.getLog(LifecycleExecutor.class);

    private static String JSF_EXEC = "JSF Lifecyle Executor";

    public static LifecycleExecutor getLifecycleExecutor(FacesContext context)  {

        // ICE-5212 Don't reuse SwfLifecycleExector objects, they are not threadsafe
        if (!isJsfLifecycle(context))  {
            Map appMap = context.getExternalContext().getApplicationMap();
            if (!appMap.containsKey(JSF_EXEC))  {
                try {
                    //ICE-5586: It's possible for Spring Web Flow to be loadable from the
                    //classpath but not configured to be used.
                    if(SeamUtilities.springWebFlowConfigured()){
                        LifecycleExecutor executor = new SwfLifecycleExecutor();
                        return executor;
                    }
                } catch (Throwable t)  {
                    //ClassNotFound Exception or Error variant, fall back to 
                    //standard JSF
                    if (log.isDebugEnabled()) {
                        log.debug("SpringWebFlow unavailable and is disabled for this application ");
                    }
                    appMap.put(JSF_EXEC,JSF_EXEC);
                }
            }
        }
        return  new JsfLifecycleExecutor();
    }

    public abstract void apply(FacesContext facesContext);

    private static boolean isJsfLifecycle( FacesContext facesContext) {
        Object request = facesContext.getExternalContext().getRequest();
        if (request instanceof HttpServletRequest)  {
            String requestURI = ((HttpServletRequest) request).getRequestURI();
            int slashIndex = requestURI.lastIndexOf("/");
            int dotIndex = requestURI.lastIndexOf(".");
            return (slashIndex < dotIndex);
        }
        return false;
    }

    /**
     * This might have a different implementation later.  
     * @param facesContext
     * @return A JsfLifecycleExecutor
     */
    public LifecycleExecutor getJsfLifecycleExecutor(FacesContext facesContext) {
        return new JsfLifecycleExecutor( );
    }
}