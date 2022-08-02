package com.zqh.jndi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import lombok.extern.slf4j.Slf4j;

@ImportResource(value= {"classpath:config/jpa-context.xml"})
@SpringBootApplication(scanBasePackages = "com.zqh.jndi")
@Slf4j
public class SpringBootJNDIApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJNDIApplication.class, args);
	}
}
