package com.zyw.shopping;

import com.zyw.shopping.redis.RedisService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan(basePackages = {"com.zyw.shopping.dao.mapper"})
@SpringBootApplication
public class ShoppingApplication implements ApplicationRunner {
    @Autowired
    private RedisService redisService;

    public static void main(String[] args) {
        SpringApplication.run(ShoppingApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        redisService.put("iphone11", 100, 20);
//        redisService.put("macbook", 100, 20);

    }
}
