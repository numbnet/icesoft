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
	public static final String GET_MESSAGE_DRAFT = "/messagedraft";

	public static final String POST_CREATE_CHAT_ROOM = "/createroom";
	public static final String POST_CREATE_MESSAGE = "/createmessage";
	public static final String POST_UPDATE_DRAFT = "/updatedraft";
	public static final String POST_LOGIN_TO_ROOM = "/logintoroom";
	public static final String POST_LOGOUT_OF_ROOM = "/logoutofroom";

	public static final String ROOM_NAME = "roomName";
	public static final String USER_NAME = "userName";
	public static final String MESSAGE = "msg";
	public static final String INDEX = "idx";

	private static Logger LOG = Logger.getLogger(ChatServlet.class.getName());

	private static DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy.MM.dd HH:mm:ss z");

	private void setCacheHeaders(HttpServletResponse resp) {
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");// HTTP
																				// 1.1
		resp.setHeader("Pragma", "no-cache");// HTTP 1.0
		resp.setHeader("Expires", "0");// prevents proxy caching
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
			resp.getWriter()
					.print(
							getChatRoomUsers(getCurrentUser(req),
									getRoomNameParam(req)));
		} else if (GET_MESSAGES.equals(url)) {
			resp.getWriter().print(
					getChatRoomMessages(getRoomNameParam(req), req
							.getParameter(INDEX)));
		} else if (GET_MESSAGE_DRAFT.equals(url)) {
			resp.getWriter().print(
					getMessageDraft(getRoomNameParam(req), req
							.getParameter(USER_NAME)));
		}
	}

	private String getRoomNameParam(HttpServletRequest req) {
		return req.getParameter(ROOM_NAME);
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
		} else if (POST_UPDATE_DRAFT.equals(url)) {
			updateMessageDraft(req, resp);
		} else if (POST_LOGIN_TO_ROOM.equals(url)) {
			loginToRoom(req, resp);
		} else if (POST_LOGOUT_OF_ROOM.equals(url)) {
			logoutOfRoom(req, resp);
		}
		resp.setStatus(200);
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
			if (room != null) {
				Collection<UserChatSession> sessions = room
						.getUserChatSessions();
				LOG.info("found " + sessions.size() + " users in chat room "
						+ roomName);
				for (UserChatSession s : sessions) {
					buff.append("<div id='");
					buff.append(s.getUser().getName());
					buff.append("'>");
					buff.append(s.getUser().getName());
					buff.append("&nbsp;<span id='");
					buff.append(s.getRoom().getName() + "_"
							+ s.getUser().getName());
					buff.append("_draft' class='draft'>");
					buff.append(getMessageDraft(s));
					buff.append("</span></div>");
				}
			}
		}
		return buff.toString();
	}

	private String getMessageDraft(UserChatSession s) {
		StringBuffer buff = new StringBuffer();
		if (s.getCurrentDraft() != null) {
			int draftLength = s.getCurrentDraft().length();
			buff.append(draftLength > 20 ? "..."
					+ s.getCurrentDraft().substring(draftLength - 20) : s
					.getCurrentDraft());
			buff.append("...");
		}
		return buff.toString();
	}

	private String getChatRoomMessages(String roomName, String index) {
		StringBuffer buff = new StringBuffer();
		List<Message> messages = null;
		if (index != null) {
			try {
				Integer iIndex = Integer.valueOf(index);
				messages = getChatService().getChatRoomMessagesFromIndex(
						roomName, iIndex);
			} catch (NumberFormatException e) {
				messages = getChatService().getAllChatRoomMessages(roomName);
			}
		} else {
			messages = getChatService().getAllChatRoomMessages(roomName);
		}
		LOG.info("found "
				+ (messages != null ? messages.size() + " messages"
						: " no messages") + " in chat room " + roomName);
		if (messages != null) {
			for (Message msg : messages) {
				buff.append("<div id='");
				buff.append(msg.getId());
				buff.append("'>");
				buff.append(DATE_FORMAT.format(msg.getCreated()));
				buff.append("&nbsp;");
				buff.append(msg.getUserChatSession().getUser().getName());
				buff.append("&nbsp;Said:&nbsp;<span style='color:gray;'>");
				buff.append(msg.getMessage());
				buff.append("</span></div>");
			}
		}
		return buff.toString();
	}

	private String getMessageDraft(String roomName, String userName) {
		String result = "";
		ChatRoom room = getChatService().getChatRoom(roomName);
		if (room != null) {
			Collection<UserChatSession> sessions = room.getUserChatSessions();
			for (UserChatSession s : sessions) {
				if (s.getUser().getName().equals(userName)
						&& s.getCurrentDraft() != null) {
					result = getMessageDraft(s);
					if (s.getCurrentDraft() != null)
						result += "<div class='typing'>&nbsp;&nbsp;&nbsp;</div>";
					break;
				}
			}
		}
		return result;
	}

	private void createNewChatRoom(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession(true).getAttribute("user");
		if (user != null) {
			String roomName = req.getParameter(ROOM_NAME);
			getChatService().createNewChatRoom(roomName);
			LOG.info("successfully created room " + roomName);

			PushContext pushContext = PushContext.getInstance(req.getSession()
					.getServletContext());
			pushContext.push("rooms");
		} else {
			LOG.warning("/createroom: user is null, ignoring");
		}
	}

	private void createNewChatMessage(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String roomName = req.getParameter(ROOM_NAME);
		User user = (User) req.getSession(true).getAttribute("user");
		if (user != null) {
			String msg = req.getParameter(MESSAGE);
			getChatService().sendNewMessage(roomName, user, msg);
			LOG.info("sent room '" + roomName + "' message '" + msg + "'");

			PushContext pushContext = PushContext.getInstance(req.getSession()
					.getServletContext());
			pushContext.push(roomName + "_messages");
			pushContext.push(roomName + "_" + user.getName() + "_draft");
		}
	}

	private void updateMessageDraft(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String roomName = req.getParameter(ROOM_NAME);
		User user = (User) req.getSession(true).getAttribute("user");
		if (user != null) {
			String msg = req.getParameter(MESSAGE);
			getChatService().updateCurrentDraft(msg, roomName, user);
			PushContext pushContext = PushContext.getInstance(req.getSession()
					.getServletContext());
			pushContext.push(roomName + "_" + user.getName() + "_draft");

		}
	}

	private void loginToRoom(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String roomName = req.getParameter(ROOM_NAME);
		User user = (User) req.getSession(true).getAttribute("user");
		if (user != null
				&& !getChatService().getChatRoom(roomName).isUserInRoom(user)) {
			getChatService().loginToChatRoom(roomName, user);
			PushContext pushContext = PushContext.getInstance(req.getSession()
					.getServletContext());
			pushContext.push(roomName + "_users");
		}
	}

	private User getCurrentUser(HttpServletRequest req) {
		return (User) req.getSession(true).getAttribute("user");
	}

	private void logoutOfRoom(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String roomName = req.getParameter(ROOM_NAME);
		User user = (User) req.getSession(true).getAttribute("user");
		if (user != null) {
			LOG.info("removing " + user.getName() + " from " + roomName);
			getChatService().logoutOfChatRoom(roomName, user);
			PushContext pushContext = PushContext.getInstance(req.getSession()
					.getServletContext());
			pushContext.push(roomName + "_users");
		}
	}
}
