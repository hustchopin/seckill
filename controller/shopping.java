package com.zyw.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class shopping {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/insert")
    public String insert(@RequestParam("id")String id,@RequestParam("name")String name,
                           @RequestParam("age")String age){
        String sql = "insert into student (id,name,age) values(?,?,?)";
        jdbcTemplate.update(sql,id,name,age);
        return "true";
    }

    @RequestMapping("/select")
    public List<Map<String, Object>> select(){
        String sql = "select * from student";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }


}
