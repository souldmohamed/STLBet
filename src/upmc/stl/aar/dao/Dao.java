package upmc.stl.aar.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import upmc.stl.aar.model.CurrencyRates;
import upmc.stl.aar.model.Player;
import upmc.stl.aar.model.ProductBet;

import com.google.appengine.api.datastore.Text;

public enum Dao {
  INSTANCE;
 
  public void addBet(String userId, String type, String quantity, String rate,String currency, String term) {
	    synchronized (this) {
	      EntityManager em = EMFService.get().createEntityManager();
	      Player player = getPlayer(userId);
	      ProductBet bet = new ProductBet(player.getPalyerId(), type, quantity, currency, rate, new Date(), term, "Waiting");
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
  public Player getPlayer(String palyerId) {
	  Player player=null;
	  synchronized (this) {
	      EntityManager em = EMFService.get().createEntityManager();
	      Query q = em.createQuery("select p from Player p where p.palyerId = :palyerId");
	      q.setParameter("palyerId", palyerId);
	      List<Player> players = q.getResultList();
	      if(players != null && players.isEmpty()){
	    	  player = new Player(palyerId, 1000);
	    	  em.persist(player);
		      em.close();
	      }else{
	    	  player=players.get(0);
	      }
	      return player;
	  }
  }
  
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
	  CurrencyRates rates=null;
	  synchronized (this) {
	      EntityManager em = EMFService.get().createEntityManager();
	      Query q = em.createQuery("select p from CurrencyRates p ");
	      List<CurrencyRates> lrates = q.getResultList();
	      if(lrates != null && lrates.isEmpty()){
	    	  rates = new CurrencyRates(new Text(""));
	      }else{
	    	  rates=lrates.get(0);
	      }
	  }
	  return rates;
  }
  

  public void saveCurrencyRates(String p_rates) {
	  CurrencyRates rates=null;
	  synchronized (this) {
	      EntityManager em = EMFService.get().createEntityManager();
	      
	      Query q = em.createQuery("select p from CurrencyRates p ");
	      List<CurrencyRates> lrates = q.getResultList();
	      if(lrates != null && lrates.isEmpty()){
	    	  System.out.println("....created");
	    	  rates = new CurrencyRates(new Text(""));
	      }else{
	    	  System.out.println("....updated");
	    	  rates=lrates.get(0);
	      }
	      
	      rates.setRates(new Text(p_rates));
	      em.persist(rates);
		  em.close();
	   }
  }
  
  @SuppressWarnings("unchecked")
  public void calculateGain(long gain) {
	  EntityManager em = EMFService.get().createEntityManager();
	  Query q = em.createQuery("select t from ProductBet t and t.status ='Waiting'");
 	  EntityTransaction tr = null;
	  

	  List<ProductBet> bets = q.getResultList();
	  int i=0;
	  for(ProductBet bet : bets){
		  tr = em.getTransaction();
 		  tr.begin();
	      if(i%2==0){
	        bet.setStatus("Gain");
	      }else{
	        bet.setStatus("Loss");
	      }
	      em.persist(bet);
	      tr.commit();
	      i++;
	  }
	 
	  em.close();
	  waitforrefresh();
  }
  
  
  private void waitforrefresh(){
	  try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
} 
