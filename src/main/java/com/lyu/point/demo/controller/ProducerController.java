package com.lyu.point.demo.controller;

import com.lyu.point.demo.config.RabbitMQConfig;
import com.lyu.point.demo.entity.OrderList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



/**
 * @Author: Lyu
 * @Description:
 * @Date: Created in 17:13 2020/5/21
 * @Modified By:
 */
@RestController
public class ProducerController {
    Logger LOGGER = LoggerFactory.getLogger(ProducerController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping(name = "秒杀", value = {"/kill"})
    @ResponseBody
    public String kill(@RequestParam(value = "name") String name, @RequestParam(value = "id") String id) {
        LOGGER.info("参加秒杀的用户是：{}，秒杀的商品是：{}", name, id);
        String message = "";
        //调用redis给该商品减一
        Long num = stringRedisTemplate.opsForValue().decrement(id);
        if (num >= 0) {
            //说明该商品的库存量有剩余，可以进行下订单操作
            LOGGER.info("用户：{}秒杀该商品：{}库存有余，可以进行下订单操作", name, id);

            //给库存队列发消息，减库存
            rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_EXCHANGE_NAME, RabbitMQConfig.STOCK_ROUTING_KEY, id);

            //给订单消息队列，生成订单
            OrderList order = new OrderList();
            order.setUserName(name);
            order.setStockId(id);
            rabbitTemplate.convertAndSend(RabbitMQConfig.OEDER_EXCHANGE_NAME, RabbitMQConfig.OEDER_ROUTING_KEY, order);
            message = "用户" + name + "秒杀" + id + "成功";
        } else {
            LOGGER.info("用户：{}秒杀时商品的库存量没有剩余,秒杀结束", name);
            message = name + "商品的库存量没有剩余,秒杀结束";
        }
        return message;
    }



}
