package com.plume.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plume.reggie.entity.ShoppingCart;
import com.plume.reggie.mapper.ShoppingCartMapper;
import com.plume.reggie.service.ShoppingCatService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCatService {


}
