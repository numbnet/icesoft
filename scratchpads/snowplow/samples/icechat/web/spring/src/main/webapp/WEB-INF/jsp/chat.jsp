<%@ include file="/WEB-INF/jsp/header.jsp" %>

<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>ICEpush ICEchat (Spring MVC)</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="./css/style-common.css"/>
    </head>
    <body>

        <div class="header_container">
            <div class="header_content">
                <img width="109px" height="100px"
                     src="./img/banner_hdr.jpg"
                     style="float: left;"/>
                <div style="font-size: 20pt; color: #0F65B8; margin-top: 45px;">
                    <span style="font-weight: bold;">ICEpush</span>
                    <span style="font-style: italic; font-family: 'Times New Roman', 'Arial', 'serif';"> with Spring MVC</span>
                </div>
            </div>
        </div>

        <c:if test="${!empty chat.loginController.currentUser}">
            <div class="body_container body_container_pos">

                <div class="chatSession chatSession_pos">
                    <c:out value="You are logged in as ${chat.loginController.currentUser.userName} (${chat.loginController.currentUser.nickName})"/>
                    <a href="logout.htm">Logout</a>
                </div>

                <div class="clearer">&nbsp;</div>
            </div>

            <div class="chatRooms chatRooms_pos">
                <h3>Chat Rooms</h3>

                <form:form method="post" commandName="chat">
                <table width="100%" cellspacing="2" cellpadding="2">
                <c:forEach var="room" items="${chat.chatManagerFacade.chatRooms}">
                    <tr>
                        <td>
                            <input type="hidden" name="submit.joinRoom.name" value="${room.name}"/>
                            <input type="submit" name="submit.joinRoom" value="${room.name}"
                                   style="width: 100%;"/>
                        </td>
                    </tr>
                </c:forEach>
                </table>
                </form:form>

                <form:form method="post" commandName="chat">
                    <!--<div class="createNewChatRoom">-->
                        <fmt:message key="roomTitle"/>
                        <br/>
                        <fmt:message key="roomName"/>

                        <form:input path="newChatRoom.name" maxlength="50"
                                    style="width: 100px; margin-top: 5px;"/>

                        <input type="submit" name="submit.newRoom" value="Create"
                               style="margin-top: 5px;"/>
                    <!--</div>-->
                </form:form>
            </div>

            <c:if test="${!empty chat.currentChatSessionHolder.session}">
                <div class="chatRoomView">
                    <div class="chatRoomHeader">
                        <c:out value="Chat Room '${chat.currentChatSessionHolder.session.room.name}'"/>
                    </div>

                    <div class="chatRoomContainer">
                        <div class="chatRoomUsers">
                            <div class="chatViewSubHeader">Who's Here?</div>
                            <table>
                            <c:forEach var="chatSession" items="${chat.currentChatSessionHolder.session.room.userChatSessions}">
                                <tr><td>
                                <c:out value="${chatSession.user.displayName}"/>
                                </td></tr>
                            </c:forEach>
                            </table>
                        </div>

                        <div class="chatRoomMessages">
                            <div class="chatViewSubHeader">Messages</div>
                            <table>
                            <c:forEach var="msg" items="${chat.currentChatSessionHolder.session.room.messages}">
                                <tr><td>
                                    [<fmt:formatDate value="${msg.created}" type="both" dateStyle="short" timeStyle="short"/>]
                                    <b><c:out value="${msg.userChatSession.user.displayName}: "/></b><c:out value="${msg.message}"/>
                                </td></tr>
                            </c:forEach>
                            </table>
                        </div>
                    </div>

                    <div class="clearer">&nbsp;</div>

                    <div class="addNewMessage">
                        <form:form method="post" commandName="chat">
                            <fmt:message key="newMessage"/>
                            
                            <form:input path="newMessage.message" maxlength="1024"
                                        style="width: 40%;"/>
                            
                            <input type="submit" name="submit.sendMessage" value="Send"/>
                        </form:form>
                    </div>
                </div>
            </c:if>
        </c:if>

        <c:if test="${empty chat.loginController.currentUser}">
            <div class="body_container">
                Please <a href="login.htm">login</a> first before attempting to chat...
            </div>
        </c:if>
    </body>
</html>
