<%@ include file="/WEB-INF/jsp/header.jsp" %>

<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>ICEpush ICEchat (Spring MVC)</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="./css/style-common.css"/>
        <script type="text/javascript" src="code.icepush"></script>
    </head>
    <body>
    	<div id="headerContainer" class="header_container">
            <div class="header_content">
				<div style="font-size: 20pt; color: #0F65B8; margin-top: 15px;">
					<img src="./img/ICEpush-Spring.png" title="ICEpush with Spring MVC"/>
					<img src="./img/Spring.png" style="float:right;margin-top:4px;margin-right:17px;" width="140px"/>
				</div>
			</div> 		
    	</div>
    	
    	<c:if test="${empty loginController.currentUser}">
            <div class="body_container">
                <p>Please <a href="login.htm">login</a> first before attempting to chat...</p>
            </div>
        </c:if>      
        
        <c:if test="${!empty loginController.currentUser}">
	        <div class="body_container body_container_pos" id="content"> 
	           <div id="chatPanel">
				
					<div class="chatSession chatSession_pos" id="chatSessionPanel">
						<c:out value="You are logged in as ${loginController.currentUser.userName} (${loginController.currentUser.nickName})"/>
						<a href="logout.htm">Logout</a>
					</div>
					
					<div class="clearer"></div>
				
					<div class="mainPanel_pos">
					
						<div id="chatRoomsPanel" class="chatRooms chatRooms_pos">
							<h3>Chat Rooms</h3>
							<div id="chatRooms">
								<table width="99%" cellspacing="2" cellpadding="2">
					                <c:forEach var="room" items="${chatManagerFacade.chatRooms}">
					                    <tr>
					                        <td>
					                            <form:form method="post" commandName="chat">
					                                <input type="hidden" name="submit.joinRoom.name" value="${room.name}"/>
					                                <input type="submit" name="submit.joinRoom" value="${room.name}"
					                                       style="width: 100%;"/>
					                            </form:form>
					                        </td>
					                    </tr>
					                </c:forEach>
					            </table>
							</div>
							<form:form method="post" commandName="chat">
			                    <table width="99%" cellspacing="0" cellpadding="0">
			                        <tr>
			                            <td width="100%" colspan="2" align="center">
			                                <fmt:message key="roomTitle"/>
			                            </td>
			                        </tr>
			                        <tr>
			                            <td width="70%">
			                                <form:input path="newChatRoom.name" maxlength="50"
			                                            style="width: 100%;"/>
			                            </td>
			                            <td width="30%">
			                                <input type="submit" name="submit.newRoom" value="Create"/>
			                            </td>
			                        </tr>
			                    </table>
			                </form:form>
						</div>
					
						<c:if test="${!empty chat.currentChatSessionHolder.session}">
							<div id="currentChatRoom" class="currentChatRoom_pos">
								<div class="chatRoom" id="chatRoom">
									<form id="chatRoomForm">
										<div class="chatRoomHeader">
											<c:out value="Chat Room '${chat.currentChatSessionHolder.session.room.name}'"/>
										</div>
										<div class="chatRoomContainer">
											<div class="chatRoomUsers">
												<icep:region group="${chat.currentChatSessionHolder.session.room.name}" page="/users.htm"/>
											</div>
											<div class="chatRoomMessages">
												<div id="chatRoomMessages">
													<icep:region group="${chat.currentChatSessionHolder.session.room.name}" page="/messages.htm"/>
												</div>
											</div>
										</div>
										<div class="clearer"></div>
										<div class="addNewMessage">
											 <form:form method="post" commandName="chat">
					                            <fmt:message key="newMessage"/>
					                            
					                            <form:input id="messageInput" path="newMessage.message" maxlength="1024"
					                                        style="width: 60%;"/>
					                            
					                            <input type="submit" name="submit.sendMessage" value="Send"/>
					                        </form:form>
										</div>
									</form>
								</div>        
							</div>		
						</c:if>			
					
					</div>
				
				</div>
	        </div>
    	</c:if>

    </body>
</html>
