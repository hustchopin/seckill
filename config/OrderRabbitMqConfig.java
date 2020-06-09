//package com.zyw.shopping.config;
//
//import com.rabbitmq.client.impl.Environment;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class OrderRabbitmqConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(OrderRabbitmqConfig.class);
//
////
////    @Autowired
////    private Environment env;
//
//    /**
//     * channel链接工厂
//     */
//    @Autowired
//    private CachingConnectionFactory connectionFactory;
//
//    /**
//     * 监听器容器配置
//     */
//    @Autowired
//    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;
//
//    /**
//     * 声明rabbittemplate
//     * @return
//     */
//    @Bean
//    public RabbitTemplate rabbitTemplate(){
//        //消息发送成功确认，对应application.properties中的spring.rabbitmq.publisher-confirms=true
//        connectionFactory.setPublisherConfirms(true);
//        //消息发送失败确认，对应application.properties中的spring.rabbitmq.publisher-returns=true
//        connectionFactory.setPublisherReturns(true);
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        //设置消息发送格式为json
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//        rabbitTemplate.setMandatory(true);
//        //消息发送到exchange回调 需设置：spring.rabbitmq.publisher-confirms=true
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            @Override
//            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//                logger.info("消息发送成功:correlationData({}),ack({}),cause({})",correlationData,ack,cause);
//            }
//        });
//        //消息从exchange发送到queue失败回调  需设置：spring.rabbitmq.publisher-returns=true
//        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
//            @Override
//            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//                logger.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
//            }
//        });
//        return rabbitTemplate;
//    }
//
////    //---------------------------------------订单队列------------------------------------------------------
////
////    /**
////     * 声明订单队列的交换机
////     * @return
////     */
////    @Bean("orderTopicExchange")
////    public TopicExchange orderTopicExchange(){
////        //设置为持久化 不自动删除
////        return new TopicExchange(env.getProperty("order.mq.exchange.name"),true,false);
////    }
////
////    /**
////     * 声明订单队列
////     * @return
////     */
////    @Bean("orderQueue")
////    public Queue orderQueue(){
////        return new Queue(env.getProperty("order.mq.queue.name"),true);
////    }
////
////    /**
////     * 将队列绑定到交换机
////     * @return
////     */
////    @Bean
////    public Binding simpleBinding(){
////        return BindingBuilder.bind(orderQueue()).to(orderTopicExchange()).with(env.getProperty("order.mq.routing.key"));
////    }
////
////    /**
////     * 注入订单对列消费监听器
////     */
////    @Autowired
////    private OrderListener orderListener;
////
////    /**
////     * 声明订单队列监听器配置容器
////     * @return
////     */
////    @Bean("orderListenerContainer")
////    public SimpleMessageListenerContainer orderListenerContainer(){
////        //创建监听器容器工厂
////        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
////        //将配置信息和链接信息赋给容器工厂
////        factoryConfigurer.configure(factory,connectionFactory);
////        //容器工厂创建监听器容器
////        SimpleMessageListenerContainer container = factory.createListenerContainer();
////        //指定监听器
////        container.setMessageListener(orderListener);
////        //指定监听器监听的队列
////        container.setQueues(orderQueue());
////        return container;
////    }
//
//}
//
