package com.suncd.conn.ibmmq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.suncd.conn.ibmmq.dao")
@SpringBootApplication
public class ConnIbmmqBoot {

    public static void main(String[] args) {
        SpringApplication.run(ConnIbmmqBoot.class, args);

    }
}
