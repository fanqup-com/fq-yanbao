package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity(name = "sys_role")
public class Sys_Role {
    @Id
    @GeneratedValue
    @NotNull
    private int id;
    private String role_code;
    private String role_name;
    private String role_intro;
    private String role_permission;
    private BigInteger create_time;
    private String creator;
    private int delete_state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole_code() {
        return role_code;
    }

    public void setRole_code(String role_code) {
        this.role_code = role_code;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_intro() {
        return role_intro;
    }

    public void setRole_intro(String role_intro) {
        this.role_intro = role_intro;
    }

    public BigInteger getCreate_time() {
        return create_time;
    }

    public void setCreate_time(BigInteger create_time) {
        this.create_time = create_time;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


    public int getDelete_state() {
        return delete_state;
    }

    public void setDelete_state(int delete_state) {
        this.delete_state = delete_state;
    }

    public String getRole_permission() {
        return role_permission;
    }

    public void setRole_permission(String role_permission) {
        this.role_permission = role_permission;
    }
}
