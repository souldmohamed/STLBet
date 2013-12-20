package upmc.stl.aar.model;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Player {
	
  @Id
  private String palyerId;
  
  private float balance;
  
  public Player(String palyerId, float balance) {
	super();
	this.palyerId = palyerId;
	this.balance = balance;
  }
  
  public String getPalyerId() {
	return palyerId;
  }

  public void setPalyerId(String palyerId) {
	this.palyerId = palyerId;
  }
  
  public float getBalance() {
	return balance;
  }

  public void setBalance(float balance) {
	this.balance = balance;
  }
  
 }