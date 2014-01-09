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

<html class="light-orange">
<head>
<title>STL Bet</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="js/getcurrency.js"></script>
<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
%>
</head>
<body onload="callAjax()">
	<c:choose>
		<c:when test="<%=userService.isUserLoggedIn()%>">
			<!-- Header -->
			<div class="container col-md-12 bkground">
				<div class="col-md-3">
					<img src="images/logo.png" style="height: 200px">
				</div>


				<div class="container col-md-9 orange">
					<br />
					<h1>
						<b>U.G.L.Y Bet.ty</b>
					</h1>
					<small>Universally Gloomy Looking Yeti Bet Thanks to You</small>

					<h3>Come for the shinies, stay for the shinies.</h3>
				</div>
			</div>

			<!-- End header -->

			<!-- Navigation bar -->
			<div class="container col-md-12">
				<br />
				<div class="navbar bkground" role="navigation">
					<div class="collapse navbar-collapse" id="Div1">
						<ul class="nav navbar-nav navbar-left">
							<li class="navbar-text"><div class="orange">
									Welcome,
									<%=user.getNickname()%>
								</div></li>
						</ul>
						<c:if test="${ Player.isEligible }">
							<ul class="nav navbar-nav navbar-left">
								<form class="navbar-form" method="post" action="/DailyGain">
									<li><input class="btn btn-default form-control"
										type="submit" value="Add credit (once per 24h)" /></li>
								</form>
							</ul>
						</c:if>

						<ul class="nav navbar-nav navbar-right">
							<li><a href="${ Logout }"><div class="orange">
										Logout</div></a></li>
						</ul>
					</div>
				</div>
			</div>
			<!-- Navigation bar end-->


			<!-- Body -->
			<div class="container col-md-12">
				<!-- Left panels -->
				<div class="container col-md-2">
					<div class="panel panel-default">
						<div class="panel-body bkground bs-sidebar hidden-print"
							role="complementary" style="height: 600px">
							<ul class="nav bs-sidenav orange">
								<li><a href="#bhome" data-toggle="tab">Home</a></li>
								<li><a href="#bhelp" data-toggle="tab">How to play ?</a></li>
								<li><a href="#bcurrent" data-toggle="tab">My bets</a></li>
								<li><a href="#bhistory" data-toggle="tab">History</a></li>
							</ul>
						</div>
					</div>
				</div>

				<!-- Message d'erreur -->
				<c:if test="${ Error }">
					<div class="tab-content col-md-10">
						<div class="panel panel-danger">
							<div class="panel-body alert-danger">Current balance
								insufficient, please add credits.</div>
						</div>
					</div>
				</c:if>
				<!-- End message d'erreur -->

				<!-- Tab panes -->
				<div class="tab-content col-md-10">
					<!-- Home tab -->
					<div class="tab-pane active" id="bhome">
						<div class="container col-md-3">
							<!-- Balance -->
							<div class="panel panel-brown">
								<div class="panel-heading">Balance</div>
								<div class="panel-body ">${ Player.balance }</div>
							</div>
						</div>
						<div class="container col-md-4">
							<!-- Base section -->
							<div class="panel panel-brown">
								<div class="panel-heading">Base</div>
								<div class="panel-body" id="base"></div>
							</div>
						</div>
						<div class="container col-md-5">
							<!-- Last updated section -->
							<div class="panel panel-brown">
								<div class="panel-heading light-brown">Last updated</div>
								<div class="panel-body" id="lastupdate"></div>
							</div>
						</div>
						<!-- Currencies info -->
						<div class="container col-md-3">

							<div class="panel panel-brown">
								<div class="panel-heading">Top Scores</div>
								<div class="panel-body">
									<table class="table table-bordered">
										<tr>
											<td class="col-md-3">1.</td>
											<td></td>
										</tr>
										<tr>
											<td>2.</td>
											<td></td>
										</tr>
										<tr>
											<td>3.</td>
											<td></td>
										</tr>
										<tr>
											<td>4.</td>
											<td></td>
										</tr>
										<tr>
											<td>5.</td>
											<td></td>
										</tr>
										<tr>
											<td>6.</td>
											<td></td>
										</tr>
										<tr>
											<td>7.</td>
											<td></td>
										</tr>
										<tr>
											<td>8.</td>
											<td></td>
										</tr>
										<tr>
											<td>9.</td>
											<td></td>
										</tr>
									</table>
								</div>
							</div>
							<!-- Current rates section -->

						</div>

						<div class="container col-md-9">
							<div class="panel panel-brown">
								<div class="panel-heading">Current rates</div>
								<div class="panel-body">
									<table class="table table-bordered">
										<tr class="light-brown">
											<td>GBP</td>
											<td>JPY</td>
											<td>EUR</td>
											<td>CNY</td>
											<td>AUD</td>
											<td>AED</td>
											<td>KWD</td>
											<td>SGD</td>
											<td>SAR</td>
										</tr>
										<tr>
											<td id="GBP"></td>
											<td id="JPY"></td>
											<td id="EUR"></td>
											<td id="CNY"></td>
											<td id="AUD"></td>
											<td id="AED"></td>
											<td id="KWD"></td>
											<td id="SGD"></td>
											<td id="SAR"></td>
										</tr>
									</table>
								</div>
							</div>
						</div>

						<!-- Right panel -->
						<div class="container col-md-9">
							<div class="panel panel-brown">
								<div class="panel-heading">Actions</div>
								<div class="panel-body">
									<form class="form-horizontal" action="/new" method="post"
										accept-charset="utf-8">

										<table class="table table-bordered">
											<tr class="light-brown text-center">
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
													name="quantity" id="quantity" required></td>
												<td><select class="form-control" name="currency"
													id="currency" size="1">
														<option>GBP</option>
														<option>JPY</option>
														<option>EUR</option>
														<option>CNY</option>
														<option>AUD</option>
														<option>AED</option>
														<option>KWD</option>
														<option>SGD</option>
														<option>SAR</option>
												</select></td>
												<td><input class="form-control" type="text" name="rate"
													id="rate" required></td>
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
						<div class="panel panel-brown">
							<div class="panel-heading">My bets</div>
							<div class="panel-body">
								<table class='table table-bordered text-center'>
									<tr class="small light-brown">
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
						<div class="panel panel-brown">
							<div class="panel-heading">Bets history</div>
							<div class="panel-body">
								<table class='table table-bordered text-center'>
									<tr class="small light-brown">
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
					<!-- Help -->
					<div class="tab-pane" id="bhelp">
						<p>Blabla bla</p>
					</div>
					<!-- End Help -->
				</div>
				<!-- End TabPanes -->
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
