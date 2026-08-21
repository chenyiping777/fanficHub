package com.cheny.service;
import com.baomidou.mybatisplus.spring.service.IService;
import com.cheny.dto.EmployeeDto;
import com.cheny.dto.EmployeeLoginDto;
import com.cheny.dto.PasswordEditDto;
import com.cheny.entity.Employee;
import com.cheny.query.EmployeeQuery;
import com.cheny.vo.EmployeeVo;
import com.cheny.vo.PageVo;

/**
* @author Mlpnk
* @description 针对表【employee(平台后台员工表)】的数据库操作Service
* @createDate 2026-08-15 11:56:10
*/
public interface EmployeeService extends IService<Employee> {

    //登录
    Employee login(EmployeeLoginDto employeeLoginDto);
    //保存员工
    void saveEmployee(EmployeeDto employeeDto);

    //分页查询员工
    PageVo<EmployeeVo> getEmployeePage(EmployeeQuery employeeQuery);

    //根据id查询员工
    EmployeeVo getVoById(Long id);

    //修改员工状态
    void updateStatus(Integer status, Long id);

    //修改员工信息
    void updateEmployee(EmployeeDto employeeDto);

    //修改密码
    void updatePassword(PasswordEditDto passwordEditDto, Long userId);


}
