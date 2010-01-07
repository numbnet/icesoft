package org.icepush.samples.icechat.spring.mvc;

import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

public class ChatFormController extends AbstractFormController {

    protected final Log logger = LogFactory.getLog(getClass());

    private PushRequestManager pushRequestManager;
    private ChatFormData chatFormData;

    public ChatFormController() {
        super();
    }

    public ModelAndView showForm(HttpServletRequest request, HttpServletResponse response,
                                 BindException errors) throws Exception {
        if (chatFormData.getPushRequestContext() == null) {
            chatFormData.setPushRequestContext(pushRequestManager.getPushRequestContext());
        }

        return new ModelAndView("chat", "chat", chatFormData);
    }

    public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response,
                                                 Object command, BindException errors) throws Exception {
        if (request.getParameter("submit.sendMessage") != null) {
            sendMessage();
        }
        else if (request.getParameter("submit.newRoom") != null) {
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

    private void sendMessage() {
        chatFormData.getChatManagerViewController().setNewChatRoomMessageBean(chatFormData.getNewMessage());
        chatFormData.getChatManagerViewController().sendNewMessage();

        // Reset the message text
        chatFormData.getNewMessage().setMessage(null);
    }

    private void newRoom() {
        logger.info("Create Room '" + chatFormData.getNewChatRoom().getName() + "' by " + chatFormData.getLoginController().getCurrentUser().getUserName());

        chatFormData.getChatManagerViewController().setNewChatRoomBean(chatFormData.getNewChatRoom());
        chatFormData.getChatManagerViewController().createNewChatRoom();
        
        // Reset the desired name
        chatFormData.getNewChatRoom().setName(null);
    }

    private void joinRoom(String name) {
        logger.info("Join Room '" + name + "' by " + chatFormData.getLoginController().getCurrentUser().getUserName());

        if (name != null) {
            chatFormData.getChatManagerViewController().openChatSession(name);
        }
    }

    public PushRequestManager getPushRequestManager() {
        return pushRequestManager;
    }

    public void setPushRequestManager(PushRequestManager pushRequestManager) {
        this.pushRequestManager = pushRequestManager;
    }

    public ChatFormData getChatFormData() {
        return chatFormData;
    }

    public void setChatFormData(ChatFormData chatFormData) {
        this.chatFormData = chatFormData;
    }
}
