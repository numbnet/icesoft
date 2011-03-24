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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icepush.samples.icechat.beans.controller.BaseChatRoomControllerBean;
import org.icepush.samples.icechat.controller.ILoginController;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.spring.impl.BasePushRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@Scope("session")
public class ChatController extends BaseChatRoomControllerBean{

    protected final Log logger = LogFactory.getLog(getClass());

    private ILoginController loginController;
    private transient PushRequestManager pushRequestManager;
    private LoginFormData loginFormData;
    
    @Autowired
    public ChatController( LoginFormData loginFormData, PushRequestManager pushRequestManager, 
    		ILoginController loginController,
    		IChatService chatService) {
    	this.loginFormData = loginFormData;
        this.chatService = chatService;
        this.loginController = loginController;
        this.pushRequestManager = pushRequestManager;
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String showRooms(Model model, SessionStatus status) throws Exception {
    	if( loginController.getCurrentUser() == null ){
    		model.addAttribute("loginFormData", loginFormData);
    		return "login";
    	}
        model.addAttribute("loginController", loginController);
        model.addAttribute("chatService", chatService);
        status.setComplete();
        return "talk";
    }
    
    @RequestMapping(value="/rooms/{id}", method = RequestMethod.GET)
    public String joinRoom(@PathVariable long id, Model model, SessionStatus status) 
    	throws Exception {        
    	UserChatSession userChatSession = chatService.loginToChatRoom(id, loginController.getCurrentUser());
    	if( userChatSession != null ){
    		model.addAttribute("chatSession", userChatSession);
        	status.setComplete();
        	pushRequestManager.getPushRequestContext().getPushContext().push(userChatSession.getRoom().getName()+"_users");
            return "room";
    	}
    	return null;
     }
    
    @RequestMapping(value="/rooms/{id}/users/{userId}/draft", method = RequestMethod.GET)
    public String getMessageDraft(@PathVariable long id, @PathVariable long userId, Model model, SessionStatus status) 
    	throws Exception {        
    	ChatRoom room = chatService.getChatRoom(Long.toString(id));
    	if( room != null ){
    		for( UserChatSession chatSession : room.getUserChatSessions()){
    			if( chatSession.getUser().getId() == userId ){
    				model.addAttribute("chatSession", chatSession);
    				break;
    			}
    		}
    	}
        return "draft";
     }

    
    @RequestMapping(value="/roomlist", method = RequestMethod.GET)
    public String getRoomList(Model model, SessionStatus status) 
    	throws Exception {        
    	model.addAttribute("rooms", chatService.getChatRooms());
    	status.setComplete();
    	return "rooms";
     }

    
    @RequestMapping(value="/newroom", method = RequestMethod.POST)
    @ResponseBody
    public String createRoom(@RequestParam String name, Model model, SessionStatus status) 
    	throws Exception {   
    	if( name != null && name.length() > 0 ){
    		ChatRoom room = chatService.createNewChatRoomWithId(name);
        	UserChatSession userChatSession = chatService.loginToChatRoom(room.getId(), loginController.getCurrentUser());
            model.addAttribute("chatSession", userChatSession);
            status.setComplete();
            pushRequestManager.getPushRequestContext().getPushContext().push("CHAT_ROOMS");
            return Long.toString(room.getId());
    	}
    	return null;
    }

    @RequestMapping(value="/rooms/{id}/newmessage", method = RequestMethod.POST)
    public String sendMessage(@PathVariable long id, @RequestParam String message, Model model, SessionStatus status) {
        
    	chatService.sendNewMessage(id, loginController.getCurrentUser(), message);
        UserChatSession chatSession = loginController.getCurrentUser().getChatSessionByRoom(chatService.getChatRoom(Long.toString(id)));
        model.addAttribute("chatSession", chatSession);
        List<Message> messages = new ArrayList<Message>(chatSession.getRoom().getMessages());
        Collections.reverse(messages);
        model.addAttribute("messages",messages);
        status.setComplete();
        pushRequestManager.getPushRequestContext().getPushContext().push(chatSession.getRoom().getName()+"_messages");
        return "messages";
    }
        
    @RequestMapping(value="/rooms/{id}/updatedraft/", method = RequestMethod.POST)
    @ResponseBody
    public String updateDraft(@PathVariable long id, @RequestParam String message, Model model, SessionStatus status) {
        
    	ChatRoom room = chatService.getChatRoom(Long.toString(id));
    	if( room != null ){
    		chatService.updateCurrentDraft(message, Long.toString(id), loginController.getCurrentUser());
            pushRequestManager.getPushRequestContext().getPushContext().push(getUserDraftGroupName(chatService.getUserChatSession(room.getName(), loginController.getCurrentUser())));
    	}
    	return "";
    }
    
    private String getUserDraftGroupName(UserChatSession session){
    	return Long.toString(session.getRoom().getId())+"_"+Long.toString(session.getUser().getId())+"_draft";
    }
    
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ModelAndView login(@ModelAttribute("loginFormData") LoginFormData loginFormData, BindingResult result, SessionStatus status,
    		HttpServletRequest request, HttpServletResponse response)
                                 throws Exception {
    	
    	new LoginFormValidator().validate(loginFormData, result);
    	
        if (!result.hasErrors()) {
            try{
                if (loginController.getPushRequestContext() == null) {
                    pushRequestManager.setPushRequestContext(new BasePushRequestContext(request, response));

                    loginController.setPushRequestContext(pushRequestManager.getPushRequestContext());
                }
                loginController.setUserName(loginFormData.getUserName());
                loginController.login();
                status.setComplete();
                return new ModelAndView(new RedirectView("./"));
            }catch (Exception failedLogin) {
                logger.error("Failed to attempt to log a user in", failedLogin);
            }
        }

        return new ModelAndView(new RedirectView("./login"));
    }
    
    @RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("loginFormData", loginFormData);
		return "login";
	}
    
