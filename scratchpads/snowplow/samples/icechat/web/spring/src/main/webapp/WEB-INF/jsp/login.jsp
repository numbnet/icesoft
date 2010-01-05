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

        <div class="body_container">
            <div class="login">
                <h2>ICEchat Login</h2>
                <div style="text-align: right;">
                    <fmt:message key="userName"/>
                    <c:out value="${user.userName}"/>
                </div>
                <div style="text-align:right;">
                    <fmt:message key="nickName"/>
                    <c:out value="${user.nickName}"/>
                </div>
                <div style="text-align:right;">
                    <fmt:message key="password"/>
                    <c:out value="${user.password}"/>
                </div>
                <div style="text-align:right;margin-top:20px;">
                    <fmt:message key="loginButton"/>
                    <fmt:message key="registerButton"/>
                </div>
            </div>            
        </div>
    </body>
</html>
