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
