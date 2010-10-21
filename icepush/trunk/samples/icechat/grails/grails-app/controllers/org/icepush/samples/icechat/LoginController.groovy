package org.icepush.samples.icechat

import org.icepush.PushContext
import org.icepush.samples.icechat.model.User
import org.icepush.samples.icechat.model.UserChatSession

class LoginController {

  def chatService  

  public static final OP = "op"
  public static final USER_NAME = "userName"
  public static final LOGIN = "login"
  public static final LOGOUT = "logout"
  public static final String USER_KEY = 'user'
  private static final HOME_PAGE_URL_KEY = "homePageURL"
  public static final RESOURCE_PARAM_KEY = "res"

  def index = { }

  def auth = {
    String op = params[OP]

    if (LOGOUT == op) {
      def user = (User)session[USER_KEY];
      if (user) {
        for (UserChatSession chatsession: user.chatSessions) {
          chatsession.room.userChatSessions.remove(chatsession);
          push "${chatsession.room.name}_users"
          chatsession = null;
        }
        request[USER_KEY] = null
        log.info "$user.name logged out"
      }
      session.invalidate();
      render ""
      return;
    }
    else if (LOGIN == op && params[USER_KEY]) {
      forward(controller:'chat')
      return;
    }
    else if (LOGIN == op) {

      String userName = params[USER_NAME]

      if (userName) {
        User user = null;

        if (LOGIN == op) {
          log.info "logging in $userName"
          user = chatService.login(userName);
          session[USER_KEY] = user
        }
        log.info "session: $session.id  user=$user"
        response.status = 200;
        render " "
        return
      }

    }
    response.status = 401
    response.flushBuffer()
    return
  }
}