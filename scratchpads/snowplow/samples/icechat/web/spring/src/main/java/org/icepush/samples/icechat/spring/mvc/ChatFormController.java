package org.icepush.samples.icechat.spring.mvc;

import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icepush.samples.icechat.spring.impl.BasePushRequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

public class ChatFormController extends AbstractFormController {

    protected final Log logger = LogFactory.getLog(getClass());

    private ChatFormData chatFormData;

    public ChatFormController() {
        super();
    }

    public ModelAndView showForm(HttpServletRequest request, HttpServletResponse response,
                                 BindException errors) throws Exception {
        return new ModelAndView("chat", "chat", chatFormData);
    }

    public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response,
                                                 Object command, BindException errors) throws Exception {
        if (chatFormData.getPushRequestContext() == null) {
            chatFormData.setPushRequestContext(new BasePushRequestContext(request, response));
        }

        if (request.getParameter("submit.newRoom") != null) {
            newRoom();
        }
        else if (request.getParameter("submit.joinRoom") != null) {
            joinRoom(request.getParameter("submit.joinRoom.name"));
        }

        return new ModelAndView("chat", "chat", chatFormData);
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        return chatFormData;
    }

    private void newRoom() {
        chatFormData.getChatManagerViewController().setNewChatRoomBean(chatFormData.getNewChatRoom());
        chatFormData.getChatManagerViewController().createNewChatRoom();
        
        // Reset the desired name
        chatFormData.getNewChatRoom().setName(null);
    }

    private void joinRoom(String name) {
        if (name != null) {
            chatFormData.getChatManagerViewController().openChatSession(name);
        }
    }

    public ChatFormData getChatFormData() {
        return chatFormData;
    }

    public void setChatFormData(ChatFormData chatFormData) {
        this.chatFormData = chatFormData;
    }
}
