package com.zyw.shopping.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
@Slf4j
@SuppressWarnings(value = "all")
public class RedisService {
    /**
     * 库存不足
     */
    public static final int LOW_STOCK = 0;

    /**
     * 不限库存
     */
    public static final long UNINITIALIZED_STOCK = -1L;




    @Autowired
    JedisPool jedisPool;

    //取jedis
    private Jedis getRedisCli() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (RuntimeException  e) {
            if(jedis != null ) {
                jedisPool.returnBrokenResource(jedis);
            }
        }
        return jedis;
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 执行扣库存的脚本
     */
    public static final String STOCK_LUA;

    static {
        /**
         *
         * @desc 扣减库存Lua脚本
         * 库存（stock）-1：表示不限库存
         * 库存（stock）0：表示没有库存
         * 库存（stock）大于0：表示剩余库存
         *
         * @params 库存key
         * @return
         *      0:库存不足
         *      -1:库存未初始化
         *      大于0:剩余库存（扣减之前剩余的库存）
         *      redis缓存的库存(value)是-1表示不限库存，直接返回1
         */
        StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('exists', KEYS[1]) == 1) then");
        sb.append("    local stock = tonumber(redis.call('get', KEYS[1]));");
        sb.append("    if (stock == -1) then");
        sb.append("        return 1;");
        sb.append("    end;");
        sb.append("    if (stock > 0) then");
        sb.append("        redis.call('incrby', KEYS[1], -1);");
        sb.append("        return stock;");
        sb.append("    end;");
        sb.append("    return 0;");
        sb.append("end;");
        sb.append("return -1;");
        STOCK_LUA = sb.toString();
    }

    /**
     * 设置String键值对
     * @param key
     * @param value
     * @param millis
     */
    public void put(String key, Object value, long millis) {
        redisTemplate.opsForValue().set(key, value, millis, TimeUnit.MINUTES);
    }
    public void putForHash(String objectKey, String hkey, String value) {
        redisTemplate.opsForHash().put(objectKey, hkey, value);
    }
    public <T> T get(String key, Class<T> type) {
        return (T) redisTemplate.boundValueOps(key).get();
    }
    public void remove(String key) {
        redisTemplate.delete(key);
    }
    public boolean expire(String key, long millis) {
        return redisTemplate.expire(key, millis, TimeUnit.MILLISECONDS);
    }
    public boolean persist(String key) {
        return redisTemplate.hasKey(key);
    }
    public String getString(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }
    public Integer getInteger(String key) {
        return (Integer) redisTemplate.opsForValue().get(key);
    }
    public Long getLong(String key) {
        return (Long) redisTemplate.opsForValue().get(key);
    }
    public Date getDate(String key) {
        return (Date) redisTemplate.opsForValue().get(key);
    }

    /**
     * 对指定key的键值减一
     * @param key
     * @return
     */
//    public Long decrBy(String key) {
//        return redisTemplate.opsForValue().decrement(key);
//    }
//
//    public long decrByUntil0Lua(String key, long value) {
//        if (value<0) return -1;
//        Jedis jedis = this.getRedisCli();
//        Long leftValue;
//        try {
//            String script = " local leftvalue = redis.call('get', KEYS[1]); "
//                    + " if ARGV[1] - leftvalue > 0 then return nil; else "
//                    + " return redis.call('decrby', KEYS[1], ARGV[1]); end; ";
//            System.out.println(script);
//            leftValue = (Long) jedis.eval(script, 1, key, "" + value);
//        } finally {
//            jedis.close();
//        }
//        if (leftValue == null)
//            return -1;
//        return leftValue;
//    }

    /**
     * 扣库存
     *
     * @param key 库存key
     * @return 扣减之前剩余的库存【0:库存不足; -1:库存未初始化; 大于0:扣减库存之前的剩余库存】
     */
    public Long decrstock(String key) {
        Jedis jedis = this.getRedisCli();
        jedis.select(1);
        try {
            // 脚本里的KEYS参数
            List<String> keys = new ArrayList<>();
            keys.add(key);
//        System.out.println("keys:"+keys);
            // 脚本里的ARGV参数
            List<String> args = new ArrayList<>();
//        System.out.println(STOCK_LUA);

            Long result = (Long) jedis.eval(STOCK_LUA, keys, args);
            return result;
        }catch (Exception e){
            log.error("扣减库存失败，错误信息："+e.getMessage());
            return -1l;
        }finally {
            jedis.close();

        }


    }

}