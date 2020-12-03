package com.fanqing.bean.resp;

import java.io.Serializable;

public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = 4380471104923516249L;


    private static final String prefix = "[核心账务系统]：";
    private String code;
    private String msg;
    private T result;

    public void Success(String message, T result) {
        this.code = "SUCCESS";
        this.msg = ("[延保系统]：" + message);
        this.result = result;
    }

    public void Error(String message, T result) {
        this.code = "ERROR";
        this.msg = ("[延保系统]：" + message);
        this.result = result;
    }

    public void Error(String code, String message) {
        this.code = code;
        this.msg = ("[延保系统]：" + message);
    }

    public void Error(String message) {
        this.code = "ERROR";
        this.msg = ("[延保系统]：" + message);
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return (T) this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String toString() {
        return "JsonResult{code=" + this.code + ", msg='" + this.msg + '\'' + ", result=" + this.result + '}';
    }

}
