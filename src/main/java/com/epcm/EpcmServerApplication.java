package com.epcm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ServletComponentScan("com.epcm")
@EnableTransactionManagement
@MapperScan(basePackages = {"com.epcm.dao"})
public class EpcmServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpcmServerApplication.class, args);
	}

}
