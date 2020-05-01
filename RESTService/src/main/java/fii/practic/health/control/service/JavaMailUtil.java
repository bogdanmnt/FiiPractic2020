package fii.practic.health.control.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class JavaMailUtil {

	public void sendMail(String recepient, String msg, Object obj) throws Exception {
		System.out.println("Ma pregatesc sa trimit mail");
		Properties properties =new Properties();
		
		properties.put("mail.smtp.auth",true);
		properties.put("mail.smtp.starttls.enable",true);
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.host","smtp.gmail.com");
		properties.put("mail.smtp.port","587");
		
		String myAccountEmail="bogdan.test.fii.20@gmail.com";
		String password="parola123A";
		
		Session session= Session.getInstance(properties,new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(myAccountEmail, password);
			}
		});
		
		
		Message message= prepareMessage(session, myAccountEmail, recepient, msg, obj);
		
		Transport.send(message);
		
		System.out.println("Mesaj trimis !");
	}
	
	

	private Message prepareMessage(Session session, String myAccountEmail, String recepient,String msg,Object obj) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("Am primit cererea ta!");
			message.setContent(getRezultat(msg, obj),"text/html");
			return message;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return null;
	}
	
	
	
	String getRezultat(String textParam, Object obj) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		 String content = textParam;
	        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
	        Matcher m = pattern.matcher(content);

	        StringBuffer sb = new StringBuffer();

	        while (m.find())
	            m.appendReplacement(sb, replace(m.group(1), obj));

	        m.appendTail(sb);

	     
	        String result= sb.toString();
	
		return result;
	
	}
	
	
	String replace(String methodName, Object a)throws SecurityException, NoSuchMethodException, IllegalArgumentException,
  IllegalAccessException, InvocationTargetException {
	    Method m = a.getClass().getMethod(methodName, new Class[] {});
      Object ret = m.invoke(a, new Object[] {});
     
      return ret.toString();
	}
	
	
	
	
	
	
	
}
