package com.ucas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableScheduling
@MapperScan("com.ucas.mapper")
public class UcasApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcasApplication.class, args);
    }

}
