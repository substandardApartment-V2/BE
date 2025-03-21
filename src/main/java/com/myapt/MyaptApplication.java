package com.myapt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 스케줄링 활성화
public class MyaptApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyaptApplication.class, args);
	}

}
