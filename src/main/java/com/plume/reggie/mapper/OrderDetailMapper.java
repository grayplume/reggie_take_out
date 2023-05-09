package com.plume.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plume.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
