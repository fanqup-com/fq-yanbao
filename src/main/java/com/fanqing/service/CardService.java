package com.fanqing.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface CardService {

    String showCity(JSONObject jsonObject,String token);

    String showCarBrandList(JSONObject jsonObject,String token);

    String showCarSeriesList(JSONObject jsonObject,String token);

    String showCarList(JSONObject jsonObject,String token);

    String showStoreServiceList(JSONObject jsonObject,String token);

    String GetExtendInsurePrice(JSONObject jsonObject,String token);

    String uploadPhoto(JSONObject jsonObject1,String token);

    String saveOrderInfo(HttpServletRequest request,String token);

    String updateOrderInfo(HttpServletRequest request);

    String getOrderInfoList(HttpServletRequest request);

}
