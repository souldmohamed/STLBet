package upmc.stl.aar.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Model entity representing a Player
 *
 */
@Entity
public class Player {

	@Id
	private String playerId;
	
	private String playerEmail;

	// Eligibility for daily credit
	private boolean isEligible = Boolean.FALSE;

	// Player balance
	private float balance;
	
	/**
	 * Player constructor
	 * @param playerId - String 
	 * @param playerEmail - String 
	 * @param balance - float
	 */
	public Player(String playerId, String playerEmail, float balance) {
		super();
		this.playerId = playerId;
		this.playerEmail = playerEmail;
		this.balance = balance;
	}

	/**
	 * Getter for playerId
	 * @return String
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * Setter for playerId
	 * @param palyerId - String
	 */
	public void setPlayerId(String palyerId) {
		this.playerId = palyerId;
	}
	
	/**
	 * Getter for player email 
	 * @return String
	 */
	public String getPlayerEmail() {
		return playerEmail;
	}

	/**
	 * Setter for player email
	 * @param playerEmail - String
	 */
	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	/**
	 * Getter for player balance
	 * @return float
	 */
	public float getBalance() {
		return balance;
	}

	/**
	 * Setter for player balance
	 * @param balance - float
	 */
	public void setBalance(float balance) {
		this.balance = balance;
	}
    
	/**
	 * Getter for player eligibility for daily credit
	 * @return boolean
	 */
	public boolean getIsEligible() {
		return isEligible;
	}
	
	/**
	 * Duplicate of getIsEligible() method. Previous method was mandatory for the property to be taken into account
	 * @return boolean
	 */
	public boolean isEligible() {
		return isEligible;
	}

	/**
	 * Setter for player eligibility to daily credit
	 * @param isEligible boolean
	 */
	public void setEligible(boolean isEligible) {
		this.isEligible = isEligible;
	}
	
	/**
	 * Add method for player balance 
	 * @param amount - float 
	 */
	public void addBalance(float amount)
	{
		this.balance += amount;
	}
	
	/**
	 * Substract method for player  balance
	 * @param amount - float
	 */
	public void removeBalance(float amount)
	{
		this.balance -= amount;
	}
    
}