package upmc.stl.aar.model;

import java.util.ArrayList;
import java.util.List;

import org.mortbay.util.ajax.JSON;

import com.google.appengine.api.datastore.Text;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.labs.repackaged.org.json.JSONStringer;

import upmc.stl.aar.dao.Dao;

public class CurrencyRatesParser {
	public void getCurrencyRates() throws JSONException
	{
		System.out.println("inside");
		Dao DAO = Dao.INSTANCE;
		CurrencyRates cr = DAO.getCurrencyRates();
		JSONObject jo = new JSONObject(cr.getRates());
		JSONArray ja = new JSONArray();
		jo.toJSONArray(ja);
		for (int i = 0; i<ja.length();i++)
		{
			System.out.println(ja.get(i));
		}
		System.out.println("finished");
	}
}