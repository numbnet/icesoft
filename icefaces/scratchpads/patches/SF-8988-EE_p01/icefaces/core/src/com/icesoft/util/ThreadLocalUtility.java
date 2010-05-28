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

package com.icesoft.util;

import com.icesoft.faces.context.BridgeFacesContext;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Need to ensure thread local variables are nulled out on outbound code paths
 * (servlet and server push + wherever else!) to allow GC. ThreadLocal references can hold onto
 * (Server thread Count * 2 + Server push Render executor pool thread count )
 * objects each with their own UIComponent tree in memory.
 *
 * This utility class just allows clients to check without having compile time
 * includes to core classes, and really only needs to be done at development time.
 *
 */
public class ThreadLocalUtility {

    private static final Log log = LogFactory.getLog(ThreadLocalUtility.class);

    public static final int EXITING_SERVLET_CORE = 0;
    public static final int EXITING_SERVER_PUSH = 1;
    public static final int EXITING_SESSION_MONITOR = 2;
    private static final int size = 3;

    private static int[] Counts = new int[size];

    private static String Descriptions[] = {"Exiting servlet execution",
                                            "Exiting server push",
                                            "Exiting session expiry monitor"}; 

    private static boolean threadLocalFailed;
    private static String firstFailure;

    // whether to keep counts and do tests or not
    private static boolean testingEnabled; 

    /**
     * Check to see if applicable ThreadLocals are cleared. Very verbose, so
     * call only if at suitable log levels.
     * 
     * @param location an indication where test is being performed
     */
    public static void checkThreadLocals(int location) {

        if (!testingEnabled) {
            return;
        } 

        Counts[location] ++;
        if (!BridgeFacesContext.isThreadLocalNull()) {
            if (!threadLocalFailed) {
                firstFailure = "BridgeFacesState failure, location: " + Descriptions[location];
            }
            threadLocalFailed = true;
            log.error("BridgeFacesContext ThreadLocal is NON-NULL: " +  Descriptions[location]);
        }  else {
            log.debug("BridgeFacesContext ThreadLocal is OK: " +  Descriptions[location] );
        }

        if (!PersistentFacesState.isThreadLocalNull()) {

            if (!threadLocalFailed) {
                firstFailure = "PersistentFacesState failure, location: " +  Descriptions[location];
            } 
            threadLocalFailed = true;

            log.error("PersistentFacesState ThreadLocal is NON-NULL: " +  Descriptions[location]);
        } else {
            log.debug("PersistentFacesState ThreadLocal is OK: " +  Descriptions[location] );
        }
    }

    public static boolean isFailed() {
        return threadLocalFailed;
    }

    public static String getFailureLocation() {
        return firstFailure;
    }

    public static int[] getTestCounts() {
        return Counts;
    }


    public static void setPerformTesting(boolean testingEnabled) {
        ThreadLocalUtility.testingEnabled = testingEnabled;
    }
}
