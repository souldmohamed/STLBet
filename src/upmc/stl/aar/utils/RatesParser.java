package upmc.stl.aar.utils;

import java.util.Date;
import java.util.logging.Logger;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import upmc.stl.aar.dao.Dao;
import upmc.stl.aar.model.CurrencyRates;
import upmc.stl.aar.servlet.GetCurrenciesCron;

public class RatesParser {
    
	private final static Logger logger = Logger.getLogger(RatesParser.class.getName());
	
	/**
	 * Retrieves rate of currency passed as a parameter
	 * @param String
	 * @return String
	 */
	public static String getCurrencyRate(String currency) {
		logger.info("getCurrencyRate currency:"+currency);
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
			logger.info("Parsing error ...");
			e.printStackTrace();
		}
		logger.info("getCurrencyRate result:"+result);
		return result;
	}
	
	/**
	 * Retrieves currency last update timestamp. 
	 * @return Date
	 */
	public static Date getCurrencyRateTimestamp() {
		logger.info("getCurrencyRateTimestamp...");
		Date result = null;
		try{
			Dao DAO = Dao.INSTANCE;
			CurrencyRates cr = DAO.getCurrencyRates();
			if (cr != null) {
				JSONObject jo = new JSONObject(cr.getRates().getValue());
				result = new Date(jo.getLong("timestamp")*1000);
			}
		}catch(JSONException e){
			logger.info("Parsing error ...");
			e.printStackTrace();
		}
		logger.info("getCurrencyRate result:"+result);
		return result;
	}
}