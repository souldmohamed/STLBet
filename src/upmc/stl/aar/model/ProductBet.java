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
  
  private String userId;
  private String type;
  private String quantity;
  private String currency;
  private String rates;
  private Date betDate;
  private String term;
  private String status;

  public ProductBet(String userId, String type, String quantity,
		String currency, String rates, Date betDate, String term, String status) {
	super();
	//this.id = id;
	this.userId = userId;
	this.type = type;
	this.quantity = quantity;
	this.currency = currency;
	this.rates = rates;
	this.betDate = betDate;
	this.term = term;
	this.status = status;
}

  public Long getId() {
    return id;
  }

  public String getUserId() {
	return userId;
  }

  public void setUserId(String userId) {
	this.userId = userId;
  }

  public String getQuantity() {
	return quantity;
  }

  public void setQuantity(String quantity) {
	this.quantity = quantity;
  }

  public String getCurrency() {
	return currency;
  }

  public void setCurrency(String currency) {
	this.currency = currency;
  }

  public String getRates() {
	return rates;
  }

  public void setRates(String rates) {
	this.rates = rates;
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
  
  
 
} 
