package com.lyu.point.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lyu.point.demo.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lyu
 * @Description: 库存MQ实现
 * @Date: Created in 14:26 2020/5/22
 * @Modified By:
 */
@Service
public class GoodsStockMQService {
    Logger LOGGER = LoggerFactory.getLogger(GoodsStockMQService.class);

    @Autowired
    private GoodsStockService goodsStockService;

    /**
     * 监听库存消息队列，并消费
     * @param id
     */
    @RabbitListener(queues = RabbitMQConfig.STOCK_QUEUE_NAME)
    public void decrStockMessage(String id) {
        LOGGER.info("库存消息队列--消费 {}", id);

        //并扣减数据库库存
        goodsStockService.decrStock(id);
    }


}
