package com.zyw.shopping.rabbitmq;


import com.zyw.shopping.config.MyRabbitMQConfig;
import com.zyw.shopping.entity.Order;
import com.zyw.shopping.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class MQOrderService {
    @Autowired
    private OrderService orderService;


    /**
     * 监听订单消息队列，并消费
     *
     * @param order
     */
    @RabbitHandler
    @RabbitListener(queues = MyRabbitMQConfig.ORDER_QUEUE)
    public void createOrder(Order order) {
        log.info("收到订单消息，订单用户为：{}，商品名称为：{}", order.getOrderName(), order.getOrderUser());
        /**
         * 调用数据库orderService创建订单信息
         */
        orderService.createOrder(order);
    }
}