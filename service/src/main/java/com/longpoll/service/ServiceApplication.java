package com.longpoll.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ServletComponentScan({
	"com.longpoll.service.nothread.servlet"})
@ComponentScan({
	"com.longpoll.service.common",
	"com.longpoll.service.nothread.servlet",
	"com.longpoll.service.nothread.logic",
	"com.longpoll.service.thread.controller",
	"com.longpoll.service.thread.logic"})
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Waiting for callers.");

        };
    }

}
