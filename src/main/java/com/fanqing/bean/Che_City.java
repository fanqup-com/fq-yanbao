package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "che_city")
public class Che_City {

    @Id
    @GeneratedValue
    @NotNull
    private int city_id;
    private String city_name;
    private String admin_code;
    private String initial;
    private int prov_id;
    private String prov_name;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"city_id\":")
                .append(city_id);
        sb.append(",\"city_name\":\"")
                .append(city_name).append('\"');
        sb.append(",\"admin_code\":\"")
                .append(admin_code).append('\"');
        sb.append(",\"initial\":\"")
                .append(initial).append('\"');
        sb.append(",\"prov_id\":")
                .append(prov_id);
        sb.append(",\"prov_name\":\"")
                .append(prov_name).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getAdmin_code() {
        return admin_code;
    }

    public void setAdmin_code(String admin_code) {
        this.admin_code = admin_code;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public int getProv_id() {
        return prov_id;
    }

    public void setProv_id(int prov_id) {
        this.prov_id = prov_id;
    }

    public String getProv_name() {
        return prov_name;
    }

    public void setProv_name(String prov_name) {
        this.prov_name = prov_name;
    }
}
