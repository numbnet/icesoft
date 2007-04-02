<html>
<head>
    <title></title>
</head>
<body>
<% if(session.getAttribute("LoggedIn")=="true"){ %>
    <jsp:forward page="webmail.iface" />
<% }else{
    session.invalidate();%>
    <jsp:forward page="login.iface" />
<% } %>
</body>
</html>
