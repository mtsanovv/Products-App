package com.mtsan.techstore.configs;

import com.mtsan.techstore.services.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

	@Bean
	public MailService instantiateMailService(@Value("${spring.mail.host}") String mailHost, @Value("${spring.mail.username}") String mailUsername, @Value("${spring.mail.password}") String mailPassword, @Value("${spring.mail.port}") int mailPort, @Value("${spring.mail.properties.mail.smtp.starttls.enable}") boolean mailStartTlsEnable) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailHost);
		mailSender.setPort(mailPort);

		mailSender.setUsername(mailUsername);
		mailSender.setPassword(mailPassword);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", mailStartTlsEnable);

		return new MailService(mailSender, mailUsername);
	}

}
