package upmc.stl.aar.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.google.appengine.api.datastore.Text;

@Entity
public class CurrencyRates {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Text rates;

	public CurrencyRates(Text rates) {
		this.rates = rates;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Text getRates() {
		return rates;
	}

	public void setRates(Text rates) {
		this.rates = rates;
	}
  
}