    @RequestMapping(value="/rooms/{id}/users", method = RequestMethod.GET)
	public String getRoomUsers(@PathVariable long id, Model model) {
    	ChatRoom room = chatService.getChatRoom(Long.toString(id));
    	if( room != null ){
    		model.addAttribute("chatSessions",room.getUserChatSessions());
    		model.addAttribute("currentUser", loginController.getCurrentUser());
    	}
		return "users";
	}

    @RequestMapping(value="/rooms/{id}/messages", method = RequestMethod.GET)
	public String getRoomMessages(@PathVariable long id, Model model) {
    	ChatRoom room = chatService.getChatRoom(Long.toString(id));
    	if( room != null ){
    		List<Message> messages = new ArrayList<Message>(room.getMessages());
            Collections.reverse(messages);
            model.addAttribute("messages",messages);
    	}
		return "messages";
	}

    
    @RequestMapping(value="/logout", method = RequestMethod.GET)
	public ModelAndView logout(Model model) {
    	User user = loginController.getCurrentUser(); 
    	List<ChatRoom> rooms = new ArrayList<ChatRoom>();
    	for( UserChatSession session : user.getChatSessions() ){
    		rooms.add(session.getRoom());
    	}
    	chatService.deleteUser(user);
    	for( ChatRoom room : rooms){
    		pushRequestManager.getPushRequestContext().getPushContext().push(room.getName()+"_users");
    	}
		loginController.logout();
		return new ModelAndView(new RedirectView("./login"));
	}

    @RequestMapping(value={"/img/*", "/css/*", "/javascript/*"}, method = RequestMethod.GET)
    public void resource(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String filePath = requestURI.substring(contextPath.length());
        InputStream fileStream = session.getServletContext().getResourceAsStream(filePath);

        byte[] buf = new byte[1024];
        OutputStream responseStream = response.getOutputStream();

        int l = 1;
        while (l > 0) {
            l = fileStream.read(buf);
            if (l > 0) {
                responseStream.write(buf, 0, l);
            }
        }
    }



 }
