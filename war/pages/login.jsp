<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>


<!DOCTYPE html>


<%@page import="java.util.ArrayList"%>

<html>
  <head>
    <title>STLBet</title>
    <link rel="stylesheet" type="text/css" href="css/main.css"/>
      <meta charset="utf-8"> 
  </head>
  <body>
<%


UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();

String url = userService.createLoginURL(request.getRequestURI());
String urlLinktext = "Login";

            
if (user != null){
    url = userService.createLogoutURL(request.getRequestURI());
    urlLinktext = "Logout";
}
    
%>
<div style="width: 100%;">
    <div class="line"></div>
    <div class="topLine">
      <div style="float: left;"><img src="images/logo.jpg" /></div>
      <div style="float: right;"><a href="<%=url%>"><%=urlLinktext%></a> <%=(user==null? "" : user.getNickname())%></div>
    </div>
  </div>

<div style="clear: both;"/>  

<hr />
<div class="main">

<%String thisURL = request.getRequestURI(); %>

<% if (request.getUserPrincipal() != null) { %>
      <p>Hello !  You can <a href="<%=url%>"><%=urlLinktext%></a> <%=(user==null? "" : user.getNickname())%></p>
<% }else{ %>
     <p>Please login with your Google account : <a href="<%=url%>"><%=urlLinktext%></a> <%=(user==null? "" : user.getNickname())%></p>   
<% } %>

</div>
</body>
</html> 