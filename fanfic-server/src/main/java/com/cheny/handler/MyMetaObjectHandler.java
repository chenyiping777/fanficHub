package com.cheny.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.cheny.utils.CurrentHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component//加上component，MP才会识别这个处理器，自己写的实现类，区别第三方给的
public class MyMetaObjectHandler implements MetaObjectHandler {
/*
*新增的时候执行  meta:元数据（描述数据的数据） metaObject就是封装了实体对象的“元对象”
* 只有字段为null的时候才会自动填充
* */
    @Override
    public void insertFill(MetaObject metaObject) {
        //参数：实体属性名，填充值，元数据对象
        //严格插入填充
        strictInsertFill(metaObject,//相当于我们的entity
                "createTime",//要填充的字段名
                LocalDateTime::now,//填充的值的来源
                LocalDateTime.class);//填充的值的类型,格式是字节码对象
        Long userId = CurrentHolder.getCurrentId();
        strictInsertFill(metaObject,"createUser",()->userId,Long.class);

        strictInsertFill(metaObject,"updateTime",()->LocalDateTime.now(),LocalDateTime.class);

        strictInsertFill(metaObject,"updateUser",()->userId,Long.class);
    }
    /*
    *更新的时候执行
    * */
    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = CurrentHolder.getCurrentId();
        strictUpdateFill(metaObject,"updateTime",()->LocalDateTime.now(),LocalDateTime.class);

        strictUpdateFill(metaObject,"updateUser",()->userId,Long.class);
    }
}
