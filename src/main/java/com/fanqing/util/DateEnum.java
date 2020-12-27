package com.fanqing.util;

/**
 * @author zhishuo
 */
public enum DateEnum {
    DATE_FORMAT("yyyy-MM-dd HH:mm:ss"), DATE_SIMPLE("yyyy-MM-dd"),
    DATE_MIN("yy-MM-dd"), DATE_SIMPLE_MIN("yyyyMMdd"),
    DATE_YYYY_MM("yyyyMM"),
    DATE_YEAR("yyyy"),DATE_MONTH("MM"),DATE_DAY("dd"),
    DATE_CHINESE("yyyy年MM月dd日"), DATE_TIME_MIN("HHmmss"),DATE_TIME_STANDARD("HH:mm:ss"),
    DATE_BANK_SEQ("yyyyMMddHHmmss"),
    DATE_BANK_SEQ_MILL("yyyyMMddHHmmssSSS"),
    DATE_SPLIT_SEQ("yyyy-MM-dd HH:mm:ss.sss"),
    DATE_SLASH("yyyy/MM/dd"),
    DATE_POINT("yyyy.MM.dd");
    private String text;

    private DateEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
