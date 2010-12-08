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
package com.icesoft.faces.presenter.chat;

import com.icesoft.faces.presenter.participant.Participant;
import com.icesoft.faces.presenter.presentation.Presentation;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Class used to translate the participant chat log into a returnable text
 * stream.
 */
public class ChatLogServlet extends HttpServlet {
    /**
     * Method called when the servlet is viewed by a user This will attempt to
     * get the chat log from the presentation and return it
     *
     * @param request  from the user
     * @param response to the user
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) {
        try {
            // To reach the presentation we need to get the participant used in the page
            // This means retrieving the faces context and working from there
            // However because this servlet is standalone, quite a few steps are required

            // First step is to get a life cycle factory where we will eventually get the faces context from
            LifecycleFactory lcfactory = (LifecycleFactory) FactoryFinder
                    .getFactory(FactoryFinder.LIFECYCLE_FACTORY);

            if (lcfactory != null) {
                // Get the first lifecycle from the factory if the factory was valid
                Lifecycle lc = lcfactory.getLifecycle(
                        lcfactory.getLifecycleIds().next().toString());

                if (lc != null) {
                    // Get a default faces context factory
                    FacesContextFactory fcfactory =
                            (FacesContextFactory) FactoryFinder.getFactory(
                                    FactoryFinder.FACES_CONTEXT_FACTORY);

                    // Get the correct faces context from the factory and based on the lifecycle
                    FacesContext fc = fcfactory.getFacesContext(
                            request.getSession().getServletContext(), request,
                            response, lc);

                    if (fc != null) {
                        // Now get the application scope from the faces context
                        Application application = fc.getApplication();

                        // Next we can get the participant object based on the value binding tag of #{participant}
                        Participant participant = ((Participant) application
                                .createValueBinding("#{participant}")
                                .getValue(fc));

                        if (participant != null) {
                            // Get the presentation from the participant, then the message log from the presentation
                            Presentation presentation =
                                    participant.getPresentation();
                            MessageLog messageLog =
                                    presentation.getMessageLog();

                            // Loop through the message log and print the results to the response stream
                            if (messageLog.size() > 0) {
                                response.setContentType("text/plain");
                                PrintWriter responseStream =
                                        response.getWriter();

                                responseStream.println(new Date(
                                        System.currentTimeMillis()) + " - " +
                                                                    presentation
                                                                            .getName());

                                for (int i = 0; i < messageLog.size(); i++) {
                                    responseStream.println(
                                            messageLog.get(i).toString());
                                }

                                responseStream.close();
                            }
                        }
                    }
                }
            }
        } catch (Exception failed) {
            failed.printStackTrace();
        }
    }
}