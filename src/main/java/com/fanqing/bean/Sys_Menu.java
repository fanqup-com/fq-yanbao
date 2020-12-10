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
    private String menu_path;
    private String menu_title;
    private String menu_route_name;
    private String menu_name;
    private String menu_father;
    private String menu_icon;
    private int has_child;
    private int is_menu;
    private BigInteger menu_create_time;
    private BigInteger menu_update_time;
    private int delete_state;


    public String getMenu_route_name() {
        return menu_route_name;
    }

    public void setMenu_route_name(String menu_route_name) {
        this.menu_route_name = menu_route_name;
    }

    public int getHas_child() {
        return has_child;
    }

    public void setHas_child(int has_child) {
        this.has_child = has_child;
    }

    public String getMenu_icon() {
        return menu_icon;
    }

    public void setMenu_icon(String menu_icon) {
        this.menu_icon = menu_icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenu_path() {
        return menu_path;
    }

    public void setMenu_path(String menu_path) {
        this.menu_path = menu_path;
    }

    public String getMenu_title() {
        return menu_title;
    }

    public void setMenu_title(String menu_title) {
        this.menu_title = menu_title;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_father() {
        return menu_father;
    }

    public void setMenu_father(String menu_father) {
        this.menu_father = menu_father;
    }

    public int getIs_menu() {
        return is_menu;
    }

    public void setIs_menu(int is_menu) {
        this.is_menu = is_menu;
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
