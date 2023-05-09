package com.plume.reggie.dto;

import com.plume.reggie.entity.Setmeal;
import com.plume.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
