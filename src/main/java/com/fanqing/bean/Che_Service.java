package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity(name = "che_service")
public class Che_Service {

    @Id
    @GeneratedValue
    @NotNull
    private int service_id;
    private String service_name;
    private int delete_state;
    private String service_remark;
    private String service_alias_name;
    private BigInteger create_time;
    private String creator;
    private BigInteger update_time;


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

    public int getDelete_state() {
        return delete_state;
    }

    public void setDelete_state(int delete_state) {
        this.delete_state = delete_state;
    }

    public String getService_remark() {
        return service_remark;
    }

    public void setService_remark(String service_remark) {
        this.service_remark = service_remark;
    }

    public String getService_alias_name() {
        return service_alias_name;
    }

    public void setService_alias_name(String service_alias_name) {
        this.service_alias_name = service_alias_name;
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

    public BigInteger getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(BigInteger update_time) {
        this.update_time = update_time;
    }
}
