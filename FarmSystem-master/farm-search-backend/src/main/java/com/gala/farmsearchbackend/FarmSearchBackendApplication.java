package com.gala.farmsearchbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gala.farmsearchbackend.mapper")
public class FarmSearchBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmSearchBackendApplication.class, args);
    }

}
