package com.zyw.shopping.controller;

import com.zyw.shopping.config.MyRabbitMQConfig;
import com.zyw.shopping.entity.Order;
import com.zyw.shopping.redis.DistributedLocker;
import com.zyw.shopping.redis.RedisService;
import com.zyw.shopping.service.OrderService;
import com.zyw.shopping.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
public class SecController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisService redisService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private StockService stockService;
    @Autowired
    private DistributedLocker distributedLocker;

    //分布式锁key
    public static final String secLock = "zyw_shopping_secLock";
    /**
     * 使用redis+消息队列进行秒杀实现
     *
     * @param username
     * @param stockName
     * @return
     */
    @GetMapping(value = "/sec", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String sec(@RequestParam(value = "username") String username, @RequestParam(value = "stockName") String stockName) {

        log.info("参加秒杀的用户是：{}，秒杀的商品是：{}", username, stockName);
        String message = null;

        //调用redis给相应商品库存量减一
        Long decrstock = redisService.decrstock(stockName);
        if (decrstock > 0) {

//                redisService.decrBy(stockName);
                /**
                 * 说明该商品的库存量有剩余，可以进行下订单操作
                 */
                //log.info("用户：{}秒杀该商品：{}库存有余，可以进行下订单操作", username, stockName);
                //发消息给库存消息队列，将库存数据减一
                rabbitTemplate.convertAndSend(MyRabbitMQConfig.STORY_EXCHANGE, MyRabbitMQConfig.STORY_ROUTING_KEY, stockName);

                //发消息给订单消息队列，创建订单
                Order order = new Order();
                order.setOrderName(stockName);
                order.setOrderUser(username);
                rabbitTemplate.convertAndSend(MyRabbitMQConfig.ORDER_EXCHANGE, MyRabbitMQConfig.ORDER_ROUTING_KEY, order);
                message = "用户" + username + "秒杀" + stockName + "成功";
                log.info("用户：{}秒杀该商品：{}库存有余，已下订单", username, stockName);
            } else if(decrstock == 0){
                /**
                 * 说明该商品的库存量没有剩余，直接返回秒杀失败的消息给用户
                 */
                log.info("用户：{}秒杀时商品的库存量没有剩余,秒杀结束", username);
                message = "用户：" + username + "商品的库存量没有剩余,秒杀结束";
            }else{
            log.info("用户：{}秒杀活动未开始", username);
            message = "用户：" + username + "商品的秒杀活动未开始";
        }
            return message;
        }

}
