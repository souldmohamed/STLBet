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
	
	public static void sendMail(Player player, ProductBet bet){
		
		try {
			Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            
			Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("admin@m2stlbetapp.appspotmail.com", "m2stlbetapp"));
            msg.addRecipient(Message.RecipientType.TO,new InternetAddress(player.getPlayerEmail(), ""));
            msg.setSubject("[m2stlbetapp] bet (" + bet.getQuantity() + ") " + bet.getStatus());
            msg.setText("Infos :"
            		+ "\ntype : " + bet.getType()
            		+ "\nquantity : " + bet.getQuantity()
            		+ "\ncurrency : " + bet.getCurrency()
            		+ "\nrate : " + bet.getRate()
            		+ "\nbet date : " + bet.getBetDate()
            		+ "\nterm : " + bet.getTerm()
            		+ "\nterm date  : " + bet.getTermDate()
            		+ "\nstatus : " + bet.getStatus());
            Transport.send(msg);
		} catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
        }
		
	}

}
