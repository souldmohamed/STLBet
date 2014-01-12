<%@page
	import="com.google.appengine.api.search.query.QueryParser.restriction_return"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="upmc.stl.aar.dao.Dao"%>
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
								<li><a href="#bconc" data-toggle="tab">How to play ?</a></li>
								<li><a href="#bcurrent" data-toggle="tab">My bets</a></li>
								<li><a href="#bhistory" data-toggle="tab">History</a></li>
							</ul>
						</div>
					</div>
				</div>

				<!-- Message d'erreur -->
				<c:choose>
					<c:when test="${ Err == 1 }">
						<div class="alert alert-danger alert-dismissable col-md-10">
							<button type="button" class="close" data-dismiss="alert"
								aria-hidden="true">&times;</button>
							Current balance insufficient, please add credits.
						</div>
					</c:when>
					<c:when test="${ Err == 2 }">
						<div class="alert alert-dismissable alert-danger col-md-10">
							<button type="button" class="close" data-dismiss="alert"
								aria-hidden="true">&times;</button>
							Quantity and rates need to have a number format (separator has to
							be a dot).
						</div>
					</c:when>
				</c:choose>
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
								<div class="panel-heading">Top 5 Gains</div>
								<div class="panel-body">
									<table class="table table-bordered">
										<c:forEach var="ind" begin="1" end="5" step="1">
											<tr>
												<td class="col-md-3">${ ind }.</td>
												<td>${ Gains[ind - 1] }</td>
											</tr>
										</c:forEach>
									</table>
								</div>
							</div>
							<div class="panel panel-brown">
								<div class="panel-heading">Top 5 Losses</div>
								<div class="panel-body">
									<table class="table table-bordered">
										<c:forEach var="ind" begin="1" end="5" step="1">
											<tr>
												<td class="col-md-3">${ ind }.</td>
												<td>${ Losses[ind - 1] }</td>
											</tr>
										</c:forEach>
									</table>
								</div>
							</div>
						</div>

						<!-- Current rates section -->
						<div class="container col-md-9">
							<div class="panel panel-brown">
								<div class="panel-heading">Current rates</div>
								<div class="panel-body">
									<table class="table table-bordered">
										<tr class="light-brown">
											<td class="col-md-2">GBP</td>
											<td class="col-md-2">JPY</td>
											<td class="col-md-2">EUR</td>
											<td class="col-md-2">CNY</td>
											<td class="col-md-2">AUD</td>
											<td class="col-md-2">AED</td>
										</tr>
										<tr>
											<td id="GBP"></td>
											<td id="JPY"></td>
											<td id="EUR"></td>
											<td id="CNY"></td>
											<td id="AUD"></td>
											<td id="AED"></td>
										</tr>
										<tr class="light-brown">
											<td>KWD</td>
											<td>SGD</td>
											<td>SAR</td>
											<td>CLF</td>
											<td>KYD</td>
											<td>HNL</td>
										</tr>
										<tr>
											<td id="KWD"></td>
											<td id="SGD"></td>
											<td id="SAR"></td>
											<td id="CLF"></td>
											<td id="KYD"></td>
											<td id="HNL"></td>
										</tr>
										<tr class="light-brown">
											<td>DOP</td>
											<td>LKR</td>
											<td>MRO</td>
											<td>MWK</td>
											<td>TRY</td>
											<td>BMD</td>
										</tr>
										<tr>
											<td id="DOP"></td>
											<td id="LKR"></td>
											<td id="MRO"></td>
											<td id="MWK"></td>
											<td id="TRY"></td>
											<td id="BMD"></td>
										</tr>
									</table>
								</div>
							</div>

							<!-- Right panel -->
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
													id="currency" size="1" onload="generateOptions();">
														<option>GBP</option>
														<option>JPY</option>
														<option>EUR</option>
														<option>CNY</option>
														<option>AUD</option>
														<option>AED</option>
														<option>KWD</option>
														<option>SGD</option>
														<option>SAR</option>
														<option>CLF</option>
														<option>KYD</option>
														<option>HNL</option>
														<option>DOP</option>
														<option>LKR</option>
														<option>MRO</option>
														<option>MWK</option>
														<option>TRY</option>
														<option>BMD</option>
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
										<input type="hidden" name="balance" value="${Player.balance }" />
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
										<td class="col-md-2">Date</td>
										<td class="col-md-1">Term</td>
										<td class="col-md-2">Term Date</td>
										<td class="col-md-1">Status</td>
									</tr>
									<c:forEach items="${ Cbets }" var="bet">
										<tr>
											<td><c:choose>
													<c:when test="${bet.type == 'Put'}">
														<button class="btn btn-info col-md-12 disabled">${bet.type}</button>
													</c:when>
													<c:when test="${bet.type == 'Call'}">
														<button class="btn btn-warning col-md-12 disabled">${bet.type}</button>
													</c:when>
												</c:choose></td>
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
										<td class="col-md-2">Date</td>
										<td class="col-md-1">Term</td>
										<td class="col-md-2">Term Date</td>
										<td class="col-md-1">Term Rate</td>
										<td class="col-md-1">Score</td>
										<td class="col-md-1">Status</td>
									</tr>
									<c:forEach items="${ Hbets }" var="bet">
										<tr>
											<td><c:choose>
													<c:when test="${bet.type == 'Put'}">
														<button class="btn btn-info col-md-12 disabled">${bet.type}</button>
													</c:when>
													<c:when test="${bet.type == 'Call'}">
														<button class="btn btn-warning col-md-12 disabled">${bet.type}</button>
													</c:when>
												</c:choose></td>
											<td>${bet.quantity}</td>
											<td>${bet.currency}</td>
											<td>${bet.rate}</td>
											<td>${bet.betDate}</td>
											<td>${bet.term}</td>
											<td>${bet.termDate}</td>
											<td>${bet.termRate}</td>
											<td>${bet.score}</td>
											<td><c:choose>
													<c:when test="${bet.status == 'Gain'}">
														<button class="btn btn-success col-md-12 disabled">${bet.status}</button>
													</c:when>
													<c:when test="${bet.status == 'Loss'}">
														<button class="btn btn-danger col-md-12 disabled">${bet.status}</button>
													</c:when>
												</c:choose></td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>
					</div>
					<!-- End bets history -->
					<!-- Help -->
					<div class="tab-pane" id="bconc">
						<div class="panel panel-brown">
							<div class="panel-heading">About</div>
							<div class="panel-body">
								<p>In this game, we simply simulate Put and Call options
									based on currency rates.</p>
							</div>
						</div>
						<div class="panel panel-brown">
							<div class="panel-heading">Rules</div>
							<div class="panel-body">
								<p>If the player creates a put (selling option) on a
									currency, he/she is somehow betting on the reduction of this
									currency's rate on due time. His/her gain/loss will be
									determined at that time by the formula :</p>
								<pre> (quantity / term rate) * (bet rate - term rate)</pre>
								<p>On the other hand, if the player creates a call (buying
									option) on a currency, he/she is betting that this currency's
									rate will increase on due time. His/her gain/loss will be
									determined at that time by the formula :</p>
								<pre> (quantity / term rate) * (term rate - bet rate)</pre>
								<p>In both cases, bet rate is represents the rate chosen by
									the player when the bet was created and term rate the effective
									rate of the currency when the bet ended.</p>
							</div>
						</div>
						<div class="panel panel-brown">
							<div class="panel-heading">Definitions by Wikipedia</div>
							<div class="panel-body">
								<p>
									<b>PUT</b>
								</p>
								<blockquote cite="http://en.wikipedia.org/wiki/Put_option">In
									finance, a put or put option is a stock market device which
									gives the owner the right, but not the obligation, to sell an
									asset (the underlying), at a specified price (the strike), by a
									predetermined date (the expiry or maturity) to a given party
									(the seller of the put). Put options are most commonly used in
									the stock market to protect against the decline of the price of
									a stock below a specified price. If the price of the stock
									declines below the specified price of the put option, the owner
									of the put has the right, but not the obligation, to sell the
									asset at the specified price, while the seller of the put, has
									the obligation to purchase the asset at the strike price if the
									buyer uses the right to do so (the buyer is said to exercise
									the put or put option). In this way the owner of the put will
									receive at least the strike price specified even if the asset
									is worth less.</blockquote>
								<p>
									<b>CALL</b>
								</p>
								<blockquote cite="http://en.wikipedia.org/wiki/Call_option">A
									call option, often simply labeled a "call", is a financial
									contract between two parties, the buyer and the seller of this
									type of option.[1] The buyer of the call option has the right,
									but not the obligation to buy an agreed quantity of a
									particular commodity or financial instrument (the underlying)
									from the seller of the option at a certain time (the expiration
									date) for a certain price (thestrike price). The seller (or
									"writer") is obligated to sell the commodity or financial
									instrument to the buyer if the buyer so decides. The buyer pays
									a fee (called a premium) for this right.</blockquote>
							</div>
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
