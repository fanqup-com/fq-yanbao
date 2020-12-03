package com.fanqing.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity(name = "ins_insureservice")
public class Ins_insureservice {


    @Id
    @GeneratedValue
    @NotNull
    private int is_id;
    private String is_name;
    private int is_lowpremium;
    private int is_medianpremium;
    private int is_highpremium;
    private int is_mincarprice;
    private int is_maxcarprice;
    private String is_carpricenexus;
    private int is_status;
    private String is_remark;
    private String is_pinyin;
    private String is_wubi;
    private String is_diy;
    private Timestamp is_createdate;
    private int is_createuser;
    private Timestamp is_modifydate;
    private int is_modifyuser;
    private String is_modifyusername;

    public int getIs_id() {
        return is_id;
    }

    public void setIs_id(int is_id) {
        this.is_id = is_id;
    }

    public String getIs_name() {
        return is_name;
    }

    public void setIs_name(String is_name) {
        this.is_name = is_name;
    }

    public int getIs_lowpremium() {
        return is_lowpremium;
    }

    public void setIs_lowpremium(int is_lowpremium) {
        this.is_lowpremium = is_lowpremium;
    }

    public int getIs_medianpremium() {
        return is_medianpremium;
    }

    public void setIs_medianpremium(int is_medianpremium) {
        this.is_medianpremium = is_medianpremium;
    }

    public int getIs_highpremium() {
        return is_highpremium;
    }

    public void setIs_highpremium(int is_highpremium) {
        this.is_highpremium = is_highpremium;
    }

    public int getIs_mincarprice() {
        return is_mincarprice;
    }

    public void setIs_mincarprice(int is_mincarprice) {
        this.is_mincarprice = is_mincarprice;
    }

    public int getIs_maxcarprice() {
        return is_maxcarprice;
    }

    public void setIs_maxcarprice(int is_maxcarprice) {
        this.is_maxcarprice = is_maxcarprice;
    }

    public String getIs_carpricenexus() {
        return is_carpricenexus;
    }

    public void setIs_carpricenexus(String is_carpricenexus) {
        this.is_carpricenexus = is_carpricenexus;
    }

    public int getIs_status() {
        return is_status;
    }

    public void setIs_status(int is_status) {
        this.is_status = is_status;
    }

    public String getIs_remark() {
        return is_remark;
    }

    public void setIs_remark(String is_remark) {
        this.is_remark = is_remark;
    }

    public String getIs_pinyin() {
        return is_pinyin;
    }

    public void setIs_pinyin(String is_pinyin) {
        this.is_pinyin = is_pinyin;
    }

    public String getIs_wubi() {
        return is_wubi;
    }

    public void setIs_wubi(String is_wubi) {
        this.is_wubi = is_wubi;
    }

    public String getIs_diy() {
        return is_diy;
    }

    public void setIs_diy(String is_diy) {
        this.is_diy = is_diy;
    }

    public Timestamp getIs_createdate() {
        return is_createdate;
    }

    public void setIs_createdate(Timestamp is_createdate) {
        this.is_createdate = is_createdate;
    }

    public int getIs_createuser() {
        return is_createuser;
    }

    public void setIs_createuser(int is_createuser) {
        this.is_createuser = is_createuser;
    }

    public Timestamp getIs_modifydate() {
        return is_modifydate;
    }

    public void setIs_modifydate(Timestamp is_modifydate) {
        this.is_modifydate = is_modifydate;
    }

    public int getIs_modifyuser() {
        return is_modifyuser;
    }

    public void setIs_modifyuser(int is_modifyuser) {
        this.is_modifyuser = is_modifyuser;
    }

    public String getIs_modifyusername() {
        return is_modifyusername;
    }

    public void setIs_modifyusername(String is_modifyusername) {
        this.is_modifyusername = is_modifyusername;
    }
}
