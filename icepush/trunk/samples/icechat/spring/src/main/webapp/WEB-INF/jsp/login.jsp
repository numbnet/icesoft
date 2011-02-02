<%@ include file="/WEB-INF/jsp/header.jsp" %>

<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>ICEpush ICEchat (Spring MVC)</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=100" /> <!-- IE8 mode -->
        <link rel="stylesheet" type="text/css" href="./css/style-common.css"/>
    </head>
    <body>

        <div class="header_container">
            <div class="header_content">
				<div style="font-size: 20pt; color: #0F65B8; margin-top: 15px;">
					<img src="./img/ICEpush-Spring.png" title="ICEpush with Spring MVC"/>
					<img src="./img/Spring.png" style="float:right;margin-top:4px;margin-right:17px;" width="140px"/>
				</div>
			</div> 		
        </div>

        <div class="body_container">
            <form:form method="post" commandName="login" modelAttribute="loginFormData" action="login">
            <div class="login">
                <h2>ICEchat Login</h2>
                <div style="text-align: right;">
                    <fmt:message key="userName"/>
                    <form:input path="userName" maxlength="50"/>
                    <div id="loginMessages">
                        <form:errors path="userName"/>
                    </div>
                </div>                
                <div style="text-align:right;margin-top:20px;">
                    <input type="submit" value="Login"/>
                </div>
                </div>
            </form:form>
            </div>            
        </div>
    </body>
</html>
