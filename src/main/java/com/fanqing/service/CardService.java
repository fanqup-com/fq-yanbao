package com.fanqing.service;

import javax.servlet.http.HttpServletRequest;

public interface CardService {

    String ShowCheCity(HttpServletRequest request);

    String ShowCarSeriesList(HttpServletRequest request);

    String ShowCarList(HttpServletRequest request);

    String GetUsedCarPrice(HttpServletRequest request);

    String GetProjectHtml(HttpServletRequest request);

    String GetInsurePlanHtml(HttpServletRequest request);

    String GetExtendInsurePrice(HttpServletRequest request);

    String uploadPhoto(HttpServletRequest request);

    String saveOrderInfo(HttpServletRequest request);

    String updateOrderInfo(HttpServletRequest request);

    String getOrderInfoList(HttpServletRequest request);

}
