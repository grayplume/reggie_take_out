package com.plume.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.plume.reggie.dto.DishDto;
import com.plume.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    // 新增菜品同时插入对应的口味数据,需要操作两个标dish和dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    // 根据id查询菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);

    // 更新菜品信息和对应的口味信息
    void updateWithFlavor(DishDto dishDto);

}
