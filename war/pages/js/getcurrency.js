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

			var EUR = $('#EUR');
			EUR.html("");
			EUR.append(myJSON.rates.EUR);

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
			
			var CLF = $('#CLF');
			CLF.html("");
			CLF.append(myJSON.rates.CLF);
			
			var KYD = $('#KYD');
			KYD.html("");
			KYD.append(myJSON.rates.KYD);
			
			var HNL = $('#HNL');
			HNL.html("");
			HNL.append(myJSON.rates.HNL);

			var DOP = $('#DOP');
			DOP.html("");
			DOP.append(myJSON.rates.DOP);
			
			var LKR = $('#LKR');
			LKR.html("");
			LKR.append(myJSON.rates.LKR);
			
			var MRO = $('#MRO');
			MRO.html("");
			MRO.append(myJSON.rates.MRO);
			
			var MWK = $('#MWK');
			MWK.html("");
			MWK.append(myJSON.rates.MWK);
			
			var TRY = $('#TRY');
			TRY.html("");
			TRY.append(myJSON.rates.TRY);
			
			var BMD= $('#BMD');
			BMD.html("");
			BMD.append(myJSON.rates.BMD);
		}
	});
}

