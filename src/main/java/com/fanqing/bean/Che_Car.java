package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "che_car")
public class Che_Car {

    @Id
    @GeneratedValue
    @NotNull
    private int model_id;
    private String model_name;
    private String short_name;
    private String model_price;
    private String model_year;
    private String min_reg_year;
    private String max_reg_year;
    private String liter;
    private String gear_type;
    private String discharge_standard;
    private String seat_number;
    private String update_time;
    private int series_id;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"model_id\":")
                .append(model_id);
        sb.append(",\"model_name\":\"")
                .append(model_name).append('\"');
        sb.append(",\"short_name\":\"")
                .append(short_name).append('\"');
        sb.append(",\"model_price\":\"")
                .append(model_price).append('\"');
        sb.append(",\"model_year\":\"")
                .append(model_year).append('\"');
        sb.append(",\"min_reg_year\":\"")
                .append(min_reg_year).append('\"');
        sb.append(",\"max_reg_year\":\"")
                .append(max_reg_year).append('\"');
        sb.append(",\"liter\":\"")
                .append(liter).append('\"');
        sb.append(",\"gear_type\":\"")
                .append(gear_type).append('\"');
        sb.append(",\"discharge_standard\":\"")
                .append(discharge_standard).append('\"');
        sb.append(",\"seat_number\":\"")
                .append(seat_number).append('\"');
        sb.append(",\"update_time\":\"")
                .append(update_time).append('\"');
        sb.append(",\"series_id\":")
                .append(series_id);
        sb.append('}');
        return sb.toString();
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getModel_price() {
        return model_price;
    }

    public void setModel_price(String model_price) {
        this.model_price = model_price;
    }

    public String getModel_year() {
        return model_year;
    }

    public void setModel_year(String model_year) {
        this.model_year = model_year;
    }

    public String getMin_reg_year() {
        return min_reg_year;
    }

    public void setMin_reg_year(String min_reg_year) {
        this.min_reg_year = min_reg_year;
    }

    public String getMax_reg_year() {
        return max_reg_year;
    }

    public void setMax_reg_year(String max_reg_year) {
        this.max_reg_year = max_reg_year;
    }

    public String getLiter() {
        return liter;
    }

    public void setLiter(String liter) {
        this.liter = liter;
    }

    public String getGear_type() {
        return gear_type;
    }

    public void setGear_type(String gear_type) {
        this.gear_type = gear_type;
    }

    public String getDischarge_standard() {
        return discharge_standard;
    }

    public void setDischarge_standard(String discharge_standard) {
        this.discharge_standard = discharge_standard;
    }

    public String getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(String seat_number) {
        this.seat_number = seat_number;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getSeries_id() {
        return series_id;
    }

    public void setSeries_id(int series_id) {
        this.series_id = series_id;
    }
}
