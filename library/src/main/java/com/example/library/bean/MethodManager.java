package com.example.library.bean;

import com.example.library.type.NetType;

import java.lang.reflect.Method;

/**
 * 保存符合要求的注解方法的实体类
 */
public class MethodManager {
    //参数类型
    private Class<?> type;
    //网络类型
    private NetType netType;
    //执行的注解方法:
    private Method method;

    public MethodManager(Class<?> type, NetType netType, Method method) {
        this.type = type;
        this.netType = netType;
        this.method = method;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public NetType getNetType() {
        return netType;
    }

    public void setNetType(NetType netType) {
        this.netType = netType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
