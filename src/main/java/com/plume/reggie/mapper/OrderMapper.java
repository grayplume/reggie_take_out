package com.plume.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plume.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
