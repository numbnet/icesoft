/*
 * Version: MPL 1.1
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
 * The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icepush.samples.icechat.spring.mvc;

import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icepush.samples.icechat.spring.impl.BaseLoginController;
import org.icepush.samples.icechat.spring.impl.BaseChatManagerFacade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.util.Map;

public class ChatFormController extends AbstractFormController {

    protected final Log logger = LogFactory.getLog(getClass());

    private ChatFormData chatFormData;
    private BaseLoginController loginController;
    private BaseChatManagerFacade chatManagerFacade;

    public ChatFormController() {
        super();
    }

    public ModelAndView showForm(HttpServletRequest request, HttpServletResponse response,
                                 BindException errors) throws Exception {
        Map<String,Object> model = errors.getModel();
        model.put("loginController", loginController);
        model.put("chatManagerFacade", chatManagerFacade);

        logger.info("showForm " + errors.getModel());

        return new ModelAndView("talk", model);
    }

    public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response,
                                                 Object command, BindException errors) throws Exception {
        logger.info("processFormSubmission " + errors.getModel());

        if (request.getParameter("submit.sendMessage") != null) {
            sendMessage();
        }
        else if (request.getParameter("submit.newRoom") != null) {
            newRoom();
        }
        else if (request.getParameter("submit.joinRoom") != null) {
            joinRoom(request.getParameter("submit.joinRoom.name"));
        }

        Map<String,Object> model = errors.getModel();
        model.put("loginController", loginController);
        model.put("chatManagerFacade", chatManagerFacade);

        logger.info("processFormSubmission " + errors.getModel());

        return new ModelAndView("talk", model);
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        return chatFormData;
    }

    private void sendMessage() {
        if ((chatFormData.getNewMessage() != null) &&
            (chatFormData.getNewMessage().getMessage() != null)) {
            if (chatFormData.getNewMessage().getMessage().trim().length() > 0) {
                chatFormData.getChatManagerViewController().setNewChatRoomMessageBean(chatFormData.getNewMessage());
                chatFormData.getChatManagerViewController().sendNewMessage();
            }

            // Reset the message text
            chatFormData.getNewMessage().setMessage(null);
        }
    }

    private void newRoom() {
        if (chatFormData.getNewChatRoom().getName() == null) {
            return;
        }

        if (chatFormData.getNewChatRoom().getName().trim().length() == 0) {
            chatFormData.getNewChatRoom().setName("Default");
        }

        logger.info("Create Room '" + chatFormData.getNewChatRoom().getName() + "' by " + loginController.getCurrentUser().getName());

        chatFormData.getChatManagerViewController().setNewChatRoomBean(chatFormData.getNewChatRoom());
        chatFormData.getChatManagerViewController().createNewChatRoom();
        chatFormData.getChatManagerViewController().openChatSession(chatFormData.getNewChatRoom().getName());

        // Reset the desired name
        chatFormData.getNewChatRoom().setName(null);
    }

    private void joinRoom(String name) {
        if (name != null) {
            logger.info("Join Room '" + name + "' by " + loginController.getCurrentUser().getName());

            chatFormData.getChatManagerViewController().openChatSession(name);
        }
    }

    public ChatFormData getChatFormData() {
        return chatFormData;
    }

    public void setChatFormData(ChatFormData chatFormData) {
        this.chatFormData = chatFormData;
    }

    public BaseLoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(BaseLoginController loginController) {
        this.loginController = loginController;
    }

    public BaseChatManagerFacade getChatManagerFacade() {
        return chatManagerFacade;
    }

    public void setChatManagerFacade(BaseChatManagerFacade chatManagerFacade) {
        this.chatManagerFacade = chatManagerFacade;
    }
}
