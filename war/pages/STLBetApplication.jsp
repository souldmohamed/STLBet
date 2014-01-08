<%@page
	import="com.google.appengine.api.search.query.QueryParser.restriction_return"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>


<%@page import="java.util.ArrayList"%>

<html>
<head>
<title>STL Bet</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>

<script>
	$.ajax({
		url : '/getCurrencies',
		dataType : 'json',
		success : function(myJSON) {

			var base = $('#base');
			base.html("");
			base.append(myJSON.base);

			var lsup = $('#lastupdate');
			lsup.html("");
			lsup.append(new Date(myJSON.timestamp*1000));

			var eur = $('#EUR');
			eur.html("");
			eur.append(myJSON.rates.EUR);

			var gbp = $('#GBP');
			gbp.html("");
			gbp.append(myJSON.rates.GBP);

			var aud = $('#AUD');
			aud.html("");
			aud.append(myJSON.rates.AUD);

			var cad = $('#CAD');
			cad.html("");
			cad.append(myJSON.rates.CAD);
		}
	});
</script>
<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
%>
</head>
<body class="alert-success">
	<c:choose>
		<c:when test="<%=userService.isUserLoggedIn()%>">
			<!-- Header -->
			<div class="container">
				<br /> <img class="col-md-3 alert-success pull-left"
					src="images/logo.png" />
				<div class="well col-md-5 text-center">
					<h1>
						<b>U.G.L.Y Bet.ty</b>
					</h1>
					<small>Universally Gloomy Looking Yeti Bet Thanks to You</small>
					<h1>

						Put. Call. Win. <small>Simple. Effective.</small>
					</h1>
				</div>
			</div>
			<!-- End header -->

			<!-- Navigation bar -->
			<div class="container">
				<div class="navbar alert-info" role="navigation">
					<div class="collapse navbar-collapse" id="Div1">
						<ul class="nav navbar-nav navbar-right">
							<li class="navbar-text">Welcome, <%=user.getNickname()%>
							</li>
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown"> <span
									class="glyphicon glyphicon-cog"></span>
							</a>
								<ul class="dropdown-menu">
									<li><a href="#">My profile</a></li>
									<li class="divider"></li>
									<li><a href="${ Logout }"> Logout </a></li>
								</ul></li>
						</ul>
						
						<c:choose>
							<c:when test="${ Player.isEligible }">
								<form class="form-inline" method="post" action="DailyGain">
									<input type="submit"/>
								</form>
							</c:when>
						</c:choose>

						<ul class="nav navbar-nav navbar-right">
							<li class="navbar-text">Current balance : ${ Player.balance }
							</li>

						</ul>

						<ul class="nav navbar-nav navbar-left alert-primary">
							<li><a href="#bhome" data-toggle="tab">Home</a></li>
							<li><a href="#bcurrent" data-toggle="tab">My bets</a></li>
							<li><a href="#bhistory" data-toggle="tab">History</a></li>
						</ul>
					</div>
				</div>
			</div>
			<!-- Navigation bar end-->


			<!-- Body -->
			<div class="container">



				<!-- Tab panes -->
				<div class="tab-content">
					<!-- Home tab -->
					<div class="tab-pane active" id="bhome">

						<!-- Currencies info -->
						<div class="container col-md-3">

							<!-- Base section -->
							<div class="panel panel-info">
								<div class="panel-heading">Base</div>
								<div class="panel-body" id="base"></div>
							</div>

							<!-- Last updated section -->
							<div class="panel panel-info">
								<div class="panel-heading alert-info">Last updated</div>
								<div class="panel-body" id="lastupdate"></div>
							</div>

							<!-- Current rates section -->
							<div class="panel panel-info">
								<div class="panel-heading">Current rates</div>
								<div class="panel-body">
									<table class="table table-bordered">
										<tr>
											<td class="col-md-4">EUR</td>
											<td id="EUR"></td>
										</tr>
										<tr>
											<td>GBP</td>
											<td id="GBP"></td>
										</tr>
										<tr>
											<td>AUD</td>
											<td id="AUD"></td>
										</tr>
										<tr>
											<td>CAD</td>
											<td id="CAD"></td>
										</tr>
									</table>
								</div>
							</div>
						</div>

						<!-- Right panel -->
						<div class="container col-md-9">
							<div class="panel panel-info">
								<div class="panel-heading">Actions</div>
								<div class="panel-body">
									<form class="form-horizontal" action="/new" method="post"
										accept-charset="utf-8">

										<table class="table table-bordered">
											<tr class="alert-info text-center">
												<td class="col-md-2">Type</td>
												<td class="col-md-2">Quantity</td>
												<td class="col-md-2">Currency</td>
												<td class="col-md-2">Rate</td>
												<td class="col-md-2">Term</td>
												<td class="col-md-2"></td>
											</tr>
											<tr>
												<td><select class="form-control" name="type" id="type"
													size="1">
														<option>Put</option>
														<option>Call</option>
												</select></td>
												<td><input class="form-control" type="text"
													name="quantity" id="quantity" /></td>
												<td><select class="form-control" name="currency"
													id="currency" size="1">
														<option>USD</option>
														<option>EUR</option>
														<option>GBP</option>
														<option>CAD</option>
														<option>AUD</option>
												</select></td>
												<td><input class="form-control" type="text" name="rate"
													id="rate" /></td>
												<td><select class="form-control" name="term" size="1">
														<option>1 h</option>
														<option>3 h</option>
														<option>6 h</option>
														<option>12 h</option>
														<option>1 day</option>
														<option>2 day</option>
														<option>3 day</option>
												</select></td>
												<td><input class="col-md-12 btn btn-default"
													type="submit" value="Submit" /></td>
											</tr>
										</table>
									</form>
								</div>
							</div>
						</div>
					</div>
					<!-- End home tab -->
					<!-- My Bets -->
					<div class="tab-pane" id="bcurrent">
						<div class="panel panel-default">
							<div class="panel-body">
								<p>My bets</p>
								<table class='table table-bordered text-center'>
									<tr class="small alert-info">
										<td class="col-md-1">Type</td>
										<td class="col-md-1">Qty</td>
										<td class="col-md-1">Curr</td>
										<td class="col-md-1">Rate</td>
										<td class="col-md-3">Date</td>
										<td class="col-md-1">Term</td>
										<td class="col-md-1">Term Date</td>
										<td class="col-md-1">Status</td>
									</tr>
									<c:forEach items="${ Cbets }" var="bet">
										<tr>
											<td>${bet.type}</td>
											<td>${bet.quantity}</td>
											<td>${bet.currency}</td>
											<td>${bet.rate}</td>
											<td>${bet.betDate}</td>
											<td>${bet.term}</td>
											<td>${bet.termDate}</td>
											<td>${bet.status}</td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>

					</div>
					<!-- Bets history -->
					<div class="tab-pane" id="bhistory">
						<div class="panel panel-default">
							<div class="panel-body">
								<p>History</p>
								<table class='table table-bordered text-center'>
									<tr class="small alert-info">
										<td class="col-md-1">Type</td>
										<td class="col-md-1">Qty</td>
										<td class="col-md-1">Curr</td>
										<td class="col-md-1">Rate</td>
										<td class="col-md-3">Date</td>
										<td class="col-md-1">Term</td>
										<td class="col-md-1">Term Date</td>
										<td class="col-md-1">Term Rate</td>
										<td class="col-md-1">Status</td>
									</tr>
									<c:forEach items="${ Hbets }" var="bet">
										<tr>
											<td>${bet.type}</td>
											<td>${bet.quantity}</td>
											<td>${bet.currency}</td>
											<td>${bet.rate}</td>
											<td>${bet.betDate}</td>
											<td>${bet.term}</td>
											<td>${bet.termDate}</td>
											<td>${bet.termRate}</td>
											<td>${bet.status}</td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>
					</div>
					<!-- End bets history -->
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<c:redirect url="/login" />
		</c:otherwise>
	</c:choose>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://code.jquery.com/jquery.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.js"></script>
</body>
</html>
