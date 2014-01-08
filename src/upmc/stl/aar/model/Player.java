package upmc.stl.aar.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Player {

	@Id
	private String playerId;
	
	private String playerEmail;

	private boolean isEligible = Boolean.FALSE;

	private float balance;

	public Player(String playerId, String playerEmail, float balance) {
		super();
		this.playerId = playerId;
		this.playerEmail = playerEmail;
		this.balance = balance;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String palyerId) {
		this.playerId = palyerId;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}
    
	public boolean getIsEligible() {
		return isEligible;
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