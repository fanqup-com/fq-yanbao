package com.fanqing.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface MenuService {

    String getMenuList(JSONObject jsonObject);
}
