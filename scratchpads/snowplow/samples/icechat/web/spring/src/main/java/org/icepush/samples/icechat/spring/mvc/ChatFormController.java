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

    private PushRequestManager pushRequestManager;
    private ChatFormData chatFormData;
    private BaseLoginController loginController;
    private BaseChatManagerFacade chatManagerFacade;

    public ChatFormController() {
        super();
    }

    public ModelAndView showForm(HttpServletRequest request, HttpServletResponse response,
                                 BindException errors) throws Exception {
        Map<String,Object> model = errors.getModel();
        model.put("chat", chatFormData);
        model.put("loginController", loginController);
        model.put("chatManagerFacade", chatManagerFacade);

        return showForm(request, errors, "chat", model);
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

        ModelAndView modelAndView = new ModelAndView("chat");
        modelAndView.addObject("chat", chatFormData);
        modelAndView.addObject("loginController", loginController);
        modelAndView.addObject("chatManagerFacade", chatManagerFacade);

        return modelAndView;
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

        logger.info("Create Room '" + chatFormData.getNewChatRoom().getName() + "' by " + loginController.getCurrentUser().getUserName());

        chatFormData.getChatManagerViewController().setNewChatRoomBean(chatFormData.getNewChatRoom());
        chatFormData.getChatManagerViewController().createNewChatRoom();
        
        // Reset the desired name
        chatFormData.getNewChatRoom().setName(null);
    }

    private void joinRoom(String name) {
        if (name != null) {
            logger.info("Join Room '" + name + "' by " + loginController.getCurrentUser().getUserName());

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
        if (chatFormData.getPushRequestContext() == null) {
            chatFormData.setPushRequestContext(pushRequestManager.getPushRequestContext());
        }

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
