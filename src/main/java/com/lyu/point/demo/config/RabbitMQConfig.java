package com.lyu.point.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lyu
 * @Description:
 * @Date: Created in 16:49 2020/5/21
 * @Modified By:
 */
@Configuration
public class RabbitMQConfig {
    /**
     * 库存队列名称
     */
    public final static String STOCK_QUEUE_NAME = "STOCK_QUEUE";
    /**
     * 订单队列名称
     */
    public final static String OEDER_QUEUE_NAME = "OEDER_QUEUE";
    /**
     * 库存消息交换器名称
     */
    public final static String STOCK_EXCHANGE_NAME = "STOCK_EXCHANGE";
    /**
     * 订单消息交换器名称
     */
    public final static String OEDER_EXCHANGE_NAME = "OEDER_EXCHANGE";
    /**
     * 库存路由key
     */
    public final static String STOCK_ROUTING_KEY = "STOCK_ROUTING_KEY";
    /**
     * 订单路由key
     */
    public final static String OEDER_ROUTING_KEY = "OEDER_ROUTING_KEY";

    /**
     * 创建库存队列
     * @return
     */
    @Bean
    public Queue getStockQueue() {
        return new Queue(STOCK_QUEUE_NAME);
    }

    /**
     * 创建一个库存交换器
     * @return
     */
    @Bean
    public Exchange getStockExchange() {
        return ExchangeBuilder.directExchange(STOCK_EXCHANGE_NAME).durable(true).build();
    }

    /**
     * 创建订单队列
     * @return
     */
    @Bean
    public Queue getOrderQueue() {
        return new Queue(OEDER_QUEUE_NAME);
    }

    /**
     * 创建一个订单交换器
     * @return
     */
    @Bean
    public Exchange getOrderExchange() {
        return ExchangeBuilder.directExchange(OEDER_EXCHANGE_NAME).durable(true).build();
    }

    /**
     * 库存交换器和队列绑定
     * @return
     */
    @Bean
    public Binding bindStock() {
        return BindingBuilder.bind(getStockQueue()).to(getStockExchange()).with(STOCK_ROUTING_KEY).noargs();
    }

    /**
     * 订单交换器和队列绑定
     * @return
     */
    @Bean
    public Binding bindOrder() {
        return BindingBuilder.bind(getOrderQueue()).to(getOrderExchange()).with(OEDER_ROUTING_KEY).noargs();
    }

}
