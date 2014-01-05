package upmc.stl.aar.utils;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import upmc.stl.aar.dao.Dao;
import upmc.stl.aar.model.CurrencyRates;

public class RatesParser {

	public static String getCurrencyRate(String currency) {
		System.out.println("getCurrencyRate currency:"+currency);
		String result = null;
		try{
			Dao DAO = Dao.INSTANCE;
			CurrencyRates cr = DAO.getCurrencyRates();
			if (cr != null) {
				JSONObject jo = new JSONObject(cr.getRates().getValue());
				JSONObject rates = jo.getJSONObject("rates");
				result = rates.getString(currency);
			}
		}catch(JSONException e){
			System.out.println("Parsing error ...");
			e.printStackTrace();
		}
		System.out.println("getCurrencyRate result:"+result);
		return result;
	}
	
}