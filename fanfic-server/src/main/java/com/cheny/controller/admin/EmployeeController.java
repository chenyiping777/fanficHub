package com.cheny.controller.admin;
import com.cheny.dto.EmployeeDto;
import com.cheny.dto.PasswordEditDto;
import com.cheny.query.EmployeeQuery;
import com.cheny.utils.CurrentHolder;
import com.cheny.utils.JwtUtil;

import com.cheny.dto.EmployeeLoginDto;
import com.cheny.entity.Employee;
import com.cheny.entity.Result;
import com.cheny.service.EmployeeService;
import com.cheny.vo.EmployeeVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtUtil jwtUtil;


    //登录功能
    @PostMapping("/login")
    public Result login(@RequestBody @Valid EmployeeLoginDto employeeLoginDto){
            log.info("employLoginDto: {}", employeeLoginDto);

            Employee employee = employeeService.login(employeeLoginDto);
            //登录成功，生成令牌
            String token = jwtUtil.createToken(employee.getId(),"admin");
            return Result.success(token);
    }
    
    //新增员工
    @PostMapping
    public Result add(@RequestBody @Valid EmployeeDto employeeDto){
        log.info("employee: {}", employeeDto);
        employeeService.saveEmployee(employeeDto);
        return Result.success();
    }


    //分页查询员工，支持输入员工姓名查询
    @PostMapping("/page")
    public Result page(@RequestBody EmployeeQuery employeeQuery){
        log.info("pageNo: {}, pageSize: {}, name: {}", employeeQuery.getPageNo(), employeeQuery.getPageSize(), employeeQuery.getName());

        return Result.success(employeeService.getEmployeePage(employeeQuery));
    }


    //修改员工的status,前端传过来的是要求：禁用-status：0     启用：status：1
    @PostMapping("/status/{status}")
    //put代表全量更新，restful：post用于执行某个处理动作，创建操作，put用于完整替换已有资源
    public Result startOrStop(@PathVariable Integer status,@RequestParam Long id){
        log.info("要禁用/启用的账号为: {}",id );
        employeeService.updateStatus(status,id);
        return Result.success();
    }


    //根据员工id查找员工信息
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id){
        log.info("要查找的员工id: {}",id );
        EmployeeVo employee = employeeService.getVoById(id);
        return Result.success(employee);
    }

    //编辑员工信息
    @PutMapping
    public Result update(@RequestBody @Valid EmployeeDto employeeDto){
        //id,idNumber,name,phone        ,sex,username,必填字段，交给前端
        log.info("要编辑的员工信息: {}",employeeDto);
        employeeService.updateEmployee(employeeDto);
        return Result.success();

    }


    //修改密码
    @PostMapping("/editPassword")
    public Result editPassword(@RequestBody @Valid PasswordEditDto passwordEditDto){
       Long userId = CurrentHolder.getCurrentId();

       employeeService.updatePassword(passwordEditDto,userId);
       return Result.success();
    }
    //退出登录

    /**
     * 退出：核心动作在前端执行，前端清除本地存储的token，
     * 后续请求不再携带token，自然就变成未登录状态
     *return
     */
    @PostMapping("/logout")
    public Result logout() {
        return Result.success();
    }


}
