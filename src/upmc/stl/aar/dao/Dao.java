package upmc.stl.aar.dao;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import upmc.stl.aar.model.CurrencyRates;
import upmc.stl.aar.model.Player;
import upmc.stl.aar.model.ProductBet;

import com.google.appengine.api.datastore.Text;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;

public enum Dao {
	INSTANCE;

	public void addBet(String userId, String playerEmail, String type, String quantity,
			String rate, String currency, String term) {
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			Player player = getPlayer(userId, playerEmail);
			ProductBet bet = new ProductBet(player.getPlayerId(), type,
					quantity, currency, rate, new Date(), term, "Waiting");
			em.persist(bet);
			em.close();
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
	public Player getPlayer(String palyerId, String playerEmail) {
		Player player = null;
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			Query q = em
					.createQuery("select p from Player p where p.palyerId = :palyerId");
			q.setParameter("palyerId", palyerId);
			List<Player> players = q.getResultList();
			if (players != null && players.isEmpty()) {
				player = new Player(palyerId, playerEmail, 1000);
				em.persist(player);
				em.close();
			} else {
				player = players.get(0);
			}
			return player;
		}
	}
	
	/**
	 * Retrieves the list of all players
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Player> getPlayers()
	{
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			Query q = em
					.createQuery("select p from Player p");
			List<Player> players = (List<Player>) q.getResultList();
			return players;
		}
	}
	
	public void addDailyGain(String playerId, String playerEmail)
	{
		EntityManager em = EMFService.get().createEntityManager();
		
		Player p = getPlayer(playerId, playerEmail);
		
		synchronized (this) {
			p.addBalance(new Float("500"));
			p.setEligible(Boolean.FALSE);
		}
		
		em.persist(p);
		em.close();
	}
	
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductBet> getBets(String userId) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em
				.createQuery("select b from ProductBet b where b.userId = :userId and b.status ='Waiting'");
		q.setParameter("userId", userId);
		List<ProductBet> bets = q.getResultList();
		return bets;
	}

	@SuppressWarnings("unchecked")
	public List<ProductBet> getHistoryBets(String userId) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em
				.createQuery("select b from ProductBet b where b.userId = :userId and b.status <> 'Waiting'");
		q.setParameter("userId", userId);
		List<ProductBet> bets = q.getResultList();
		return bets;
	}

	@SuppressWarnings("unchecked")
	public CurrencyRates getCurrencyRates() {
		CurrencyRates rates = null;
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			Query q = em.createQuery("select p from CurrencyRates p ");
			List<CurrencyRates> lrates = q.getResultList();
			if (lrates != null && lrates.isEmpty()) {
				rates = new CurrencyRates(new Text(""));
			} else {
				rates = lrates.get(0);
			}
		}
		return rates;
	}

	@SuppressWarnings("unchecked")
	public void saveCurrencyRates(String p_rates) {
		CurrencyRates rates = null;
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();

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
			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	public void calculateGain(long gain) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em
				.createQuery("select t from ProductBet t where t.status='Waiting'");
		EntityTransaction tr = null;

		List<ProductBet> bets = q.getResultList();
		int i = 0;
		for (ProductBet bet : bets) {
			tr = em.getTransaction();
			tr.begin();
			// TODO CalculGain a modifier
			if (i % 2 == 0) {
				bet.setStatus("Gain");
			} else {
				bet.setStatus("Loss");
			}
			em.persist(bet);
			tr.commit();
			
			Query q2 = em.createQuery("select p from Player p where p.palyerId = :palyerId");
			q2.setParameter("palyerId", bet.getUserId());
			Player player = (Player) q2.getResultList().get(0);

			try {
				Properties props = new Properties();
	            Session session = Session.getDefaultInstance(props, null);
	            
				Message msg = new MimeMessage(session);
	            msg.setFrom(new InternetAddress("admin@m2stlbetapp.appspotmail.com", "m2stlbetapp"));
	            msg.addRecipient(Message.RecipientType.TO,
	                             new InternetAddress(player.getPlayerEmail(), ""));
	            msg.setSubject("[m2stlbetapp] bet (" + bet.getQuantity() + ") " + bet.getStatus());
	            msg.setText("Infos :"
	            		+ "\ntype : " + bet.getType()
	            		+ "\nquantity : " + bet.getQuantity()
	            		+ "\ncurrency : " + bet.getCurrency()
	            		+ "\nrates : " + bet.getRates()
	            		+ "\nbetDate : " + bet.getBetDate()
	            		+ "\nterm : " + bet.getTerm()
	            		+ "\nstatus : " + bet.getStatus());
	            Transport.send(msg);
			} catch (MessagingException e) {
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	        	e.printStackTrace();
	        }
			
			i++;
		}

		em.close();
		waitforrefresh();
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
