<%
    if ("POST".equals(request.getMethod())) {
        response.setContentType("text/xml");
%>
<partial-response>
    <error>
        <error-name>class org.icefaces.application.SessionExpiredException</error-name>
        <error-message>Session has expired</error-message>
    </error>
</partial-response>
<%} else {%>
<head>
    <title>Login</title>
</head>
<body>
<div>enter the user name and password of any user specified as being in 'manager-gui' role in tomcat-users.xml</div>
<form method="POST" action="j_security_check">
    <table>
        <tbody>
        <tr>
            <th style="text-align: right;">user:</th>
            <td><input type="text" name="j_username"></td>
        </tr>
        <tr>
            <th style="text-align: right;">password:</th>
            <td><input type="password" name="j_password"></td>
        </tr>
        <tr>
            <td></td>
            <td style="float: right;"><input type="submit" value="Login"/></td>
        </tr>
        </tbody>
    </table>
</form>
</body>
</html>
<%}%>
