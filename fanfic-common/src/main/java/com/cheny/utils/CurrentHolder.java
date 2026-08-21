package com.cheny.utils;

public class CurrentHolder {
    private static final ThreadLocal<Object> CURRENT_LOCAL = new ThreadLocal<>();
    //线程本地变量

    public static void setCurrentId(Long employeeId){

        CURRENT_LOCAL.set(employeeId);
    }

    public static void setRole(String role){
        CURRENT_LOCAL.set(role);
    }
    public static Long getCurrentId(){
        return (Long) CURRENT_LOCAL.get();
    }

    public static String getRole(){
        return (String) CURRENT_LOCAL.get();
    }

    public static void remove(){
        CURRENT_LOCAL.remove();
    }
}
