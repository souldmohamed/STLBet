package upmc.stl.aar.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.google.appengine.api.datastore.Text;

/**
 * Model entity for CurrencyRates. 
 * Simple Text storing the JSON response from the API 
 *
 */
@Entity
public class CurrencyRates {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Text rates;

	public CurrencyRates(Text rates) {
		this.rates = rates;
	}

	/**
	 * Getter method for id
	 * @return Long
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter method for id
	 * @param id - Long
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter method for rates
	 * @return Text
	 */
	public Text getRates() {
		return rates;
	}

	/**
	 * Setter method for rates
	 * @param rates - Text
	 */
	public void setRates(Text rates) {
		this.rates = rates;
	}
  
}