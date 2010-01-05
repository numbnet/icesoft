<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>ICEpush ICEchat (Spring MVC)</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel='stylesheet' type='text/css' href='./css/style-common.css'/>
        <script type="text/javascript" src="code.icepush"></script>
    </head>
    <body>

        <div class="header_container">
            <div class="header_content">
                <img width="109px" height="100px"
                     src="./img/banner_hdr.jpg"
                     style="float:left"/>
                <div style="font-size:20pt;color:#0F65B8;margin-top:45px;">
                    <span style="font-weight:bold;">ICEpush</span>
                    <span style="font-style:italic;font-family:Times New Roman"> with Spring MVC</span>
                </div>
            </div>
        </div>

        <div class="body_container">

            <!--
            <h:panelGroup  rendered="#{not empty loginController.currentUser}">

                <div class="chatSession">
                    <h:form id="chatSession">
                        You are logged in as #{loginController.currentUser.userName}
                        <h:outputText rendered="#{not empty loginController.currentUser.nickName}"
                            value="(#{loginController.currentUser.nickName})"/>&nbsp;
                        <h:commandButton value="Logout" actionListener="#{loginController.logout}"/>
                    </h:form>
                </div>

                <div class="clearer"/>

                <div class="chatRooms">
                    <h3>Chat Rooms</h3>
                    <h:form id="chatRooms">
                        <ui:repeat value="#{chatManagerFacadeBean.chatRooms}" var="room">
                            <div>
                                <h:commandLink value="#{room.name}"
                                    actionListener="#{chatManagerVC.openChatSession(room.name)}"/>
                            </div>
                        </ui:repeat>
                    </h:form>
                    <h:form id="createNewChatRoom">
                        <div class="createNewChatRoom">
                            Create New Chat Room
                            <br/>
                            <h:outputLabel for="newChatRoomName" value="Name"/>&nbsp;
                            <h:inputText id="newChatRoomName" value="#{newChatRoomBean.name}"
                                required="true" style="width: 100px;margin-top:5px;"/>
                            <h:commandButton value="Create" style="margin-top:5px;"
                                actionListener="#{chatManagerVC.createNewChatRoom}"/>
                        </div>
                    </h:form>
                </div>

                <h:panelGroup rendered="#{not empty currentChatSessionHolderBean.session}">
                    <div class="chatRoomView">
                        <h:form id="chatRoomView">
                            <div class="chatRoomHeader">Chat Room '#{currentChatSessionHolderBean.session.room.name}'</div>
                            <div class="chatRoomContainer">
                                <div class="chatRoomUsers">
                                    <div class="chatViewSubHeader">Who's Here?</div>
                                    <ui:repeat value="#{currentChatSessionHolderBean.session.room.userChatSessions}" var="chatSession">
                                        <div>#{chatSession.user.displayName}</div>
                                    </ui:repeat>
                                </div>
                                <div class="chatRoomMessages">
                                    <div class="chatViewSubHeader">Messages</div>
                                    <ui:repeat value="#{currentChatSessionHolderBean.session.room.messages}" var="msg">
                                        <div>
                                            <h:outputText value="#{msg.created}">
                                                <f:convertDateTime type="both" dateStyle="short" timeStyle="short"/>
                                            </h:outputText>
                                            &nbsp;#{msg.userChatSession.user.displayName} said
                                            &nbsp;'#{msg.message}'
                                        </div>
                                    </ui:repeat>
                                </div>
                            </div>
                            <div class="clearer"/>
                            <div class="addNewMessage">
                                New Message&nbsp;<h:inputText value="#{newChatRoomMessageBean.message}"
                                    style="width:40%;"/>
                                <h:commandButton value="Send" actionListener="#{chatManagerVC.sendNewMessage}"/>
                            </div>
                        </h:form>
                    </div>
                </h:panelGroup>

            </h:panelGroup>
            -->

            <!--
            <h:panelGroup rendered="#{empty loginController.currentUser}">
                <div class="login">
                    <h:form id="login">
                        <h2>ICEchat Login</h2>
                        <div style="text-align:right;">
                            <h:outputLabel value="User Name " for="userName"/>
                            <h:inputText id="userName" value="#{credentialsBean.userName}"
                                required="true"/>
                        </div>
                        <div style="text-align:right;">
                            <h:outputLabel value="Nick Name " for="nickName"/>
                            <h:inputText id="nickName" value="#{credentialsBean.nickName}"/>
                        </div>
                        <div style="text-align:right;">
                            <h:outputLabel value="Password " for="password"/>
                            <h:inputSecret id="password" value="#{credentialsBean.password}"
                                required="true"/>
                        </div>
                        <div style="text-align:right;margin-top:20px;">
                            <h:commandButton id="login" value="Login" actionListener="#{springLoginController.login}"/>
                            <h:commandButton id="register" value="Register" actionListener="#{loginController.register}"/>
                        </div>
                        <div id="loginMessages">
                            <h:messages globalOnly="true"/>
                        </div>

                    </h:form>
                </div>
            </h:panelGroup>
            -->

        </div>
    </body>
</html>
