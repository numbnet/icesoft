package org.icepush.samples.icechat

import grails.test.ControllerUnitTestCase
import org.icepush.samples.icechat.model.User
import org.icepush.samples.jsp.beans.ChatService

class LoginControllerTests extends ControllerUnitTestCase {

  void testAuth() {
    mockParams.op = "login"
    mockParams.userName = "test"

    controller.chatService = new ChatService()
    controller.chatService.metaClass.login = {username ->
      new User()
    }
    

    controller.auth()
    println mockSession['user']
  }
}