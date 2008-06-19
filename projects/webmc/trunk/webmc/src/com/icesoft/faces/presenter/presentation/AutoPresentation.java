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

import com.icesoft.faces.presenter.participant.Participant;
import com.icesoft.faces.presenter.participant.ParticipantInfo;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class used to setup any initial default presentations
 * This basically reads the contents of PRESENTATION_FOLDER_NAME (which
 * is normally .../web/basepres/) and creates an automated slide show
 * presentation for any valid files found
 */
public class AutoPresentation {
    private static Log log = LogFactory.getLog(AutoPresentation.class);
    private static String[] defaultPresentationNames;

    public static final String PRESENTATION_FOLDER_NAME = "basepres";
    public static final String DEFAULT_PASSWORD = "";

    /**
     * Method to get the default presentation names as a String list
     *
     * @return defaultPresentationNames
     */
    public static String[] getDefaultPresentationNames() {
        return defaultPresentationNames;
    }

    /**
     * Method to get the default presentation names as a formatted String
     * The format would be: item1, item2, item3
     *
     * @return formatted defaultPresentationNames
     */
    public static String getDefaultPresentationNamesList() {
        StringBuffer toReturn = new StringBuffer();

        for (int i = 0; i < defaultPresentationNames.length; i++) {
            toReturn.append(defaultPresentationNames[i]);

            if (i < (defaultPresentationNames.length-1)) {
                toReturn.append(", ");
            }
        }

        return toReturn.toString();
    }

    /**
     * Method to perform the reading of the PRESENTATION_FOLDER_NAME contents,
     * and creation of any presentations
     *
     * @param parent manager
     * @return true on successful creation
     */
    public static boolean createDefaultPresentation(PresentationManagerBean parent) {
        try{
            String path = generateZipPath();

            // Do not bother if no path was found
            if (path == null) {
                return false;
            }

            // Work from the base presentation folder
            File rootPath = new File(path + File.separator + "uploadDirectory" + File.separator + PRESENTATION_FOLDER_NAME);

            // Ensure the folder exists and is readable
            if ((!rootPath.exists()) || (!rootPath.canRead())) {
                return false;
            }

            Participant owner = generateParticipant();

            // Loop through a list of files in the base presentation folder
            File[] defaultPresentations = rootPath.listFiles();
            File currentFile = null;
            defaultPresentationNames = new String[defaultPresentations.length];
            for (int i = 0; i < defaultPresentations.length; i++) {
                try{
                    currentFile = defaultPresentations[i];

                    // Create a new presentation and load the associated file
                    Presentation newPresentation = generatePresentation(parent,
                                                                        owner,
                                                                        stripFilename(
                                                                                currentFile.getName()));
                    newPresentation.load(currentFile, true);
                    defaultPresentationNames[i] = stripFilename(currentFile.getName());
                }catch (Exception readFailure) {
                    if (log.isErrorEnabled()) {
                        readFailure.printStackTrace();
                        log.error("Failed to read file \'" + currentFile + "\' as a default presentation");
                    }
                }
            }

            return true;
        }catch (Exception failedCreate) {
            if (log.isErrorEnabled()) {
                failedCreate.printStackTrace();
                log.error("Failed to create the default presentations");
            }
        }

        return false;
    }

    /**
     * Convenience method to get the root servlet context path
     * This will be used to determine where the app is deployed, and where
     * PRESENTATION_FOLDER_NAME should be read from
     * This is to make sure that any presentations in the folder are handled
     * in a similar way to a user uploaded zip file
     *
     * @return path, or null on error
     */
    private static String generateZipPath() {
        try{
            // Get the faces context first
            FacesContext fc = FacesContext.getCurrentInstance();
            if (fc != null) {
                // Then get the external context
                ExternalContext ec = fc.getExternalContext();
                if (ec != null) {
                    // Then try to get the servlet context
                    Object context = ec.getContext();
                    if (context instanceof ServletContext) {
                        // Then get the path from the cast servlet context
                        return ((ServletContext)context).getRealPath("");
                    }
                }
            }
        }catch (Exception failedPath) {
            if (log.isErrorEnabled()) {
                failedPath.printStackTrace();
                log.error("Failed to get a root path from the servlet context");
            }
        }

        return null;
    }

    /**
     * Convenience method to generate a default moderator that will run
     * each of the initial presentations
     *
     * @return default moderator
     */
    private static Participant generateParticipant() {
        Participant toReturn = new Participant();
        toReturn.getLoginBean().setPresentationPassword(DEFAULT_PASSWORD);
        toReturn.setFirstName("Automated");
        toReturn.setLastName("Control");
        toReturn.setEmail("admin@icefaces.org");
        toReturn.setRole(ParticipantInfo.ROLE_MODERATOR);

        return toReturn;
    }

    /**
     * Convenience method to generate a new presentation based on the passed 
     * values.  This is basically a wrapper for 
     * PresentationManagerBean.createPresentation, with the addition of setting 
     * up the created presentation.
     *
     * @param parent who will create the presentation
     * @param owner of the presentation
     * @param name of the new presentation
     * @return the created presentation, or null on error
     */
    private static Presentation generatePresentation(
            PresentationManagerBean parent, Participant owner, String name) {
        // Generate a new presentation using the parent manager
        // Note the sessionId is set to DEFAULT_SESSION_ID, so that the
        // initial presentations will never be removed, even if the moderator
        // crashes out
        Presentation toReturn =
                parent.createPresentation(owner, name);

        if (toReturn != null) {
            owner.getChatView().setPresentation(toReturn);
            owner.getParticipantView().setPresentation(toReturn);
            owner.setPresentation(toReturn);
            toReturn.getParticipants().add(owner);
            toReturn.setAutoPlay(true);
            toReturn.getTimer().setChangeDelay(new Integer(6));
        }

        return toReturn;
    }

    /**
     * Convenience method to remove the extension from a filename
     * So test.zip becomes test
     *
     * @param name to strip
     * @return formatted result, or initial name if no extension was found
     */
    private static String stripFilename(String name) {
        // Get the name without any file extension
        if (name.indexOf('.') != -1) {
            return name.substring(0, name.indexOf('.'));
        }

        return name;
    }
}
