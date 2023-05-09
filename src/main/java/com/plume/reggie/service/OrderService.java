package com.plume.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.plume.reggie.entity.Orders;

import java.util.Map;

public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);

    void updateStatus(Map<String, Object> params);
}
