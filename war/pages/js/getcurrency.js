function callAjax() {
	$.ajax({
		url : '/getCurrencies',
		dataType : 'json',
		success : function(myJSON) {

			var base = $('#base');
			base.html("");
			base.append(myJSON.base);

			var lsup = $('#lastupdate');
			lsup.html("");
			lsup.append(new Date(myJSON.timestamp * 1000));

			var GBP = $('#GBP');
			GBP.html("");
			GBP.append(myJSON.rates.GBP);

			var JPY = $('#JPY');
			JPY.html("");
			JPY.append(myJSON.rates.JPY);

			var eur = $('#EUR');
			eur.html("");
			eur.append(myJSON.rates.EUR);

			var CNY = $('#CNY');
			CNY.html("");
			CNY.append(myJSON.rates.CNY);

			var AUD = $('#AUD');
			AUD.html("");
			AUD.append(myJSON.rates.AUD);

			var AED = $('#AED');
			AED.html("");
			AED.append(myJSON.rates.AED);

			var KWD = $('#KWD');
			KWD.html("");
			KWD.append(myJSON.rates.KWD);

			var SGD = $('#SGD');
			SGD.html("");
			SGD.append(myJSON.rates.SGD);

			var SAR = $('#SAR');
			SAR.html("");
			SAR.append(myJSON.rates.SAR);
		}
	});
}
