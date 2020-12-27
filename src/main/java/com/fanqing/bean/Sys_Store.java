package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity(name = "sys_store")
public class Sys_Store {

    @Id
    @GeneratedValue
    @NotNull
    private int id;
    private String store_name;
    private String store_tel;
    private String authorized_repairer;
    private String store_intro;
    private String store_years;
    private String store_carbrand;
    private String store_service;
    private String store_address;
    private String store_manager;
    private String store_city;
    private String store_province;
    private String store_code;
    private BigInteger store_create_time;
    private BigInteger store_update_time;
    private int delete_state;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_tel() {
        return store_tel;
    }

    public void setStore_tel(String store_tel) {
        this.store_tel = store_tel;
    }

    public String getAuthorized_repairer() {
        return authorized_repairer;
    }

    public void setAuthorized_repairer(String authorized_repairer) {
        this.authorized_repairer = authorized_repairer;
    }

    public String getStore_intro() {
        return store_intro;
    }

    public void setStore_intro(String store_intro) {
        this.store_intro = store_intro;
    }

    public String getStore_years() {
        return store_years;
    }

    public void setStore_years(String store_years) {
        this.store_years = store_years;
    }

    public String getStore_carbrand() {
        return store_carbrand;
    }

    public void setStore_carbrand(String store_carbrand) {
        this.store_carbrand = store_carbrand;
    }

    public String getStore_service() {
        return store_service;
    }

    public void setStore_service(String store_service) {
        this.store_service = store_service;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getStore_manager() {
        return store_manager;
    }

    public void setStore_manager(String store_manager) {
        this.store_manager = store_manager;
    }

    public String getStore_city() {
        return store_city;
    }

    public void setStore_city(String store_city) {
        this.store_city = store_city;
    }

    public String getStore_province() {
        return store_province;
    }

    public void setStore_province(String store_province) {
        this.store_province = store_province;
    }

    public String getStore_code() {
        return store_code;
    }

    public void setStore_code(String store_code) {
        this.store_code = store_code;
    }

    public BigInteger getStore_create_time() {
        return store_create_time;
    }

    public void setStore_create_time(BigInteger store_create_time) {
        this.store_create_time = store_create_time;
    }

    public BigInteger getStore_update_time() {
        return store_update_time;
    }

    public void setStore_update_time(BigInteger store_update_time) {
        this.store_update_time = store_update_time;
    }

    public int getDelete_state() {
        return delete_state;
    }

    public void setDelete_state(int delete_state) {
        this.delete_state = delete_state;
    }
}
