package com.zyw.shopping.service;

import com.zyw.shopping.entity.Stock;

public interface StockService  {
    void decrByStock(String stockName);
    Integer selectByExample(String stockName);
}
