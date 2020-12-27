package com.fanqing.service;

import com.alibaba.fastjson.JSONObject;

public interface MenuService {

    String getMenuList(JSONObject jsonObject,String token);
}
