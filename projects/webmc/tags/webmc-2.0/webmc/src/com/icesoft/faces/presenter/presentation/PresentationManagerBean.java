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
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.presenter.participant.Participant;
import com.icesoft.faces.presenter.util.MessageBundleLoader;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Class to handle the UI level representation of the presentation manager Looks
 * after things like the login screen presentation list, requesting addition of
 * new users to presentations, etc.
 */
public class PresentationManagerBean {
    public static final String DEFAULT_PRESENTATION = MessageBundleLoader.getMessage("bean.presentationManagerBean.defaultPresentation");
    private static final String NO_PRESENTATION = MessageBundleLoader.getMessage("bean.presentationManagerBean.noPresentation");

    private PresentationManager backendManager =
            PresentationManager.getInstance();
    private String currentPresentationsSelection = NO_PRESENTATION;
    private RenderManager renderManager;
    private OnDemandRenderer loginPageRenderer;

    public PresentationManagerBean() {
    }

    public String getCurrentPresentationsSelection() {
        return currentPresentationsSelection;
    }

    public RenderManager getRenderManager() {
        return renderManager;
    }

    /**
     * Method to set the render manager This should be called automatically from
     * faces-config In addition this method will setup the loginPageRenderer
     *
     * @param renderManager to set
     */
    public void setRenderManager(RenderManager renderManager) {
        this.renderManager = renderManager;
        loginPageRenderer =
                renderManager.getOnDemandRenderer("loginPageRenderer");
    }

    /**
     * Method to get a named presentation from the back end manager
     *
     * @param name of the presentation
     * @return the presentation, or null if it doesn't exist
     */
    public Presentation getPresentation(String name) {
        return backendManager.getPresentation(name);
    }

    /**
     * Method to get the default presentation selection text This is used for
     * the initial state in dropdowns on the login screen
     *
     * @return DEFAULT_PRESENTATION string
     */
    public String getDefaultPresentationSelection() {
        return DEFAULT_PRESENTATION;
    }

    /**
     * Method to get the list of presentations as a SelectItem list. This
     * basically wraps the back end list.
     * In addition this will create default initial presentations as needed
     * These initial presentations are loaded automatically and run in slide 
     * show mode, and can be used to provide a generic set of presentations that 
     * any user can join (so a deployment could include a "Howto" presentation, 
     * etc.)  To setup a default presentation, just drop the proper zip file 
     * (normally the type that would be uploaded to webmc by a moderator) into 
     * the AutoPresentation.PRESENTATION_FOLDER_NAME folder, which is located 
     * normally in .../web/basepres/  Any files found in this folder will be 
     * loaded by AutoPresentation.createDefaultPresentation as a new 
     * presentation (name based on the file), with a password of 'password' 
     *
     * @return the presentations as SelectItems
     */
    public SelectItem[] getPresentationItems() {
        Map presentations = backendManager.getPresentationMap();

        if (presentations.isEmpty()) {
            AutoPresentation.createDefaultPresentation(this);

            if (presentations.isEmpty()) {
                currentPresentationsSelection = NO_PRESENTATION;
                return new SelectItem[]{new SelectItem(NO_PRESENTATION)};
            }
        }

        Iterator presIter = presentations.keySet().iterator();
        ArrayList presentationNames = new ArrayList(presentations.size());
        presentationNames.add(new SelectItem(DEFAULT_PRESENTATION));
        
        Presentation current;
        while (presIter.hasNext()) {
            current = (Presentation)presentations.get((String)presIter.next());
            presentationNames.add(1,new SelectItem(current.getName()));
        }

        currentPresentationsSelection = DEFAULT_PRESENTATION;
        return (SelectItem[]) presentationNames
                .toArray(new SelectItem[presentationNames.size()]);
    }

    /**
     * Method to create a new presentation This relies on the back end manager to
     * do the real work, besides handing off the local renderer
     *
     * @param participant who created the presentation
     * @param name        of the presentation to create
     * @return the created presentation, or null if something went wrong
     */
    public Presentation createPresentation(Participant participant,
                                           String name) {
        Presentation toReturn =
                backendManager.createPresentation(participant, name,
                                                  renderManager.getOnDemandRenderer(
                                                          name));

        if (toReturn != null) {
            toReturn.setManager(this);
        }
        // The presentation list should be updated.
        loginPageRenderer.requestRender();

        return toReturn;
    }

    /**
     * Method to add a participant to a presentation The presentation should
     * exist first before attempting to add to it
     *
     * @param participant  to add to the presentation
     * @param presentation to join
     */
    public void joinPresentation(Participant participant,
                                 Presentation presentation) {
        backendManager.joinPresentation(participant, presentation);
    }

    /**
     * Method to end a presentation, such as when the moderator logs out
     * Basically deletes the presentation from the list, and renders the login
     * screen
     *
     * @param presentation to remove
     */
    public void endPresentation(Presentation presentation) {
        backendManager.removePresentation(presentation);
        // The presentation list should be updated.
        loginPageRenderer.requestRender();
    }
}