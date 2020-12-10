package com.fanqing.controller;

import com.alibaba.fastjson.JSONObject;
import com.fanqing.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/menuService")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/getMenuList", method = RequestMethod.POST)
    public String userLogin(@RequestBody JSONObject jsonObject, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return menuService.getMenuList(jsonObject);
    }


}
