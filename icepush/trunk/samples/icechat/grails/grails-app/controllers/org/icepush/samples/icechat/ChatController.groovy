package org.icepush.samples.icechat

class ChatController {

  def index = {
    noCache(response)
  }

  def chatService

  private void noCache(response) {
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
    response.setHeader("Pragma", "no-cache");//HTTP 1.0
    response.setHeader("Expires", "0");//prevents proxy caching
  }

  def createNewChatRoom = {
    noCache(response)

    if (session["user"]) {
      chatService.createNewChatRoom(params.roomName);
      push "rooms"
    }
    render ""
  }

  def createNewMessage = {
    noCache(response)

    def roomName = session["currentChatRoom"].name
    def user = session["user"]
    if (user) {
      def msg = params["msg"]
      chatService.sendNewMessage(roomName, user, msg);
      push "${roomName}_messages"
      push "${roomName}_${user.name}_draft"
    }
    render ""
  }

  def loginToChatRoom = {
    noCache(response)

    def user = session["user"]
    if (user) {
      if (session["currentChatRoom"])
        chatService.logoutOfChatRoom(session["currentChatRoom"].name, user);
      def chatRoom = chatService.getChatRoom(params.roomName);
      if (chatRoom) {
        chatService.loginToChatRoom(chatRoom.name, user);
        session["currentChatRoom"] = chatRoom
        push "${chatRoom.name}_users"
      }
    }
    render ""
  }

  def logoutOfChatRoom = {
    noCache(response)

    def user = session["user"]
    if (user) {
      chatService.logoutOfChatRoom(params.roomName, user);
      push "${params.roomName}_users"
    }

    render ""
  }

  def messageDraft = {
    noCache(response)
    [username:params.user]
  }

  def updateDraft = {
    noCache(response)

    def user = session["user"]
    if (user && session["currentChatRoom"]) {
      def room = session["currentChatRoom"]

      chatService.updateCurrentDraft(params.msg, room.name, user);
      push "${room.name}_${user.name}_draft"
    }

    render ""
  }

  def chatRoomUsers = {
    noCache(response)
  }

  def chatRooms = {
    noCache(response)
    [chatRooms: chatService.chatRooms]
  }

  def chatRoomMessages = {
    noCache(response)
    def messages = chatService.getAllChatRoomMessages(session["currentChatRoom"].name)
    def sortedM = new ArrayList(messages)
    Collections.reverse(sortedM);
    [sortedMessages: sortedM]
  }

}
