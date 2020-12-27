package com.fanqing.controller;

import com.alibaba.fastjson.JSONObject;
import com.fanqing.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     *@Date:2020/12/17_22:36
     */
    @RequestMapping(value = "/showCity", method = RequestMethod.POST)
    public String ShowCheCity(@RequestBody JSONObject jsonObject, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.showCity(jsonObject,token);
    }

    /*
     *@Author:xjy
     *@Description:获取车品牌列表
     *@Date:2020/12/17_22:36
     */
    @RequestMapping(value = "/showCarBrandList", method = RequestMethod.POST)
    public String showCarBrandList(@RequestBody JSONObject jsonObject, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.showCarBrandList(jsonObject,token);
    }


    /*
     *@Author:xjy
     *@Description:获取车系列表
     *@Date:2020/12/17_22:36
     */
    @RequestMapping(value = "/showCarSeriesList", method = RequestMethod.POST)
    public String showCarSeriesList(@RequestBody JSONObject jsonObject, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.showCarSeriesList(jsonObject,token);
    }

    /*
     *@Author:xjy
     *@Description:获取车型列表
     *@Date:2020/12/17_22:36
     */
    @RequestMapping(value = "/showCarList", method = RequestMethod.POST)
    public String showCarList(@RequestBody JSONObject jsonObject, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.showCarList(jsonObject,token);
    }
    /*
     *@Author:xjy
     *@Description:获取门店可售服务列表
     *@Date:2020/12/17_22:36
     */
    @RequestMapping(value = "/showStoreServiceList", method = RequestMethod.POST)
    public String showStoreServiceList(@RequestBody JSONObject jsonObject, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.showStoreServiceList(jsonObject,token);
    }

    /*
     *@Author:xjy
     *@Description:获取延保价格
     *@Date:2020/12/18_21:31
     */
    @RequestMapping(value = "/GetExtendInsurePrice", method = RequestMethod.POST)
    public String GetExtendInsurePrice(@RequestBody JSONObject jsonObject, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.GetExtendInsurePrice(jsonObject,token);
    }

    @RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
    public String uploadPhoto(@RequestBody JSONObject jsonObject, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.uploadPhoto(jsonObject,token);
    }

    @RequestMapping(value = "/saveOrderInfo", method = RequestMethod.POST)
    public String saveOrderInfo(HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return cardService.saveOrderInfo(request,token);
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
