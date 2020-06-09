package com.zyw.shopping.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
@Slf4j
public class RedisConfig extends CachingConfigurerSupport {
    /**
     * redis连接密码
     */
    @Value("${spring.redis.password}")
    private String pwssword;
    /**
     * redis主机
     */
    @Value("${spring.redis.host}")
    private String host;
    /**
     * redis端口
     */
    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;

    /**
     * 单机模式自动装配,生成环境建议使用哨兵(可以理解成主从的升级版本)或者集群
     * @return
     */
    @Bean
    RedissonClient redissonSingle() {
        Config config = new Config();

        if(pwssword.isEmpty() || "".equals(pwssword)){
            config.useSingleServer()
                    .setAddress(new StringBuffer("redis://").append(host).append(":").append(port).toString())
                    .setDatabase(1);
        }else{
            config.useSingleServer()
                    .setAddress(new StringBuffer("redis://").append(host).append(":").append(port).toString())
                    .setDatabase(1)
                    .setPassword(pwssword);
        }
        return Redisson.create(config);
    }

    // 配置redis得配置详解
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public JedisPool redisPoolFactory() {
        log.info("JedisPool注入成功！！");
        log.info("redis地址：" + host + ":" + port);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(50);
        jedisPoolConfig.setMaxWaitMillis(10000);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        if(pwssword ==null || pwssword.equals("")){
            return new JedisPool(jedisPoolConfig, host, port,  timeout);
        }else{
            return new JedisPool(jedisPoolConfig, host, port, timeout,pwssword);
        }
    }
}