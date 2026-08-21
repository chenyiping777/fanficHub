package com.cheny.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheny.constant.MessageConstant;
import com.cheny.constant.StatusConstant;
import com.cheny.dto.EmployeeDto;
import com.cheny.dto.EmployeeLoginDto;
import com.cheny.dto.PasswordEditDto;
import com.cheny.entity.Employee;
import com.cheny.exception.AddEmployeeFailedException;
import com.cheny.exception.LoginFailedException;
import com.cheny.exception.UpdateEmployeeException;
import com.cheny.query.EmployeeQuery;
import com.cheny.service.EmployeeService;
import com.cheny.mapper.EmployeeMapper;
import com.cheny.vo.EmployeeVo;
import com.cheny.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
* @author Mlpnk
* @description 针对表【employee(平台后台员工表)】的数据库操作Service实现
* @createDate 2026-08-15 11:56:10
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

    @Override
    public Employee login(EmployeeLoginDto employeeLoginDto) {
        String username = employeeLoginDto.getUsername();
        String password = employeeLoginDto.getPassword();
        //校验
        if (username == null || password == null) {
            throw new RuntimeException("用户名或密码为空");
        }
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

        //相同原始密码，同一个算法，输出结果一定一样
        //明文一样，MD5 算出来的十六进制字符串就完全相同。数据库存的是这个 MD5 密文，数据库里面永远不存原始明文密码。
        //MD5 是单向哈希，只能加密，不能解密
        //它不是加密算法，是摘要哈希算法，没有解密密钥，不能反向还原出原始密码。

        //用户名不存在
        Employee employee = lambdaQuery().eq(Employee::getUsername, username).one();
        if (employee == null)
            throw new LoginFailedException(MessageConstant.ACCOUNT_NOT_FOUND);
        //密码不对
        if(!Objects.equals(employee.getPassword(), password))
            throw new LoginFailedException(MessageConstant.PASSWORD_ERROR);

        //用户存在密码正确，但账号被锁定
        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            throw new LoginFailedException(MessageConstant.ACCOUNT_LOCKED);
        }
        return employee;
    }

    @Override
    public void saveEmployee(EmployeeDto employeeDto) {

        if (employeeDto == null){
            throw new AddEmployeeFailedException(MessageConstant.EMPLOYEE_NOT_FOUND);
        }
//        if(employeeDto.getUsername() == null){
//            throw new AddEmployeeFailedException(MessageConstant.USERNAME_NOT_FOUND);
//        }
        //要求用户名是唯一的
        Long count = lambdaQuery().eq(Employee::getUsername, employeeDto.getUsername()).count();
        if (count > 0) {
            throw new AddEmployeeFailedException(MessageConstant.ALREADY_EXISTS);
            //项目里写自定义业务异常BusinessException，配合全局异常处理器，返回统一 JSON。
        }
        //密码加密
        Employee employee = new Employee();
        employee.setPassword(DigestUtils.md5DigestAsHex(employeeDto.getPassword().getBytes(StandardCharsets.UTF_8)));
        BeanUtils.copyProperties(employeeDto, employee);
        save(employee);
    }

    @Override
    public PageVo<EmployeeVo> getEmployeePage(EmployeeQuery employeeQuery) {

        // 分页参数
        Integer pageNo = employeeQuery.getPageNo();
        Integer pageSize = employeeQuery.getPageSize();
        Page<EmployeeVo> pageParam = new Page<>(pageNo, pageSize);

        // 查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeQuery.getName() != null) {
            queryWrapper.eq(Employee::getName, employeeQuery.getName());
        }
        // 排序条件
        String sortBy = employeeQuery.getSortBy();
        //待排序的字段
        Boolean asc = employeeQuery.getAsc();
        // 是否升序 asc有三种取值：true（升序）, false（降序）, null（不排序）
        if (sortBy != null && asc != null) {
            pageParam.addOrder(asc ? OrderItem.asc(sortBy) : OrderItem.desc(sortBy));
        }
        //这里用的是MP提供的分页查询方法，返回的是一个Page对象，里面包含分页查询结果
        Page<EmployeeVo> page =baseMapper.selectPageVo(pageParam,queryWrapper);
        //结果封装
        PageVo<EmployeeVo> pageVo = new PageVo<>();
        pageVo.setTotal(page.getTotal());//总条数
        pageVo.setPages(page.getPages());//总页数
        pageVo.setList(page.getRecords());//当前页数据
        return pageVo;
    }

    //局部更新：只修改status字段，专门用来做员工启用/禁用状态修改
    @Override
    public void updateStatus(Integer status, Long id) {
        //判断员工是否存在
        Employee employee = getById(id);
        if (employee == null) {
            throw new UpdateEmployeeException(MessageConstant.EMPLOYEE_NOT_FOUND);
        }
//        if (status != 0 && status != 1) {
//            throw new UpdateEmployeeException(MessageConstant.PARAM_ILLEGAL);
//        }都没必要，因为用户操作是点击，不是输入，不会有非法参数出现
        //要求用户名是唯一的,可以不变，但不能重复
        Long count = lambdaQuery().eq(Employee::getUsername, employee.getUsername())
                                  .ne(Employee::getId, id).count();
        if (count > 0) {
            throw new AddEmployeeFailedException(MessageConstant.ALREADY_EXISTS);
            //项目里写自定义业务异常BusinessException，配合全局异常处理器，返回统一 JSON。
        }
        employee.setStatus(status);
        boolean flag = updateById(employee);
        if(!flag) throw new UpdateEmployeeException(MessageConstant.UPDATE_ERROR);
    }

    @Override
    public void updateEmployee(EmployeeDto employeeDto) {

        Employee employee = new Employee();
        if(employeeDto.getPassword() != null) {
            employee.setPassword(DigestUtils.md5DigestAsHex(employeeDto.getPassword().getBytes(StandardCharsets.UTF_8)));
        }
        BeanUtils.copyProperties(employeeDto,employee);
        updateById(employee);//只更新非空的字段
    }

    @Override
    public void updatePassword(PasswordEditDto passwordEditDto, Long userId) {
        Employee employee = getById(userId);

        String prePassword = passwordEditDto.getPrePassword();

        prePassword = DigestUtils.md5DigestAsHex(prePassword.getBytes(StandardCharsets.UTF_8));
        if(!Objects.equals(employee.getPassword(),prePassword)){
           throw new UpdateEmployeeException("密码不一致");
        }
        String newPassword = passwordEditDto.getNewPassword();
        //密码加密处理后再存
        newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes(StandardCharsets.UTF_8));

        employee.setPassword(newPassword);
        boolean flag = updateById(employee);
        if(!flag) throw new UpdateEmployeeException(MessageConstant.PASSWORD_EDIT_FAILED);
    }

    @Override
    public EmployeeVo getVoById(Long id) {
        Employee employee  = getById(id);
        //转换为EmployeeVo
        EmployeeVo employeeVo = new EmployeeVo();
        BeanUtils.copyProperties(employee, employeeVo);
        return employeeVo;
    }
}




