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
import upmc.stl.aar.utils.Mail;
import upmc.stl.aar.utils.RatesParser;

import com.google.appengine.api.datastore.Text;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;

public enum Dao {
	INSTANCE;

	public void addBet(String userId, String playerEmail, String type, String quantity,
			String rate, String currency, Date betDate, String term, Date termDate) {
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			Player player = getPlayer(userId, playerEmail);
			ProductBet bet = new ProductBet(player.getPlayerId(), type,
					quantity, currency, rate, new Date(), term, termDate,null, "Waiting");
			em.persist(bet);
			em.close();
			
			Mail.sendMail(player, bet);
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
	 * @param playerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductBet> getBets(String playerId) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em
				.createQuery("select b from ProductBet b where b.playerId = :playerId and b.status ='Waiting'");
		q.setParameter("playerId", playerId);
		List<ProductBet> bets = q.getResultList();
		return bets;
	}

	@SuppressWarnings("unchecked")
	public List<ProductBet> getHistoryBets(String playerId) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em
				.createQuery("select b from ProductBet b where b.playerId = :playerId and b.status <> 'Waiting'");
		q.setParameter("playerId", playerId);
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
	public void calculateGain() {
		
		EntityManager em = EMFService.get().createEntityManager();
		
		// Retreive all bets whome status is wating
		Query q = em.createQuery("select t from ProductBet t where t.status='Waiting'");
		
		EntityTransaction tr = null;

		List<ProductBet> bets = q.getResultList();
		
		Date now = new Date();
		
		for (ProductBet bet : bets) {
			
			System.out.println("bet ...");
			Player player = getPlayer(bet.getPlayerId(), "");
			
			tr = em.getTransaction();
			tr.begin();
			if(bet.getTermDate().after(now)){
				String sQuantity = bet.getQuantity();
				String sBetRate = bet.getRate();
				String sTermRate = RatesParser.getCurrencyRate(bet.getCurrency());
				
				System.out.println("sQuantity="+sQuantity);
				System.out.println("sBetRate="+sBetRate);
				System.out.println("sTermRate="+sTermRate);
				
				Float quantity = Float.valueOf(sQuantity) ;
				Float betRate = Float.valueOf(sBetRate) ;
				Float termRate = Float.valueOf(sTermRate) ;
				
				bet.setTermRate(sTermRate);
				int res = 0;
				if(bet.getType().equals("Call")){
					System.out.println("call");
					res = Math.round(quantity * (termRate - betRate));
					if(termRate >= betRate){
						System.out.println("gain +"+res);
						bet.setStatus("Gain : "+res);
					}else{
						System.out.println("loss -"+res);
						bet.setStatus("Loss : "+res);
					}
				}else if(bet.getType().equals("Put")){
					System.out.println("put");
					res = Math.round(quantity * (betRate - termRate));
					if(termRate <= betRate){
						System.out.println("gain +"+res);
						bet.setStatus("Gain : "+res);
					}else{
						System.out.println("loss -"+res);
						bet.setStatus("Loss : "+res);
					}
				}
				player.setBalance(player.getBalance()+res);
				updateBalance(player);
				
			}
			em.persist(bet);
			tr.commit();
			Mail.sendMail(player, bet);
		}

		em.close();
		waitforrefresh();
	}

	public void updateBalance(Player player) {
		System.out.println("updateBalance :"+player.getBalance());
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			Query q = em
					.createQuery("select p from Player p where p.palyerId = :palyerId");
			q.setParameter("palyerId", player.getPlayerId());
			List<Player> players = q.getResultList();
			if (players != null && !players.isEmpty()) {
				Player player_to_update = players.get(0);
				player_to_update.setBalance(player.getBalance());
				em.persist(player_to_update);
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
