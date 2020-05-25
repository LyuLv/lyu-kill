package com.lyu.point.demo.service;

import com.lyu.point.demo.config.RabbitMQConfig;
import com.lyu.point.demo.entity.OrderList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lyu
 * @Description:
 * @Date: Created in 14:50 2020/5/22
 * @Modified By:
 */
@Service
public class OrderListMQService {
    Logger LOGGER = LoggerFactory.getLogger(OrderListMQService.class);

    @Autowired
    private OrderListService orderListService;

    @RabbitListener(queues = RabbitMQConfig.OEDER_QUEUE_NAME)
    public void creatOrder(OrderList order) {
        LOGGER.info("收到订单消息，订单用户为：{}，商品为：{}", order.getUserName(), order.getStockId());

        orderListService.orderInsert(order);
    }
}
