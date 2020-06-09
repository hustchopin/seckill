package com.zyw.shopping.service.Impl;

import com.zyw.shopping.dao.mapper.OrderMapper;
import com.zyw.shopping.entity.Order;
import com.zyw.shopping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public int createOrder(Order order) {
        int insertSelective = orderMapper.insertSelective(order);
        return insertSelective;
    }
}
