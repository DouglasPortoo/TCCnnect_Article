package com.api.tccArticle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TccArticleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TccArticleApplication.class, args);
	}

}
