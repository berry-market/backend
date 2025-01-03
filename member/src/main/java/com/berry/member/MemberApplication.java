package com.berry.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
	"com.berry.member",
	"com.berry.common"
})
public class MemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemberApplication.class, args);
	}

}
