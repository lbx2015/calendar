package net.riking.test;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


public class TestMail {
	public static void main(String[] args) {
		Email email = new SimpleEmail();
		email.setHostName("smtpcom.263xmail.com");
		email.setSmtpPort(25);
		email.setAuthenticator(new DefaultAuthenticator("dev@riking.net", "ios123a"));
		email.setSSLOnConnect(true);
		try {
			email.setFrom("dev@riking.net");
			email.setSubject("TestMail");
			email.setMsg("This is a test mail ... :-)");
			email.addTo("kilayzm@126.com");
			email.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
