package com.sbd.quartz_scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class QuartzSchedulerApplication {

	/*@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(QuartzSchedulerApplication.class);
	}*/


	public static void main(String[] args) {
		SpringApplication.run(QuartzSchedulerApplication.class, args);
	}

}
