package com.cheny.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    响应结果的实体类
* */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result {
    private Integer code;//成功编码 ：1成功，0失败
    private String msg;//错误信息
    private Object data;//数据

    public static Result success(){
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        return result;
    }

    public static Result success(Object o){
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        result.data = o;
        return result;
    }
    public static Result error(){
        Result result = new Result();
        result.code = 0;
        result.msg = "error";
        return result;
    }

    public static Result error(String msg){
        Result result = new Result();
        result.code = 0;
        result.msg = msg;
        return result;
    }
}
