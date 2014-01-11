package upmc.stl.aar.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import upmc.stl.aar.model.CurrencyRates;
import upmc.stl.aar.model.Player;
import upmc.stl.aar.model.ProductBet;
import upmc.stl.aar.utils.Mail;
import upmc.stl.aar.utils.RatesParser;

import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;
import java.util.logging.Logger;

public enum Dao {
	INSTANCE;

	private final static Logger logger = Logger.getLogger(Dao.class.getName());

	/**
	 * Method called when the player creates a bet. 
	 * Creates a bet and persists it into the database. 
	 * Also sends a mail to the player to notify him of his bet.
	 * 	
	 * @param userId 		- player ID
	 * @param playerEmail	- player Email
	 * @param type			- put or call option
	 * @param quantity		- quantity of currency 
	 * @param rate			- rate at which the player want to buy/sell
	 * @param currency		- currency involved
	 * @param betDate		- time the bet was registered
	 * @param term			- duration of bet selected
	 * @param termDate		- end date for the bet
	 */
	public void addBet(String userId, String playerEmail, String type,
			int quantity, float rate, String currency, Date betDate,
			String term, Date termDate) {
		logger.info("user Id :" + userId + " add bet .....");
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			String term2 = term.replace("+", " ");
			try {
				Player player = getPlayer(userId, playerEmail);
				ProductBet bet = new ProductBet(player.getPlayerId(), type,
						quantity, currency, rate, new Date(), term2, termDate,
						null, "Waiting");
				em.persist(bet);
				Mail.sendMail(player, bet);
			} finally {
				em.close();
			}
		}
		waitforrefresh();
	}

	/**
	 * Method called to retire a bet from the list after it's gain has been evaluated
	 * 
	 * @param id	- Bet id 
	 */
	public void removeBet(long id) {
		EntityManager em = EMFService.get().createEntityManager();
		try {
			ProductBet bet = em.find(ProductBet.class, id);
			em.remove(bet);
		} finally {
			em.close();
		}
	}

	/**
	 * Searches for a player based on his id. 
	 * If the player does not yet exist in the database, he is persisted with his email.
	 * 
	 * 
	 * @param playerId		- Player Id
	 * @param playerEmail	- Player Email
	 * @return an instance of the Player
	 */
	@SuppressWarnings("unchecked")
	public Player getPlayer(String playerId, String playerEmail) {
		Player player = null;
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			try {
				Query q = em
						.createQuery("select p from Player p where p.playerId = :playerId");
				q.setParameter("playerId", playerId);
				List<Player> players = q.getResultList();
				if (players != null && players.isEmpty()) {
					player = new Player(playerId, playerEmail, 1000);
					em.persist(player);
				} else {
					player = players.get(0);
				}
			} finally {
				em.close();
			}
			return player;
		}
	}

	/**
	 * Method called once per day by the cron at midnight. 
	 * Renders every player eligible for a daily credit of 500 USD
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void creditUsers() {
		synchronized (this) {

			EntityManager em = EMFService.get().createEntityManager();
			try {
				Query q = em.createQuery("select p from Player p");
				List<Player> pList = (List<Player>) q.getResultList();
				for (int i = 0; i < pList.size(); i++) {
					Player p = pList.get(i);
					p.setEligible(true);
					em.persist(p);
				}
			} finally {
				em.close();
			}
		}
	}

	/**
	 * Method called when player clicks on the button.
	 * Credits player 500 USD and turns false the isEligible property of this player 
	 * 
	 * @param playerId
	 */
	public void addDailyGain(String playerId) {
		EntityManager em = EMFService.get().createEntityManager();
		try {
			Query q = em
					.createQuery("select p from Player p where p.playerId = :playerId");
			q.setParameter("playerId", playerId);
			Player p = (Player) q.getSingleResult();
			logger.info(""+p.getBalance()+"");
			synchronized (this) {
				// Protective check to prevent abuse.
				if (p.isEligible())
				{
				p.addBalance(new Float("500"));
				p.setEligible(Boolean.FALSE);
				}
			}
			logger.info(""+p.getBalance()+"");
			em.persist(p);
		} finally {
			em.close();
		}
	}

	/**
	 * Retrieves the list of current bets running for this player.
	 * 
	 * @param playerId - player those id we retrieve.
	 * @return the list of bets that are waiting for the player
	 */
	@SuppressWarnings("unchecked")
	public List<ProductBet> getBets(String playerId) {
		EntityManager em = EMFService.get().createEntityManager();
		List<ProductBet> bets = new ArrayList<ProductBet>();
		try {
			Query q = em
					.createQuery("select b from ProductBet b where b.playerId = :playerId and b.status ='Waiting'");
			q.setParameter("playerId", playerId);
			bets = q.getResultList();
		} finally {
			em.close();
		}
		return bets;
	}

	/**
	 * Retrieves the historic list of player bets (all those that are completed).  
	 * 
	 * @param playerId
	 * @return the list of bets that have been completed for player playerId
	 */
	@SuppressWarnings("unchecked")
	public List<ProductBet> getHistoryBets(String playerId) {
		EntityManager em = EMFService.get().createEntityManager();
		List<ProductBet> bets = new ArrayList<ProductBet>();
		try {
			Query q = em
					.createQuery("select b from ProductBet b where b.playerId = :playerId and b.status <> 'Waiting'");
			q.setParameter("playerId", playerId);
			bets = q.getResultList();
		} finally {
			em.close();
		}
		return bets;
	}

	/**
	 * Retrieves the rate of all currencies stored in the database.
	 *  
	 * @return Text field representing the JSON response received by the API
	 */
	@SuppressWarnings("unchecked")
	public CurrencyRates getCurrencyRates() {
		CurrencyRates rates = null;
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			try {
				Query q = em.createQuery("select p from CurrencyRates p ");
				List<CurrencyRates> lrates = q.getResultList();
				if (lrates != null && lrates.isEmpty()) {
					rates = new CurrencyRates(new Text(""));
				} else {
					rates = lrates.get(0);
				}
			} finally {
				em.close();
			}
		}
		return rates;
	}

	/**
	 * Persists currency rates into the database. 
	 * Called by the cron to update currency rates in the database
	 * 
	 * @param p_rates currency in JSON format 
	 */
	@SuppressWarnings("unchecked")
	public void saveCurrencyRates(String p_rates) {
		CurrencyRates rates = null;
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			try {
				Query q = em.createQuery("select p from CurrencyRates p ");
				List<CurrencyRates> lrates = q.getResultList();
				if (lrates != null && lrates.isEmpty()) {
					logger.info("....created");
					rates = new CurrencyRates(new Text(""));
				} else {
					logger.info("....updated");
					rates = lrates.get(0);
				}

				rates.setRates(new Text(p_rates));
				em.persist(rates);
			} finally {
				em.close();
			}
		}
	}

	/**
	 * Method called by scheduled cron task. Calculates the gain/loss 
	 * for every bet those termination date has expired
	 */
	@SuppressWarnings("unchecked")
	public void calculateGain() {

		EntityManager em = EMFService.get().createEntityManager();

		try {
			// Retrieve all bets whom status is waiting
			Query q = em
					.createQuery("select t from ProductBet t where t.status='Waiting'");

			EntityTransaction tr = null;

			List<ProductBet> bets = q.getResultList();

			Date now = new Date();

			for (ProductBet bet : bets) {

				logger.info("bet ...");
				Player player = getPlayer(bet.getPlayerId(), "");

				tr = em.getTransaction();
				tr.begin();

				// select the bet whom status = waiting and betDate <
				// CurrencyRates
				// Timestamp and termDate < now
				if (bet.getTermDate().before(now)
						&& bet.getBetDate().before(
								RatesParser.getCurrencyRateTimestamp())) {
					float sQuantity = bet.getQuantity();
					float sBetRate = bet.getRate();
					String sTermRate = RatesParser.getCurrencyRate(bet
							.getCurrency());

					logger.info("sQuantity=" + sQuantity);
					logger.info("sBetRate=" + sBetRate);
					logger.info("sTermRate=" + sTermRate);

					Float quantity = Float.valueOf(sQuantity);
					Float betRate = Float.valueOf(sBetRate);
					Float termRate = Float.valueOf(sTermRate);

					bet.setTermRate(sTermRate);
					int res = 0;
					if (bet.getType().equals("Call")) {
						logger.info("call");
						res = Math.round(quantity * (termRate - betRate));
						if (termRate >= betRate) {
							logger.info("Gain" + res);
							bet.setScore(res);
							bet.setStatus("Gain");
						} else {
							logger.info("Loss " + res);
							bet.setScore(res);
							bet.setStatus("Loss");
						}
					} else if (bet.getType().equals("Put")) {
						logger.info("put");
						res = Math.round(quantity * (betRate - termRate));
						if (termRate <= betRate) {
							logger.info("Gain "  + res);
							bet.setScore(res);
							bet.setStatus("Gain");
						} else {
							logger.info("Loss " + res);
							bet.setScore(res);
							bet.setStatus("Loss");
						}
					}
					player.setBalance(player.getBalance() + res);
					updateBalance(player);

				}
				em.persist(bet);
				tr.commit();
				Mail.sendMail(player, bet);
			}
		} finally {
			em.close();
		}
		waitforrefresh();
	}

	
	/**
	 * Internal method called by CalculateGain. 
	 * Updates player balance based on bet outcome.
	 * 
	 * @param player
	 */
	@SuppressWarnings("unchecked")
	private void updateBalance(Player player) {
		logger.info("updateBalance :" + player.getBalance());
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			try {
				Query q = em
						.createQuery("select p from Player p where p.playerId = :playerId");
				q.setParameter("playerId", player.getPlayerId());
				List<Player> players = q.getResultList();
				if (players != null && !players.isEmpty()) {
					Player player_to_update = players.get(0);
					player_to_update.setBalance(player.getBalance());
					em.persist(player_to_update);
				}
			} finally {
				em.close();
			}
		}
	}
    
	/**
	 * This method retreive scors of a player in descreasing order
	 * @param playerId
	 * @return
	 */
	public List<Float> getPlayerScores(String playerId){
		
		List<Float> list = new ArrayList<Float>();
		List<Float> result = new ArrayList<Float>();
		
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			try {
				Query q = em
						.createQuery("select p.score from ProductBet p where p.playerId = :playerId  and p.status <> 'Waiting' ");
				q.setParameter("playerId", playerId);
				list = (List<Float>)q.getResultList();	
			} finally {
				em.close();
			}
		}
		
		if(list != null) {
		    result.addAll(list);
		    Collections.sort(result);
			Collections.reverse(result);
		}
		
		return result;
	}
	
	/**
	 * Sleep method
	 */
	private void waitforrefresh() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
