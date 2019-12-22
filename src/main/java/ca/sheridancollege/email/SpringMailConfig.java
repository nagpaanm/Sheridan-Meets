package ca.sheridancollege.email;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


//on email account - turn off 2-step validation
//on email account - turn on less secure app access

@Configuration
public class SpringMailConfig {

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = 
				new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		
		mailSender.setUsername("java3test123123@gmail.com");
		mailSender.setPassword("Mrpandu123!");
		
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol",  "smtp");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.auth",  "true");
		props.put("mail.smtp.starttls.enable",  "true");
		props.put("mail.debug",  "true");
		return mailSender;
		
	}
}
