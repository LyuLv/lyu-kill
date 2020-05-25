package com.lyu.point.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.lyu.point.demo.dao.GoodsStockMapper;
import com.lyu.point.demo.entity.GoodsStock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author: Lyu
 * @Description:
 * @Date: Created in 14:29 2020/5/22
 * @Modified By:
 */
@Service
public class GoodsStockService {
    Logger LOGGER = LoggerFactory.getLogger(GoodsStockMQService.class);
    @Resource
    private GoodsStockMapper stockMapper;

    /**
     * 根据商品id扣减数据库库存
     */
    @Transactional(rollbackFor = Exception.class)
    public void decrStock(String id) {
        if (StringUtils.isBlank(id)) {
            throw new NullPointerException();
        }
        GoodsStock goodsStock = stockMapper.selectByPrimaryKey(id);
        goodsStock.setStock(goodsStock.getStock() - 1);
        stockMapper.updateByPrimaryKey(goodsStock);
        LOGGER.info("商品库存扣减成功>>>>>>>>> {}", JSONObject.toJSONString(goodsStock));
    }
}
