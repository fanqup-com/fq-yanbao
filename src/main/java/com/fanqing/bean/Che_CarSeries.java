package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "che_carseries")
public class Che_CarSeries {

    @Id
    @GeneratedValue
    @NotNull
    private int series_id;
    private String series_name;
    private String maker_type;
    private String series_group_name;
    private String update_time;
    private int brand_id;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"series_id\":")
                .append(series_id);
        sb.append(",\"series_name\":\"")
                .append(series_name).append('\"');
        sb.append(",\"maker_type\":\"")
                .append(maker_type).append('\"');
        sb.append(",\"series_group_name\":\"")
                .append(series_group_name).append('\"');
        sb.append(",\"update_time\":\"")
                .append(update_time).append('\"');
        sb.append(",\"brand_id\":")
                .append(brand_id);
        sb.append('}');
        return sb.toString();
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

    public String getMaker_type() {
        return maker_type;
    }

    public void setMaker_type(String maker_type) {
        this.maker_type = maker_type;
    }

    public String getSeries_group_name() {
        return series_group_name;
    }

    public void setSeries_group_name(String series_group_name) {
        this.series_group_name = series_group_name;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }
}
