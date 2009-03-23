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
package com.icesoft.faces.presenter.presentation;

import com.icesoft.faces.async.render.OnDemandRenderer;
import com.icesoft.faces.presenter.participant.Participant;
import com.icesoft.faces.presenter.util.MessageBundleLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Singleton class used to handle the various presentations for webmc This means
 * creating, removing, retrieving, etc. In addition some validation helper
 * methods are provided, such as checking for an existing presentation Very
 * little validation is done for the presentation management, as it is assumed
 * valid users will be joining the proper presentation
 */
public class PresentationManager {
    public static final int SLIDE_PRELOAD_COUNT = 3;
    public static final String CHAT_LOG_EXTENSION = ".txt";

    private static Log log = LogFactory.getLog(PresentationManager.class);

    private static PresentationManager singleton = null;
    private Map presentationMap = Collections.synchronizedMap(new HashMap());

    /**
     * Plain constructor, which is private to fulfill the singleton role
     */
    private PresentationManager() {
    }

    /**
     * Method to return the singleton instance of this class
     *
     * @return instance of this class
     */
    public static synchronized PresentationManager getInstance() {
        if (singleton == null) {
            singleton = new PresentationManager();
        }

        return (singleton);
    }

    public Map getPresentationMap() {
        return presentationMap;
    }

    /**
     * Method to create a new presentation based on the passed variables In
     * addition to generating the new presentation, this will add a chat message
     * and the creator participant to get things rolling
     *
     * @param participant moderator who created and owns the presentation
     * @param name        of the presentation
     * @param renderer    to use with the presentation
     * @return the created presentation
     */
    public Presentation createPresentation(Participant participant, String name,
                                           OnDemandRenderer renderer) {
        Presentation toReturn = null;

        if (!presentationExists(name)) {
            toReturn = new Presentation(name,
                                        participant.getLoginBean().getPresentationPassword(),
                                        participant, renderer);

            toReturn.addChatMessage(MessageBundleLoader.getMessage("bean.presentationManager.createPresentation.chatMessage.user"),
            		MessageBundleLoader.getMessage("bean.presentationManager.createPresentation.chatMessage.text"));

            presentationMap.put(name, toReturn);
        }

        return toReturn;
    }

    /**
     * Method to join an existing presentation Ideally this would be called by a
     * ROLE_VIEWER participant after presentationExists has returned true, and a
     * slot count check has been performed
     *
     * @param participant  who wishes to join
     * @param presentation to join
     */
    public void joinPresentation(Participant participant,
                                 Presentation presentation) {
        presentation.addParticipant(participant);
    }

    /**
     * Method to remove a presentation from this manager's care
     *
     * @param presentation to remove
     * @return the removed object
     */
    public Object removePresentation(Presentation presentation) {
        return presentationMap.remove(presentation.getName());
    }

    /**
     * Method to retrieve a named presentation from the Map If the presentation
     * does not exist, a null is returned The proper way to check for existing
     * presentations is to use the presentationExists method instead
     *
     * @param name of the presentation to retrieve
     * @return the presentation, or null if not found
     */
    public Presentation getPresentation(String name) {
        Object toReturn = presentationMap.get(name);

        if (toReturn != null) {
            return (Presentation) toReturn;
        }

        return null;
    }

    /**
     * Method to return the presentation map as a list
     *
     * @return the list
     */
    public Presentation[] getPresentationList() {
        return (Presentation[]) presentationMap.values()
                .toArray(new Presentation[presentationMap.size()]);
    }

    /**
     * Method to determine if a presentation exists in the map or not
     *
     * @param name of the presentation to check for
     * @return true if it exists
     */
    public boolean presentationExists(String name) {
        return presentationMap.containsKey(name);
    }

    /**
     * Method to determine if a password is already attached to an existing
     * presentation
     *
     * @param name of the presentation
     * @return true if the password exists in another presentation
     */
    public boolean passwordExists(String name) {
        Iterator iter = presentationMap.values().iterator();
        while (iter.hasNext()) {
            if (((Presentation) iter.next()).getPassword().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to authenticate a password against a named presentation
     *
     * @param password to check
     * @param name     of the presentation
     * @return true if the password matches for the presentation
     */
    public boolean isPasswordAndPresentationMatch(String password,
                                                  String name) {
    	return presentationExists(name) &&
               getPresentation(name).getPassword().equals(password);
    }
}