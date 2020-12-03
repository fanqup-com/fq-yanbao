package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity(name = "ins_projectservice")
public class Ins_projectservice {

    @Id
    @GeneratedValue
    @NotNull
    private int ps_id;
    private String ps_name;
    private BigDecimal ps_coefficient;
    private int ps_status;
    private String ps_remark;
    private String ps_pinyin;
    private String ps_wubi;
    private String ps_diy;
    private Timestamp ps_createdate;
    private int ps_createuser;
    private Timestamp ps_modifydate;
    private int ps_modifyuser;
    private String ps_modifyusername;

    public int getPs_id() {
        return ps_id;
    }

    public void setPs_id(int ps_id) {
        this.ps_id = ps_id;
    }

    public String getPs_name() {
        return ps_name;
    }

    public void setPs_name(String ps_name) {
        this.ps_name = ps_name;
    }

    public BigDecimal getPs_coefficient() {
        return ps_coefficient;
    }

    public void setPs_coefficient(BigDecimal ps_coefficient) {
        this.ps_coefficient = ps_coefficient;
    }

    public int getPs_status() {
        return ps_status;
    }

    public void setPs_status(int ps_status) {
        this.ps_status = ps_status;
    }

    public String getPs_remark() {
        return ps_remark;
    }

    public void setPs_remark(String ps_remark) {
        this.ps_remark = ps_remark;
    }

    public String getPs_pinyin() {
        return ps_pinyin;
    }

    public void setPs_pinyin(String ps_pinyin) {
        this.ps_pinyin = ps_pinyin;
    }

    public String getPs_wubi() {
        return ps_wubi;
    }

    public void setPs_wubi(String ps_wubi) {
        this.ps_wubi = ps_wubi;
    }

    public String getPs_diy() {
        return ps_diy;
    }

    public void setPs_diy(String ps_diy) {
        this.ps_diy = ps_diy;
    }

    public Timestamp getPs_createdate() {
        return ps_createdate;
    }

    public void setPs_createdate(Timestamp ps_createdate) {
        this.ps_createdate = ps_createdate;
    }

    public int getPs_createuser() {
        return ps_createuser;
    }

    public void setPs_createuser(int ps_createuser) {
        this.ps_createuser = ps_createuser;
    }

    public Timestamp getPs_modifydate() {
        return ps_modifydate;
    }

    public void setPs_modifydate(Timestamp ps_modifydate) {
        this.ps_modifydate = ps_modifydate;
    }

    public int getPs_modifyuser() {
        return ps_modifyuser;
    }

    public void setPs_modifyuser(int ps_modifyuser) {
        this.ps_modifyuser = ps_modifyuser;
    }

    public String getPs_modifyusername() {
        return ps_modifyusername;
    }

    public void setPs_modifyusername(String ps_modifyusername) {
        this.ps_modifyusername = ps_modifyusername;
    }
}
