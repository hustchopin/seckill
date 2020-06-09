package com.zyw.shopping.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
public class MyRabbitMQConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;



    //库存交换机
    public static final String STORY_EXCHANGE = "STORY_EXCHANGE";

    //订单交换机
    public static final String ORDER_EXCHANGE = "ORDER_EXCHANGE";

    //库存队列
    public static final String STORY_QUEUE = "STORY_QUEUE";

    //订单队列
    public static final String ORDER_QUEUE = "ORDER_QUEUE";

    //库存路由键
    public static final String STORY_ROUTING_KEY = "STORY_ROUTING_KEY";

    //订单路由键
    public static final String ORDER_ROUTING_KEY = "ORDER_ROUTING_KEY";
//    @Bean
//    public ConnectionFactory connectionFactory() {
//
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
//        connectionFactory.setUsername(username);
//        connectionFactory.setPassword(password);
//        //connectionFactory.setVirtualHost(virtualHosthost);
//        return connectionFactory;
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate() {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory());
//        //template.setDefaultReceiveQueue(queue);//设置默认接收队列
//        return template;
//    }
//
//    @Bean(name="defauilTemplate")
//    public RabbitTemplate rabbitTemplateDefault() {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory());
//        template.setDefaultReceiveQueue("fastSending");//设置默认接收队列
//        return template;
//    }



    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    //创建库存交换机
    @Bean
    public Exchange getStoryExchange() {
        return ExchangeBuilder.directExchange(STORY_EXCHANGE).durable(true).build();
    }
    //创建库存队列
    @Bean
    public Queue getStoryQueue() {
        return new Queue(STORY_QUEUE);
    }
    //库存交换机和库存队列绑定
    @Bean
    public Binding bindStory() {
        return BindingBuilder.bind(getStoryQueue()).to(getStoryExchange()).with(STORY_ROUTING_KEY).noargs();
    }
    //创建订单队列
    @Bean
    public Queue getOrderQueue() {
        return new Queue(ORDER_QUEUE);
    }
    //创建订单交换机
    @Bean
    public Exchange getOrderExchange() {
        return ExchangeBuilder.directExchange(ORDER_EXCHANGE).durable(true).build();
    }
    //订单队列与订单交换机进行绑定
    @Bean
    public Binding bindOrder() {
        return BindingBuilder.bind(getOrderQueue()).to(getOrderExchange()).with(ORDER_ROUTING_KEY).noargs();
    }

}