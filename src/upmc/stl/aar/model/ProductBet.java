package upmc.stl.aar.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author Lars Vogel
 * 
 */

@Entity
public class ProductBet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String playerId;
	private String type;
	private float quantity;
	private String currency;
	private float rate;
	private Date betDate;
	private String term;
	private Date termDate;
	private String termRate;
	private String status;

	public ProductBet(String playerId, String type, float quantity,
			String currency, float rate, Date betDate, String term,Date termDate,String termRate,
			String status) {
		super();
		this.playerId = playerId;
		this.type = type;
		this.quantity = quantity;
		this.currency = currency;
		this.rate = rate;
		this.betDate = betDate;
		this.term = term;
		this.termDate=termDate;
		this.termRate=termRate;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public Date getBetDate() {
		return betDate;
	}

	public void setBetDate(Date betDate) {
		this.betDate = betDate;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTermDate() {
		return termDate;
	}

	public void setTermDate(Date termDate) {
		this.termDate = termDate;
	}

	public String getTermRate() {
		return termRate;
	}

	public void setTermRate(String termRate) {
		this.termRate = termRate;
	}
	
}
