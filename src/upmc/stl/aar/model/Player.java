package upmc.stl.aar.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Player {

	@Id
	private String palyerId;

	private boolean isEligible = Boolean.FALSE;

	private float balance;

	public Player(String playerId, float balance) {
		super();
		this.palyerId = playerId;
		this.balance = balance;
	}

	public String getPlayerId() {
		return palyerId;
	}

	public void setPlayerId(String playerId) {
		this.palyerId = playerId;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public boolean isEligible() {
		return isEligible;
	}

	public void setEligible(boolean isEligible) {
		this.isEligible = isEligible;
	}
	
	public void addBalance(float amount)
	{
		this.balance += amount;
	}
	
	public void removeBalance(float amount)
	{
		this.balance -= amount;
	}

}