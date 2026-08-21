package com.cheny.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheny.entity.Employee;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cheny.vo.EmployeeVo;

public interface EmployeeMapper extends BaseMapper<Employee> {
    Page<EmployeeVo> selectPageVo(Page<EmployeeVo> pageParam, LambdaQueryWrapper<Employee> queryWrapper);
}
