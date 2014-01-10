package upmc.stl.aar.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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

	public void addBet(String userId, String playerEmail, String type,
			float quantity, float rate, String currency, Date betDate,
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

	public void removeBet(long id) {
		EntityManager em = EMFService.get().createEntityManager();
		try {
			ProductBet bet = em.find(ProductBet.class, id);
			em.remove(bet);
		} finally {
			em.close();
		}
	}

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
	 * 
	 * 
	 * @return
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

	public void addDailyGain(String playerId) {
		EntityManager em = EMFService.get().createEntityManager();
		try {
			Query q = em
					.createQuery("select p from Player p where p.playerId = :playerId");
			q.setParameter("playerId", playerId);
			Player p = (Player) q.getSingleResult();
			System.out.println(p.getBalance());
			synchronized (this) {
				p.addBalance(new Float("500"));
				p.setEligible(Boolean.FALSE);
			}
			System.out.println(p.getBalance());
			em.persist(p);
		} finally {
			em.close();
		}
	}

	/**
	 * 
	 * @param playerId
	 * @return
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

	@SuppressWarnings("unchecked")
	public void saveCurrencyRates(String p_rates) {
		CurrencyRates rates = null;
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			try {
				Query q = em.createQuery("select p from CurrencyRates p ");
				List<CurrencyRates> lrates = q.getResultList();
				if (lrates != null && lrates.isEmpty()) {
					System.out.println("....created");
					rates = new CurrencyRates(new Text(""));
				} else {
					System.out.println("....updated");
					rates = lrates.get(0);
				}

				rates.setRates(new Text(p_rates));
				em.persist(rates);
			} finally {
				em.close();
			}
		}
	}

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

				System.out.println("bet ...");
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

					System.out.println("sQuantity=" + sQuantity);
					System.out.println("sBetRate=" + sBetRate);
					System.out.println("sTermRate=" + sTermRate);

					Float quantity = Float.valueOf(sQuantity);
					Float betRate = Float.valueOf(sBetRate);
					Float termRate = Float.valueOf(sTermRate);

					bet.setTermRate(sTermRate);
					int res = 0;
					if (bet.getType().equals("Call")) {
						System.out.println("call");
						res = Math.round(quantity * (termRate - betRate));
						if (termRate >= betRate) {
							System.out.println("gain +" + res);
							bet.setStatus("Gain : " + res);
						} else {
							System.out.println("loss -" + res);
							bet.setStatus("Loss : " + res);
						}
					} else if (bet.getType().equals("Put")) {
						System.out.println("put");
						res = Math.round(quantity * (betRate - termRate));
						if (termRate <= betRate) {
							System.out.println("gain +" + res);
							bet.setStatus("Gain : " + res);
						} else {
							System.out.println("loss -" + res);
							bet.setStatus("Loss : " + res);
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

	@SuppressWarnings("unchecked")
	public void updateBalance(Player player) {
		System.out.println("updateBalance :" + player.getBalance());
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

	private void waitforrefresh() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
