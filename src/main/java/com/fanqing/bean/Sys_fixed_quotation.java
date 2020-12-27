package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity(name = "sys_account")
public class Sys_fixed_quotation {

    @Id
    @GeneratedValue
    @NotNull
    private int id;
    private int brand_id;
    private String brand_name;
    private int series_id;
    private String series_name;
    private String liter;
    private int service_id;
    private String service_name;
    private BigDecimal amount_money;
    private int is_during_insurance;
    private int original_year;
    private int max_year;
    private int is_new_car;
    private BigInteger create_time;
    private BigInteger update_time;
    private int delete_state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getSeries_id() {
        return series_id;
    }

    public void setSeries_id(int series_id) {
        this.series_id = series_id;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public String getLiter() {
        return liter;
    }

    public void setLiter(String liter) {
        this.liter = liter;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public BigDecimal getAmount_money() {
        return amount_money;
    }

    public void setAmount_money(BigDecimal amount_money) {
        this.amount_money = amount_money;
    }

    public int getIs_during_insurance() {
        return is_during_insurance;
    }

    public void setIs_during_insurance(int is_during_insurance) {
        this.is_during_insurance = is_during_insurance;
    }

    public int getOriginal_year() {
        return original_year;
    }

    public void setOriginal_year(int original_year) {
        this.original_year = original_year;
    }

    public int getMax_year() {
        return max_year;
    }

    public void setMax_year(int max_year) {
        this.max_year = max_year;
    }

    public int getIs_new_car() {
        return is_new_car;
    }

    public void setIs_new_car(int is_new_car) {
        this.is_new_car = is_new_car;
    }

    public BigInteger getCreate_time() {
        return create_time;
    }

    public void setCreate_time(BigInteger create_time) {
        this.create_time = create_time;
    }

    public BigInteger getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(BigInteger update_time) {
        this.update_time = update_time;
    }

    public int getDelete_state() {
        return delete_state;
    }

    public void setDelete_state(int delete_state) {
        this.delete_state = delete_state;
    }
}
