<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="upmc.stl.aar.model.ProductBet" %>
<%@ page import="upmc.stl.aar.model.Player" %>
<%@ page import="upmc.stl.aar.dao.Dao" %>

<!DOCTYPE html>


<%@page import="java.util.ArrayList"%>

<html>
  <head>
    <title>STL Bet</title>
    <link rel="stylesheet" type="text/css" href="css/main.css"/>
    <meta charset="utf-8"> 
    <script src="//code.jquery.com/jquery-1.10.2.js"></script>
    <script>
     $.ajax({
    url: '/getCurrecies',
    dataType: 'json',
    success: function(myJSON) {
       
        var currencies = $('#currencies');
        currencies.html('');
        currencies.append(
        		'<div class="headline">Today Rates</div>'+
        		'Base :'+myJSON.base + '<br>'+
        		'Timestamp :'+new Date(myJSON.timestamp) + '<br>'+
        		'<table border="1">'+
        		'<tr><td>EUR</td><td>'+myJSON.rates.EUR+'</td></tr>'+
        		'<tr><td>GBP</td><td>'+myJSON.rates.GBP+'</td></tr>'+
        		'<tr><td>AUD</td><td>'+myJSON.rates.AUD+'</td></tr>'+
        		'<tr><td>CAD</td><td>'+myJSON.rates.CAD+'</td></tr>'+
        		'</table>'
        );
         
      }
    });
    </script>
  </head>
<body>
  
<%
Dao dao = Dao.INSTANCE;

UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();

String url = userService.createLoginURL(request.getRequestURI());
String urlLinktext = "Login";
List<ProductBet> bets = new ArrayList<ProductBet>();
List<ProductBet> historybets = new ArrayList<ProductBet>();
Player player = null;

if (user != null){
    url = userService.createLogoutURL(request.getRequestURI());
    urlLinktext = "Logout";
    bets = dao.getBets(user.getUserId());
    historybets = dao.getHistoryBets(user.getUserId());
    player = dao.getPlayer(user.getUserId());
}
    
%>

<% if (user != null){ %> 


<table>

<tr>
<td colspan="4">
  <div style="width: 100%;">
    <div class="line"></div>
    <div class="topLine">
      <div style="float: left;"><img src="images/logo.jpg" /></div>
      <div style="float: right;">Welcome <%=(user==null? "" : user.getNickname())%> <a href="<%=url%>"><%=urlLinktext%></a></div>
    </div>
  </div>
 </td>
</tr>

<tr><div class="line"></div></tr>
<tr><div class="line"></div></tr>
<tr><div class="line"></div></tr>
<tr><div class="line"></div></tr>

<tr>

<td>

<div id="display" style="width: 100%;">

<div class="headline">Balance</div>
<div style="float: left;"><%=player.getBalance() %></div>
<div class="line"></div>
<br>

<div class="headline">My Bets</div>
<table>
  <tr>
      <th>Type </th>
      <th>Quantity</th>
      <th>Currency</th>
      <th>Rate</th>
      <th>Date</th>
      <th>Term</th>
      <th>Status</th>
    </tr>

<% for (ProductBet bet : bets) {%>
<tr> 
<td>
<%=bet.getType()%>
</td>
<td>
<%=bet.getQuantity()%>
</td>
<td>
<%=bet.getCurrency()%>
</td>
<td>
<%=bet.getRates()%>
</td>
<td>
<%=bet.getBetDate()%>
</td>
<td>
<%=bet.getTerm()%>
</td>
<td>
<%=bet.getStatus()%>
</td>
</tr> 
<%}
%>
</table>

<div class="headline">History</div>
<table>
  <tr>
      <th>Type </th>
      <th>Quantity</th>
      <th>Currency</th>
      <th>Rate</th>
      <th>Date</th>
      <th>Term</th>
      <th>Status</th>
    </tr>

<% for (ProductBet bet : historybets) {%>
<tr> 
<td>
<%=bet.getType()%>
</td>
<td>
<%=bet.getQuantity()%>
</td>
<td>
<%=bet.getCurrency()%>
</td>
<td>
<%=bet.getRates()%>
</td>
<td>
<%=bet.getBetDate()%>
</td>
<td>
<%=bet.getTerm()%>
</td>
<td>
<%=bet.getStatus()%>
</td>
</tr> 
<%}
%>
</table>
</div>

</td>

<td><div class="line"></div></td>

<td><div class="line"></div></td>

<td>

<div id="main" style="width: 100%;">


<div id="currencies"></div>

<div class="headline">Play</div>
<form action="/new" method="post" accept-charset="utf-8">
  <table>
  	<tr>
      <th>Type </th>
      <th>Quantity</th>
      <th>Currency</th>
      <th>Rate</th>
      <th>Term</th>
    </tr>
    <tr>
      <td><select name="type" id="type" size="1">
              <option>Put</option>
              <option>Call</option>
          </select>
      </td>
      <td><input type="text" name="quantity" id="quantity"/></td>
      <td><select name="currency" id="currency" size="1">
              <option>USD</option>
              <option>EUR</option>
              <option>GBP</option>
              <option>CAD</option>
              <option>AUD</option>
          </select>
      </td>
      <td><input type="text" name="rate" id="rate"/></td>
      <td><select name="term" size="1">
              <option>1 h</option>
              <option>3 h</option>
              <option>6 h</option>
              <option>12 h</option>
              <option>1 day</option>
              <option>2 day</option>
              <option>3 day</option>
          </select>
      </td>
    </tr>
    <tr>
      <td align="left"><input type="submit" value="Submit"/></td>
    </tr>
  </table>
</form>

</td>
</tr>
</table>
<% }else{ %>


  <div style="width: 100%;">
    <div class="line"></div>
    <div class="topLine">
      <div style="float: left;"><img src="images/logo.jpg" /></div>
      <div style="float: right;">Please login with your Google account  <%=(user==null? "" : user.getNickname())%> <a href="<%=url%>"><%=urlLinktext%></a></div>
    </div>
  </div>
<% } %>

</body>
</html> 