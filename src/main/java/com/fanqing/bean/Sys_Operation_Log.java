package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity(name = "sys_operation_log")
public class Sys_Operation_Log {

    @Id
    @GeneratedValue
    @NotNull
    private int id;
    private String user_name;
    private BigInteger operation_time;
    private String operation_info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public BigInteger getOperation_time() {
        return operation_time;
    }

    public void setOperation_time(BigInteger operation_time) {
        this.operation_time = operation_time;
    }

    public String getOperation_info() {
        return operation_info;
    }

    public void setOperation_info(String operation_info) {
        this.operation_info = operation_info;
    }
}
