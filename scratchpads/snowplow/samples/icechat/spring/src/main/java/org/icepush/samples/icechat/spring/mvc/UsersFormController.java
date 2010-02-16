package org.icepush.samples.icechat.spring.mvc;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class UsersFormController implements Controller {

    protected final Log logger = LogFactory.getLog(getClass());

    private ChatFormData chatFormData;

    public UsersFormController() {
        super();
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) throws ServletException, IOException {
        return new ModelAndView("users", "chat", chatFormData);
    }

    public ChatFormData getChatFormData() {
        return chatFormData;
    }

    public void setChatFormData(ChatFormData chatFormData) {
        this.chatFormData = chatFormData;
    }
}
