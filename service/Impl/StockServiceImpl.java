package com.zyw.shopping.service.Impl;

import com.zyw.shopping.dao.mapper.StockMapper;
import com.zyw.shopping.entity.Stock;
import com.zyw.shopping.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional
@Slf4j
public class StockServiceImpl implements StockService {
    @Autowired
    private StockMapper stockMapper;

    // 秒杀商品后减少库存
    @Override
    public void decrByStock(String stockName) {
        Example example = new Example(Stock.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", stockName);
        synchronized (StockServiceImpl.class) {
            List<Stock> stocks = stockMapper.selectByExample(example);
//            System.out.println(stocks);
            if (!CollectionUtils.isEmpty(stocks)) {
                Stock stock = stocks.get(0);
                stock.setStock(stock.getStock() - 1);
                int i = stockMapper.updateByPrimaryKey(stock);
//                System.out.println("stockchange:" + i);
            }
        }
    }

    // 秒杀商品前判断是否有库存
    @Override
    public Integer selectByExample(String stockName) {
        Example example = new Example(Stock.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", stockName);
        List<Stock> stocks = stockMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(stocks)) {
            return stocks.get(0).getStock().intValue();
        }
        return 0;
    }
}