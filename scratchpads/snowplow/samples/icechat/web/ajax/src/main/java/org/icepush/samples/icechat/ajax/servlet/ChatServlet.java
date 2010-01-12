package org.icepush.samples.icechat.ajax.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icepush.PushContext;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.service.exception.UnauthorizedException;

public class ChatServlet extends HttpServlet {

	public static final String GET_CHAT_ROOMS = "/chatrooms";
	public static final String GET_CHAT_ROOM_USERS = "/chatroomusers";
	public static final String GET_MESSAGES = "/messages";

	public static final String POST_CREATE_CHAT_ROOM = "/createroom";
	public static final String POST_CREATE_MESSAGE = "/createmessage";
	public static final String POST_UPDATE_DRAFT = "/updatedraft";
	public static final String POST_LOGIN_TO_ROOM = "/logintoroom";

	public static final String ROOM_NAME = "roomName";
	public static final String USER_NAME = "userName";
	public static final String PASSWORD = "password";
	public static final String MESSAGE = "msg";

	private static Logger LOG = Logger.getLogger(ChatServlet.class.getName());
	
	private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
	
	private void setCacheHeaders(HttpServletResponse resp){
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
        resp.setHeader("Pragma", "no-cache");//HTTP 1.0
        resp.setHeader("Expires", "0");//prevents proxy caching
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/html");
		setCacheHeaders(resp);

		String url = req.getServletPath();

		if (GET_CHAT_ROOMS.equals(url)) {
			resp.getWriter().print(getChatRooms());
		} else if (GET_CHAT_ROOM_USERS.equals(url)) {
			resp.getWriter().print(
					getChatRoomUsers(getCurrentUser(req), req
							.getParameter(ROOM_NAME)));
		}else if (GET_MESSAGES.equals(url)) {
			resp.getWriter().print(
					getChatRoomMessages(req
							.getParameter(ROOM_NAME)));
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/html");
		setCacheHeaders(resp);
		
		String url = req.getServletPath();

		if (POST_CREATE_CHAT_ROOM.equals(url)) {
			createNewChatRoom(req, resp);
		} else if (POST_CREATE_MESSAGE.equals(url)) {
			createNewChatMessage(req, resp);
		} else if( POST_UPDATE_DRAFT.equals(url)){
			updateMessageDraft(req,resp);
		}else if( POST_LOGIN_TO_ROOM.equals(url)){
			loginToRoom(req,resp);
		}
	}

	public IChatService getChatService() {
		return (SynchronizedChatServiceBean) this.getServletContext()
				.getAttribute("chatService");
	}

	private String getChatRooms() {
		List<ChatRoom> rooms = getChatService().getChatRooms();
		LOG.info("found " + rooms.size() + " chat rooms");
		StringBuffer buff = new StringBuffer();
		if (rooms != null && rooms.size() > 0) {
			for (ChatRoom room : rooms) {
				buff.append(room.toString());
			}
		} else {
			buff.append("<div>No rooms currently available</div>");
		}
		return buff.toString();
	}

	private String getChatRoomUsers(User user, String roomName) {
		StringBuffer buff = new StringBuffer();
		if (user != null) {
			ChatRoom room = getChatService().getChatRoom(roomName);
			if( room != null ){
				Collection<UserChatSession> sessions = room.getUserChatSessions();
				LOG.info("found " + sessions.size() + " users in chat room " + roomName);
				for( UserChatSession s : sessions ){
					buff.append("<div>");
					buff.append(s.getUser().getDisplayName());
					buff.append("&nbsp;");
					if( s.getCurrentDraft() != null ){
						buff.append("<span style='color:green;'>");
						buff.append(s.getCurrentDraft());
						buff.append("</span>");						
					}
					buff.append("</div>");
				}
			}
		}
		return buff.toString();
	}

	private String getChatRoomMessages(String roomName) {
		StringBuffer buff = new StringBuffer();
		List<Message> messages = getChatService().getAllChatRoomMessages(roomName);
		LOG.info("found "
				+ (messages != null ? messages.size() + " messages" : " no messages")
				+ " in chat room " + roomName);
		if (messages != null) {
			for (Message msg : messages) {
				buff.append("<div>");
				buff.append(DATE_FORMAT.format(msg.getCreated()));
				buff.append("&nbsp;");
				buff.append(msg.getUserChatSession().getUser().getDisplayName());
				buff.append("&nbsp;Said:&nbsp;<span style='color:gray;'>");
				buff.append(msg.getMessage());
				buff.append("</span></div>");
			}
		}
		return buff.toString();
	}

	private void createNewChatRoom(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession(true).getAttribute("user");
		if (user != null) {
			String roomName = req.getParameter(ROOM_NAME);
			getChatService().createNewChatRoom(
					roomName, user.getUserName(), user.getPassword());
			LOG.info("successfully created room " + roomName );
			
			PushContext pushContext = PushContext.getInstance(req.getSession().getServletContext());
			pushContext.push("icechat");
		} else {
			LOG.warning("/createroom: user is null, ignoring");
		}
	}

	private void createNewChatMessage(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String roomName = req.getParameter(ROOM_NAME);
		User user = (User) req.getSession(true).getAttribute("user");
		if (user != null) {
			try{
				String msg = req.getParameter(MESSAGE);
				getChatService().sendNewMessage(roomName, user.getUserName(), user.getPassword(), msg);
				LOG.info("sent room '" + roomName + "' message '" + msg + "'");
				
				PushContext pushContext = PushContext.getInstance(req.getSession().getServletContext());
				pushContext.push(roomName);
			}
			catch(UnauthorizedException e){
				e.printStackTrace();
			}
		}
	}

	private void updateMessageDraft(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String roomName = req.getParameter(ROOM_NAME);
		User user = (User) req.getSession(true).getAttribute("user");
		if (user != null) {
			try{
				String msg = req.getParameter(MESSAGE);
				getChatService().updateCurrentDraft(msg, roomName, user.getUserName(), user.getPassword());
				LOG.info("updated draft '" + msg + "'");
			}
			catch(UnauthorizedException e){
				e.printStackTrace();
			}
		}
	}
	
	private void loginToRoom(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String roomName = req.getParameter(ROOM_NAME);
		User user = (User) req.getSession(true).getAttribute("user");
		if (user != null) {
			getChatService().loginToChatRoom(roomName, user.getUserName(), user.getPassword());
		}
	}


	private User getCurrentUser(HttpServletRequest req) {
		return (User) req.getSession(true).getAttribute("user");
	}

}
