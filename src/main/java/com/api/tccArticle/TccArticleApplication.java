package com.api.tccArticle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TccArticleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TccArticleApplication.class, args);
	}

}
