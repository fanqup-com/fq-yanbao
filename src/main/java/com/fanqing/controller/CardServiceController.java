package com.fanqing.controller;

import com.fanqing.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cardService")
public class CardServiceController {

    @Autowired
    private CardService cardService;

    /*
     *@Author:xjy
     *@Description:获取城市级联信息
     *@Date:2019/5/29_15:36
     */
    @RequestMapping(value = "/ShowCheCity", method = RequestMethod.POST)
    public String ShowCheCity(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.ShowCheCity(request);
    }

    /*
     *@Author:xjy
     *@Description:获取车列表
     *@Date:2019/6/1_16:43
     */
    @RequestMapping(value = "/ShowCarSeriesList", method = RequestMethod.POST)
    public String ShowCarSeriesList(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.ShowCarSeriesList(request);
    }


    /*
     *@Author:xjy
     *@Description:查询车型
     *@Date:2019/6/1_17:27
     */
    @RequestMapping(value = "/ShowCarList", method = RequestMethod.POST)
    public String ShowCarList(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.ShowCarList(request);
    }

    /*
     *@Author:xjy
     *@Description:获取车辆基础估值
     *@Date:2019/6/4_10:24
     */
    @RequestMapping(value = "/GetUsedCarPrice", method = RequestMethod.POST)
    public String GetUsedCarPrice(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.GetUsedCarPrice(request);
    }

    /*
     *@Author:xjy
     *@Description:获取项目列表
     *@Date:2019/6/4_11:37
     */
    @RequestMapping(value = "/GetProjectHtml", method = RequestMethod.POST)
    public String GetProjectHtml(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.GetProjectHtml(request);
    }

    /*
     *@Author:xjy
     *@Description:获取投保计划
     *@Date:2019/6/4_14:29
     */
    @RequestMapping(value = "/GetInsurePlanHtml", method = RequestMethod.POST)
    public String GetInsurePlanHtml(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.GetInsurePlanHtml(request);
    }

    /*
     *@Author:xjy
     *@Description:获取延保价格1
     *@Date:2019/6/6_11:25
     */
    @RequestMapping(value = "/GetExtendInsurePrice", method = RequestMethod.POST)
    public String GetExtendInsurePrice(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.GetExtendInsurePrice(request);
    }

    @RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
    public String uploadPhoto(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.uploadPhoto(request);
    }

    @RequestMapping(value = "/saveOrderInfo", method = RequestMethod.POST)
    public String saveOrderInfo(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.saveOrderInfo(request);
    }

    @RequestMapping(value = "/updateOrderInfo", method = RequestMethod.POST)
    public String updateOrderInfo(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.updateOrderInfo(request);
    }

    @RequestMapping(value = "/getOrderInfoList", method = RequestMethod.POST)
    public String getOrderInfoList(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.getOrderInfoList(request);
    }











}
