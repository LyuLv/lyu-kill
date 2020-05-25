package com.lyu.point.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.lyu.point.demo.dao.OrderListMapper;
import com.lyu.point.demo.entity.OrderList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @Author: Lyu
 * @Description:
 * @Date: Created in 14:50 2020/5/22
 * @Modified By:
 */
@Service
public class OrderListService {
    Logger LOGGER = LoggerFactory.getLogger(OrderListService.class);

    @Resource
    private OrderListMapper orderListMapper;

    /**
     * 下单成功，生成订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderInsert(OrderList order) {
        if (order == null) {
            throw new NullPointerException();
        }

        //获取当前用户，当前商品是否已存在
        OrderList orderList = orderListMapper.selectByWhere(order);
        if (orderList == null) {
            order.setId(String.valueOf(UUID.randomUUID()));
            order.setStockNumber(1L);
            orderListMapper.insert(order);
        } else {
            orderList.setStockNumber(orderList.getStockNumber() + 1);
            orderListMapper.updateByPrimaryKey(orderList);
        }

        LOGGER.info("订单生成成功 {}", JSONObject.toJSONString(order));
    }
}
