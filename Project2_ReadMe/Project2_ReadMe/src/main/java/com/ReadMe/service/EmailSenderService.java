package com.ReadMe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ReadMe.Main;

@Service
public class EmailSenderService {
	@Autowired
	private JavaMailSender mailSender;
	
	/*
	 *  GMAIL ACCOUNT CREDENTIALS
	 *  Username: readme.revature@gmail.com
	 *  Password: P4$$w0rd
	 */
	public void sendEmail(String toEmail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("readme.revature@gmail.com");
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
		Main.log.info("Mail sent successfully");
	}

}
