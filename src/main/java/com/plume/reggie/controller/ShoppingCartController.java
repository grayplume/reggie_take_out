package com.plume.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.plume.reggie.common.BaseContext;
import com.plume.reggie.common.R;
import com.plume.reggie.entity.ShoppingCart;
import com.plume.reggie.service.ShoppingCatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.LongAccumulator;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCatService shoppingCatService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据:{}",shoppingCart);

        // 设置用户id,指定哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        // 查询当前菜品或套餐是否在购物车之中
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        if (dishId != null){
            // 添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            // 添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //SQL:select * from shoping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart catServiceOne = shoppingCatService.getOne(queryWrapper);

        if (catServiceOne != null){
            // 如果已经存在,数量加一
            Integer number = catServiceOne.getNumber();
            catServiceOne.setNumber(number+1);
            shoppingCatService.updateById(catServiceOne);
        }else {
            // 如果不存在,添加到购物车
            shoppingCart.setNumber(1);
            shoppingCatService.save(shoppingCart);
            catServiceOne = shoppingCart;
        }

        return R.success(catServiceOne);
    }

    /**
     * 购物车回显
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        // 进行用户对比
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        // 查询并返回即可

        List<ShoppingCart> list = shoppingCatService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 购物车清空
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){

        // 进行用户对比
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        // 删除即可
        shoppingCatService.remove(queryWrapper);

        return R.success("清空成功");
    }

}
