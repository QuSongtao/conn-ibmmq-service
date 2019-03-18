package com.suncd.conn.ibmmq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.suncd.conn.ibmmq.dao")
@SpringBootApplication
@EnableScheduling
@EnableJms
public class ConnIbmmqBoot {
    public static void main(String[] args) {
        SpringApplication.run(ConnIbmmqBoot.class, args);
    }
}
