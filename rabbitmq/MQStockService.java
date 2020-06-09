package com.zyw.shopping.rabbitmq;

import com.zyw.shopping.config.MyRabbitMQConfig;
import com.zyw.shopping.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class MQStockService {
    @Autowired
    private StockService stockService;
    /**
     * 监听库存消息队列，并消费
     * @param stockName
     */
    @RabbitListener(queues = MyRabbitMQConfig.STORY_QUEUE)
    public void decrByStock(String stockName) {
        log.info("库存消息队列收到的消息商品信息是：{}", stockName);
        /**
         * 调用数据库service给数据库对应商品库存减一
         */
        stockService.decrByStock(stockName);
    }
}