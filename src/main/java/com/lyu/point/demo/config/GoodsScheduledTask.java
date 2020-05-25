package com.lyu.point.demo.config;

import com.lyu.point.demo.dao.GoodsStockMapper;
import com.lyu.point.demo.entity.GoodsStock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lyu
 * @Description: 定时刷新redis缓存数据
 * @Date: Created in 17:52 2020/5/22
 * @Modified By:
 */
@Component
public class GoodsScheduledTask {
    Logger LOGGER = LoggerFactory.getLogger(GoodsScheduledTask.class);

    @Resource
    private GoodsStockMapper goodsStockMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(cron = "0 */35 * * * ?")
    public void goodsScheduledTask() {
        //获取全量商品信息
        List<GoodsStock> stockList = goodsStockMapper.selectAll();

        stockList.forEach(goodsStock -> {
            LOGGER.info("定时任务--商品ID：" + goodsStock.getId()+"商品库存：" + goodsStock.getStock());
            try {
                stringRedisTemplate.opsForValue().set(goodsStock.getId(), String.valueOf(goodsStock.getStock()), 30, TimeUnit.MINUTES);
            } catch (Exception e) {
                LOGGER.info("定时任务--商品ID：" + goodsStock.getId()+"商品库存：" + goodsStock.getStock() + "放入Redis缓存异常<<<<<<<<<<<<<<<<<<<<");
                e.printStackTrace();
            }
        });
    }
}
