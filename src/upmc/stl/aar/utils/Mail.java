package upmc.stl.aar.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Query;

import upmc.stl.aar.model.Player;
import upmc.stl.aar.model.ProductBet;

public class Mail {
	
	/**
	 * Sends a mail to the player updating him about his current bets 
	 * @param player Player
	 * @param bet	 ProductBet
	 */
	public static void sendMail(Player player, ProductBet bet){
		
		try {
			Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            
			Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("admin@m2stlbetapp.appspotmail.com", "m2stlbetapp"));
            msg.addRecipient(Message.RecipientType.TO,new InternetAddress(player.getPlayerEmail(), ""));
            msg.setSubject("[M2stlbetapp] Bet Information Update (" + bet.getQuantity() + ") " + bet.getStatus());
            msg.setText("Thank you for playing on the U.G.L.Y Bet.ty application !"
            		+ "\n\nHere are some update information about the recent " 
            		+ bet.getType() 
            		+ " bet you have made."
            		+ " Just to remind you, it concerned " 
            		+ bet.getQuantity() 
            		+ " " 
            		+ bet.getCurrency() 
            		+ " which you evaluated to a rate of : " 
            		+ bet.getRate() + "."
            		+ "\n\nYou created the bet on the date of " 
            		+ bet.getBetDate() 
            		+ "with an ending estimation of " 
            		+ bet.getTerm() 
            		+ " and therefore it will end on " 
            		+ bet.getTermDate() 
            		+ ".\n\nThank you for your support. We hope to see you again soon" 
            		+ "\n\nGet back to the website : http://m2stlbetapp.appspot.com/");
            Transport.send(msg);
		} catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
        }
		
	}

}
