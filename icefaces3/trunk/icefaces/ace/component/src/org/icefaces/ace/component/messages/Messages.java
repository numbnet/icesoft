/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.ace.component.messages;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Messages extends MessagesBase {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    class AceFacesMessage {
        String getClientId() {
            return clientId;
        }

        FacesMessage getFacesMessage() {
            return facesMessage;
        }

        private String clientId;
        private FacesMessage facesMessage;

        AceFacesMessage(String clientId, FacesMessage facesMessage) {
            this.clientId = clientId;
            this.facesMessage = facesMessage;
        }
    }

    public Iterator getMsgList(FacesContext context) {
        String sourceMethod = "getMsgList";
        List<AceFacesMessage> msgList = new ArrayList<AceFacesMessage>();
        String forId = (forId = getFor()) == null ? "@all" : forId.trim();
        Iterator messageIter;
        if (forId.equals("@all")) {
            if (isGlobalOnly()) {
                messageIter = context.getMessages(null);
                while (messageIter.hasNext()) {
                    msgList.add(new AceFacesMessage(null, (FacesMessage) messageIter.next()));
                }
            } else {
                Iterator<String> clientIdIter = context.getClientIdsWithMessages();
                while (clientIdIter.hasNext()) {
                    String clientId = clientIdIter.next();
                    messageIter = context.getMessages(clientId);
                    while (messageIter.hasNext()) {
                        msgList.add(new AceFacesMessage(clientId, (FacesMessage) messageIter.next()));
                    }
                }
            }
        } else {
            UIComponent forComponent = forId.equals("") ? null : findComponent(forId);
            if (forComponent == null) {
                logger.logp(Level.WARNING, logger.getName(), sourceMethod, "'for' attribute value cannot be empty or non-existent id.");
                msgList = Collections.emptyList();
            } else {
                String clientId = forComponent.getClientId(context);
                messageIter = context.getMessages(clientId);
                while (messageIter.hasNext()) {
                    msgList.add(new AceFacesMessage(clientId, (FacesMessage) messageIter.next()));
                }
            }
        }
        return msgList.iterator();
    }

    public void setPrevMsgSet(Set msgSet) {
        getStateHelper().put("prevMsgSet", msgSet);
    }

    public Set getPrevMsgSet() {
        return (Set) getStateHelper().get("prevMsgSet");
    }
}
