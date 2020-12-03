package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "che_carbrand")
public class Che_CarBrand {

    @Id
    @GeneratedValue
    @NotNull
    private int brand_id;
    private String brand_name;
    private String initial;
    private String update_time;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"brand_id\":")
                .append(brand_id);
        sb.append(",\"brand_name\":\"")
                .append(brand_name).append('\"');
        sb.append(",\"initial\":\"")
                .append(initial).append('\"');
        sb.append(",\"update_time\":\"")
                .append(update_time).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
