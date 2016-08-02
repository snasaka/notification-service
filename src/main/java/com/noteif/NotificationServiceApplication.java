package com.noteif;

import config.XmppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(XmppConfig.class)
@SpringBootApplication
public class NotificationServiceApplication implements CommandLineRunner {

	@Autowired
	private XmppAuthenticationProvider xmppAuthenticationProvider;

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		xmppAuthenticationProvider.connectToServer();
	}
}
