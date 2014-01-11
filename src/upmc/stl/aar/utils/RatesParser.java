package upmc.stl.aar.utils;

import java.util.Date;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import upmc.stl.aar.dao.Dao;
import upmc.stl.aar.model.CurrencyRates;

public class RatesParser {

	/**
	 * Retrieves rate of currency passed as a parameter
	 * @param String
	 * @return String
	 */
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
	
	/**
	 * Retrieves currency last update timestamp. 
	 * @return Date
	 */
	public static Date getCurrencyRateTimestamp() {
		System.out.println("getCurrencyRateTimestamp...");
		Date result = null;
		try{
			Dao DAO = Dao.INSTANCE;
			CurrencyRates cr = DAO.getCurrencyRates();
			if (cr != null) {
				JSONObject jo = new JSONObject(cr.getRates().getValue());
				result = new Date(jo.getLong("timestamp")*1000);
			}
		}catch(JSONException e){
			System.out.println("Parsing error ...");
			e.printStackTrace();
		}
		System.out.println("getCurrencyRate result:"+result);
		return result;
	}
}