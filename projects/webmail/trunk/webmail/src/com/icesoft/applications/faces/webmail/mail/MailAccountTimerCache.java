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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 *
 */
package com.icesoft.applications.faces.webmail.mail;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TimerTask;

/**
 * <p>This class is responsible for keeping track TimerTasks which have
 * been added the mediators Timer.  These references are kept to insure that
 * when a session expires we correctly remove the task from the timer.  If this
 * is not done then it is possible for the Timer task to keep executing well
 * after the session was destroyed. </p>
 *
 * @author ICEsoft Technologies, Inc.
 */
public class MailAccountTimerCache {

    private static Map timerList = new HashMap(30);

    private MailAccountTimerCache(){

    }

    /**
     * <p>Adds a mail account timer to an internal hash indexed by the session id.
     * This record keeping allows us to make sure that a mail accounts timer
     * is cancelled when the session is expired.
     * </p>
     * @param session session of current user.
     * @param timerTask timer task object associated with a mail account, used to tickel
     * the mail account to insure we get events related to new or removed messages.
     */
    public static void cacheMailAccountTimer(HttpSession session, TimerTask timerTask ){
        // check to see if this session has already been cached.
        ArrayList cachedAccountTimers = (ArrayList)timerList.get(session.getId());
        if (cachedAccountTimers != null){
            // add our new timer to this list
            cachedAccountTimers.add(timerTask);
        }
        // no entry, so we insert a new list
        else{
            cachedAccountTimers = new ArrayList(1);
            cachedAccountTimers.add(timerTask);
            // add the list to the cache
            timerList.put(session.getId(),  cachedAccountTimers);
        }
    }

    /**
     * <p>Release the mail account associated with the specified session id.
     * This method should be called when a session is expired to release the
     * appropriate anonymous account.</p>
     *
     * @param session HTTP session associated with a anonymous user account who
     *                  should be released and put back in the user pool.
     */
    public static void releaseMailAccountTimers(HttpSession session) {
        if (session != null) {
            // find out if there is an timer in the map for this session
            ArrayList cachedAccountTimers = (ArrayList)timerList.get(session.getId());
            if ( cachedAccountTimers != null ){
                for (int i = 0; i < cachedAccountTimers.size(); i++) {
                    if (cachedAccountTimers.get(i) != null ){
                        ((TimerTask)cachedAccountTimers.get(i)).cancel();
                    }
                }
            }

        }
    }

}
