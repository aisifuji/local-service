package com.herocheer.zhsq.localservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.herocheer.zhsq.localservice.impl.mapper"})
public class LocalServiceStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocalServiceStarterApplication.class, args);
    }

}
