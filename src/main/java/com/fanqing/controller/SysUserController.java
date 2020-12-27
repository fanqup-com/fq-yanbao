package com.fanqing.controller;

import com.alibaba.fastjson.JSONObject;
import com.fanqing.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/sysUserService")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登录
     * @param jsonObject
     * @param response
     * @return
     */
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public String userLogin(@RequestBody JSONObject jsonObject, HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return sysUserService.userLogin(jsonObject);
    }

    /**
     * 根据token获取用户信息
     * @param jsonObject
     * @param response
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public String getUserInfo(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return sysUserService.getUserInfo(jsonObject,token);
    }

    /**
     * 获取用户列表
     * @param jsonObject
     * @param response
     * @return
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST)
    public String getUserList(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return sysUserService.getUserList(jsonObject,token);
    }

    /**
     * 创建新用户
     * @param jsonObject
     * @param response
     * @return
     */
    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public String createUser(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return sysUserService.createUser(jsonObject,token);
    }

    /**
     * 修改用户信息，状态
     * @param jsonObject
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public String updateUser(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return sysUserService.updateUser(jsonObject,token);
    }

    /**
     * 获取角色列表
     * @param jsonObject
     * @param response
     * @return
     */
    @RequestMapping(value = "/getRoleList", method = RequestMethod.POST)
    public String getRoleList(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return sysUserService.getRoleList(jsonObject,token);
    }

    /**
     * 创建角色
     * @param jsonObject
     * @param response
     * @return
     */
    @RequestMapping(value = "/createRole", method = RequestMethod.POST)
    public String createRole(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return sysUserService.createRole(jsonObject,token);
    }

    /**
     * 修改角色信息
     * @param jsonObject
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    public String updateRole(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return sysUserService.updateRole(jsonObject,token);
    }

    /**
     * 获取操作记录日志
     * @param jsonObject
     * @param response
     * @return
     */
    @RequestMapping(value = "/getOperationLogList", method = RequestMethod.POST)
    public String getOperationLogList(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return sysUserService.getOperationLogList(jsonObject,token);
    }

    /**
     * 获取门店列表
     * @param jsonObject
     * @param response
     * @return
     */
    @RequestMapping(value = "/getStoreList", method = RequestMethod.POST)
    public String getStoreList(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return sysUserService.getStoreList(jsonObject,token);
    }

    /**
     * 创建门店
     * @param jsonObject
     * @param response
     * @return
     */
    @RequestMapping(value = "/createStore", method = RequestMethod.POST)
    public String createStore(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return sysUserService.createStore(jsonObject,token);
    }

    /**
     * 修改门店信息
     * @param jsonObject
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateStore", method = RequestMethod.POST)
    public String updateStore(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        return sysUserService.updateStore(jsonObject,token);
    }










}
