package org.example.bean;

import org.springframework.stereotype.Component;

import java.io.Serializable;
@Component
public class BeanUtil  implements Serializable {
    private Integer code;//状态码
    private String message;//状态信息
    private Object obj;//数据      例如，list集合\对象

    private BeanUtil(){}

    private BeanUtil(Integer code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    public static BeanUtil getInstance(){
        return new BeanUtil();
    }
    //方法
    public static BeanUtil ok(String message){
        return new BeanUtil(200,message,null);
    }
    public static BeanUtil ok(String message,Object obj){
        return new BeanUtil(200,message,obj);
    }
    public static BeanUtil error(String message){
        return new BeanUtil(500,message,null);
    }
    public static BeanUtil error(String message,Object obj){
        return new BeanUtil(500,message,obj);
    }

    @Override
    public String toString() {
        return "BeanUtil{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", obj=" + obj +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
