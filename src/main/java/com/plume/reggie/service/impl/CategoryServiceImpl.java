package com.plume.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plume.reggie.common.CustomException;
import com.plume.reggie.entity.Category;
import com.plume.reggie.entity.Dish;
import com.plume.reggie.entity.Setmeal;
import com.plume.reggie.mapper.CategoryMapper;
import com.plume.reggie.service.CategoryService;
import com.plume.reggie.service.DishService;
import com.plume.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类,需要判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        // 查询分类是关联菜品,是就抛出一个业务异常
        if (count1 > 0){
            // 已经关联
            throw new CustomException("当前分类关联了菜品,不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        // 查询分类是关联套餐,是就抛出一个业务异常
        if (count2 > 0){
            //已经关联
            throw new CustomException("当前分类关联了套餐,不能删除");
        }
        // 正常化删除
        super.removeById(id);
    }
}
