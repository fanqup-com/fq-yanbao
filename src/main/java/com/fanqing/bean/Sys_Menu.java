package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity(name = "sys_menu")
public class Sys_Menu {

    @Id
    @GeneratedValue
    @NotNull
    private int id;
    private String menu_addr;
    private String menu_title;
    private String menu_intro;
    private BigInteger menu_create_time;
    private BigInteger menu_update_time;
    private int delete_state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenu_addr() {
        return menu_addr;
    }

    public void setMenu_addr(String menu_addr) {
        this.menu_addr = menu_addr;
    }

    public String getMenu_title() {
        return menu_title;
    }

    public void setMenu_title(String menu_title) {
        this.menu_title = menu_title;
    }

    public String getMenu_intro() {
        return menu_intro;
    }

    public void setMenu_intro(String menu_intro) {
        this.menu_intro = menu_intro;
    }

    public BigInteger getMenu_create_time() {
        return menu_create_time;
    }

    public void setMenu_create_time(BigInteger menu_create_time) {
        this.menu_create_time = menu_create_time;
    }

    public BigInteger getMenu_update_time() {
        return menu_update_time;
    }

    public void setMenu_update_time(BigInteger menu_update_time) {
        this.menu_update_time = menu_update_time;
    }

    public int getDelete_state() {
        return delete_state;
    }

    public void setDelete_state(int delete_state) {
        this.delete_state = delete_state;
    }
}
