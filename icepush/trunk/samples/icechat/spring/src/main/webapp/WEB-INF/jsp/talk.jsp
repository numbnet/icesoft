<%@ include file="/WEB-INF/jsp/header.jsp" %>
<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>

<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>ICEpush ICEchat (Spring MVC)</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=100" /> <!-- IE8 mode -->
        <link rel="stylesheet" type="text/css" href="./css/style-common.css"/>
        <script type="text/javascript" src="code.icepush"></script>
        <script type="text/javascript" src="./javascript/jquery-1.4.1.js"></script>
        <script type="text/javascript">
        var expectedHash = '';
        
        function joinRoom(id){
        	makeHistory('rooms/'+id);
        }
        function createRoom(){
			$.post("./newroom/", {name: $("#chatRoomName").val()},
					function (data){
						makeHistory('rooms/'+data);
		        		$("#chatRoomName").val('');	
		     });	
	  	}
        function sendMessage(){
			$.post("./rooms/"+$("#roomId").val()+"/newmessage/", {message: $("#message").val()});
			$("#message").val('');
        }
        function updateDraft(){
            $.post("./rooms/"+$("#roomId").val()+"/updatedraft/",{message: $("#message").val()});
        }
        function makeHistory(newHash){
	          window.location.hash = newHash;
	          return true;
        }
                
        function pollHash() {
	       	  handleHistory();
	       	  window.setInterval("handleHistory()", 500);
	       	  return true;
       	}
        function handleHistory(){
	          if ( window.location.hash != expectedHash ){
	            expectedHash = window.location.hash;
	            $.get( expectedHash.length > 0 ? expectedHash.substring(1) : '', 
	        			function (data){     
	            			$("#currentChatRoom").slideUp(1000);	   					
			        		$("#chatRoomContainer").html( data );
			        		$("#currentChatRoom").ready( function(){
			        			$("#currentChatRoom").hide().fadeIn(1000).slideDown(1000);
			        		});
			        		
	        	});
	          }
	          return true;
        }
        pollHash();
                	        
        </script>
    </head>
    <body>
    	<form action="rooms">
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
                <p>Please <a href="/chat/login">login</a> first before attempting to chat...</p>
            </div>
        </c:if>      
        
        <c:if test="${!empty loginController.currentUser}">
	        <div class="body_container body_container_pos" id="content"> 
	           <div id="chatPanel">
				
					<div class="chatSession chatSession_pos" id="chatSessionPanel">
						<c:out value="You are logged in as ${loginController.currentUser.name}"/>
						<a href="./logout">Logout</a>
					</div>
					
					<div class="clearer"></div>
				
					<div class="mainPanel_pos">
					
						<div id="chatRoomsPanel" class="chatRooms chatRooms_pos">
							<h3>Chat Rooms</h3>
							<div id="chatRooms">
								<icep:region group="CHAT_ROOMS" page="/roomlist"/>
							</div>
					         <table width="99%" cellspacing="0" cellpadding="0">
		                        <tr>
		                            <td width="100%" colspan="2" align="center">
		                                <fmt:message key="roomTitle"/>
		                            </td>
		                        </tr>
		                        <tr>
		                            <td width="70%">
		                                <input id="chatRoomName" maxlength="50"
		                                            style="width: 100%;" onkeypress="if(event.keyCode == 13){ createRoom(); return false;}"/>
		                            </td>
		                            <td width="30%">
		                                <button onclick="createRoom();return false;" title="Create">Create</button>
		                            </td>
		                        </tr>
		                    </table>
			   		</div>
					
						<div id="chatRoomContainer">							
						</div>			
					
					</div>
				
				</div>
	        </div>
    	</c:if>
		</form>
    </body>
</html>
