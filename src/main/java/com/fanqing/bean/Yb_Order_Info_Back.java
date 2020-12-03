package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity(name = "yb_order_info_back")
public class Yb_Order_Info_Back {


    @Id
    @GeneratedValue
    @NotNull
    private int id;
    private String yb_order_id;
    private String yb_customer_name;
    private String yb_customer_cert_no;
    private String yb_customer_address;
    private String yb_customer_tel;
    private BigInteger yb_create_time;
    private String yb_creator;
    private BigDecimal yb_order_fee;
    private BigDecimal yb_actual_fee;
    private String yb_pay_type;
    private String yb_car_info;
    private String yb_car_number;
    private String yb_car_vin_number;
    private String yb_car_engine_number;
    private String yb_car_mileage;
    private String yb_car_buytime;
    private String yb_product_type;
    private String yb_order_store;
    private int yb_order_print_time;
    private String yb_cert_no_front_path;
    private String yb_cert_no_back_path;
    private String yb_driving_permit_path;
    private String yb_mileage_path;
    private String yb_car_front_path;
    private String yb_car_back_path;
    private BigInteger yb_update_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYb_order_id() {
        return yb_order_id;
    }

    public void setYb_order_id(String yb_order_id) {
        this.yb_order_id = yb_order_id;
    }

    public String getYb_customer_name() {
        return yb_customer_name;
    }

    public void setYb_customer_name(String yb_customer_name) {
        this.yb_customer_name = yb_customer_name;
    }

    public String getYb_customer_cert_no() {
        return yb_customer_cert_no;
    }

    public void setYb_customer_cert_no(String yb_customer_cert_no) {
        this.yb_customer_cert_no = yb_customer_cert_no;
    }

    public String getYb_customer_address() {
        return yb_customer_address;
    }

    public void setYb_customer_address(String yb_customer_address) {
        this.yb_customer_address = yb_customer_address;
    }

    public String getYb_customer_tel() {
        return yb_customer_tel;
    }

    public void setYb_customer_tel(String yb_customer_tel) {
        this.yb_customer_tel = yb_customer_tel;
    }

    public BigInteger getYb_create_time() {
        return yb_create_time;
    }

    public void setYb_create_time(BigInteger yb_create_time) {
        this.yb_create_time = yb_create_time;
    }

    public String getYb_creator() {
        return yb_creator;
    }

    public void setYb_creator(String yb_creator) {
        this.yb_creator = yb_creator;
    }

    public BigDecimal getYb_order_fee() {
        return yb_order_fee;
    }

    public void setYb_order_fee(BigDecimal yb_order_fee) {
        this.yb_order_fee = yb_order_fee;
    }

    public BigDecimal getYb_actual_fee() {
        return yb_actual_fee;
    }

    public void setYb_actual_fee(BigDecimal yb_actual_fee) {
        this.yb_actual_fee = yb_actual_fee;
    }

    public String getYb_pay_type() {
        return yb_pay_type;
    }

    public void setYb_pay_type(String yb_pay_type) {
        this.yb_pay_type = yb_pay_type;
    }

    public String getYb_car_info() {
        return yb_car_info;
    }

    public void setYb_car_info(String yb_car_info) {
        this.yb_car_info = yb_car_info;
    }

    public String getYb_car_number() {
        return yb_car_number;
    }

    public void setYb_car_number(String yb_car_number) {
        this.yb_car_number = yb_car_number;
    }

    public String getYb_car_vin_number() {
        return yb_car_vin_number;
    }

    public void setYb_car_vin_number(String yb_car_vin_number) {
        this.yb_car_vin_number = yb_car_vin_number;
    }

    public String getYb_car_engine_number() {
        return yb_car_engine_number;
    }

    public void setYb_car_engine_number(String yb_car_engine_number) {
        this.yb_car_engine_number = yb_car_engine_number;
    }

    public String getYb_car_mileage() {
        return yb_car_mileage;
    }

    public void setYb_car_mileage(String yb_car_mileage) {
        this.yb_car_mileage = yb_car_mileage;
    }

    public String getYb_car_buytime() {
        return yb_car_buytime;
    }

    public void setYb_car_buytime(String yb_car_buytime) {
        this.yb_car_buytime = yb_car_buytime;
    }

    public String getYb_product_type() {
        return yb_product_type;
    }

    public void setYb_product_type(String yb_product_type) {
        this.yb_product_type = yb_product_type;
    }

    public String getYb_order_store() {
        return yb_order_store;
    }

    public void setYb_order_store(String yb_order_store) {
        this.yb_order_store = yb_order_store;
    }

    public int getYb_order_print_time() {
        return yb_order_print_time;
    }

    public void setYb_order_print_time(int yb_order_print_time) {
        this.yb_order_print_time = yb_order_print_time;
    }

    public String getYb_cert_no_front_path() {
        return yb_cert_no_front_path;
    }

    public void setYb_cert_no_front_path(String yb_cert_no_front_path) {
        this.yb_cert_no_front_path = yb_cert_no_front_path;
    }

    public String getYb_cert_no_back_path() {
        return yb_cert_no_back_path;
    }

    public void setYb_cert_no_back_path(String yb_cert_no_back_path) {
        this.yb_cert_no_back_path = yb_cert_no_back_path;
    }

    public String getYb_driving_permit_path() {
        return yb_driving_permit_path;
    }

    public void setYb_driving_permit_path(String yb_driving_permit_path) {
        this.yb_driving_permit_path = yb_driving_permit_path;
    }

    public String getYb_mileage_path() {
        return yb_mileage_path;
    }

    public void setYb_mileage_path(String yb_mileage_path) {
        this.yb_mileage_path = yb_mileage_path;
    }

    public String getYb_car_front_path() {
        return yb_car_front_path;
    }

    public void setYb_car_front_path(String yb_car_front_path) {
        this.yb_car_front_path = yb_car_front_path;
    }

    public String getYb_car_back_path() {
        return yb_car_back_path;
    }

    public void setYb_car_back_path(String yb_car_back_path) {
        this.yb_car_back_path = yb_car_back_path;
    }

    public BigInteger getYb_update_time() {
        return yb_update_time;
    }

    public void setYb_update_time(BigInteger yb_update_time) {
        this.yb_update_time = yb_update_time;
    }
}
