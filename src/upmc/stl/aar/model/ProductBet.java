package upmc.stl.aar.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity model representing a bet 
 * 
 */

@Entity
public class ProductBet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String playerId;
	private String type;
	private int quantity;
	private String currency;
	private float rate;
	private Date betDate;
	private String term;
	private Date termDate;
	private String termRate;
	private String status;
	private float score;

	/**
	 * Bet constructor
	 * @param playerId  - String
	 * @param type		- String
	 * @param quantity	- float
	 * @param currency	- String
	 * @param rate		- float
	 * @param betDate	- Date
	 * @param term		- String	
	 * @param termDate	- Date
	 * @param termRate	- String
	 * @param status	- String
	 */
	public ProductBet(String playerId, String type, int quantity,
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

	/**
	 * Getter for bet id
	 * @return Long
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Getter for player id
	 * @return String
	 */
	public String getPlayerId() {
		return playerId;
	}
	
	/**
	 * Setter for player id
	 * @param playerId String
	 */
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	/**
	 * Getter for bet quantity
	 * @return float
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Setter for bet quantity
	 * @param quantity float
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Getter for currency whom player chose to bet on
	 * @return String
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Setter for currency whom player chose to bet on
	 * @param currency String
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * Getter for currency rate whom player chose for his bet
	 * @return float
	 */
	public float getRate() {
		return rate;
	}

	/**
	 * Setter for currency rate whom player chose for his bet
	 * @param rate float
	 */
	public void setRate(float rate) {
		this.rate = rate;
	}

	/**
	 * Getter for date of creation of the bet
	 * @return Date
	 */
	public Date getBetDate() {
		return betDate;
	}

	/**
	 * Setter for bet creation date
	 * @param betDate Date
	 */
	public void setBetDate(Date betDate) {
		this.betDate = betDate;
	}

	/**
	 * Getter for bet duration 
	 * @return String
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Setter for bet duration
	 * @param term String
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * Getter for bet status
	 * @return String
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Setter for bet status
	 * @param status String
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Setter for bet id
	 * @param id Long
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for bet type (put or call)
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * Setter for bet type (put or call)
	 * @param type String
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Getter for bet termination date
	 * @return Date
	 */
	public Date getTermDate() {
		return termDate;
	}

	/**
	 * Setter for bet termination date
	 * @param termDate Date
	 */
	public void setTermDate(Date termDate) {
		this.termDate = termDate;
	}

	/**
	 * Getter for currency rate at time of bet termination 
	 * @return String
	 */
	public String getTermRate() {
		return termRate;
	}

	/**
	 * Setter for currency rate at time of bet termination
	 * @param termRate String
	 */
	public void setTermRate(String termRate) {
		this.termRate = termRate;
	}
    
	/**
	 * Getter for score of bet 
	 * @return float
	 */
	public float getScore() {
		return score;
	}
    
	/**
	 * Setter for score of bet
	 * @param score float
	 */
	public void setScore(float score) {
		this.score = score;
	}
	
}
