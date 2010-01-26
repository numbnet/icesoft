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

public class MessagesFormController extends AbstractFormController {

    protected final Log logger = LogFactory.getLog(getClass());

    private PushRequestManager pushRequestManager;
    private BaseLoginController loginController;
    private ChatFormData chatFormData;

    public MessagesFormController() {
        super();
    }

    public ModelAndView showForm(HttpServletRequest request, HttpServletResponse response,
                                 BindException errors) throws Exception {
        logger.info("showForm " + errors.getModel());

        Map<String,Object> model = errors.getModel();
        model.put("chat", chatFormData);

        return showForm(request, errors, "messages", model);
    }

    public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response,
                                                 Object command, BindException errors) throws Exception {
        logger.info("processFormSubmission " + errors.getModel());
        //this method is not used, consider a different controller type

        ModelAndView modelAndView = new ModelAndView("messages");

        return modelAndView;
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        logger.info("MessagesFormController formBackingObject ");
        return "MessagesFormControllerBackingObject";
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

}
