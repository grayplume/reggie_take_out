package com.plume.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plume.reggie.entity.Employee;
import com.plume.reggie.mapper.EmployeeMapper;
import com.plume.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmplyeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
