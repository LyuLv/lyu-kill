package com.lyu.point.demo.config;

import com.lyu.point.demo.dao.GoodsStockMapper;
import com.lyu.point.demo.entity.GoodsStock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lyu
 * @Description:
 * @Date: Created in 14:58 2020/5/22
 * @Modified By:
 */
@Component
@Scope("singleton")
public class ApplicationInitListener implements ApplicationListener<ContextRefreshedEvent> {
    Logger LOGGER = LoggerFactory.getLogger(ApplicationInitListener.class);

    @Resource
    private GoodsStockMapper goodsStockMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 初始化时向redis中刷新全部商品
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            LOGGER.info(">>>>>>>>>>>>项目初始化完成，执行监听器中逻辑");

            //查询所有商品
            List<GoodsStock> goodsStocks = goodsStockMapper.selectAll();
            Iterator<GoodsStock> iterator = goodsStocks.iterator();
            while (iterator.hasNext()) {
                GoodsStock goodsStock = iterator.next();
                LOGGER.info("商品ID：" + goodsStock.getId()+"商品库存：" + goodsStock.getStock());

                try {
                    stringRedisTemplate.opsForValue().set(goodsStock.getId(), String.valueOf(goodsStock.getStock()), 30, TimeUnit.MINUTES);
                } catch (Exception e) {
                    LOGGER.error("当前商品ID：" + goodsStock.getId()+"商品库存：" + goodsStock.getStock() + "放入Redis缓存异常<<<<<<<<<<<<<<<<<<<<");
                    e.printStackTrace();
                }
            }
        }
    }
}
