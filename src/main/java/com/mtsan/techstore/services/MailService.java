package com.mtsan.techstore.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

public class MailService {

	private final JavaMailSender javaMailSender;

	private final String springMailUsername;

	public MailService(JavaMailSender javaMailSender, String springMailUsername) {
		this.javaMailSender = javaMailSender;
		this.springMailUsername = springMailUsername;
	}

	@Async
	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("TechStore Mail <" + springMailUsername + ">");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		javaMailSender.send(message);
	}
}