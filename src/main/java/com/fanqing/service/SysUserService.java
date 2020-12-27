package com.fanqing.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface SysUserService {

    String userLogin(JSONObject jsonObject);

    String getUserInfo(JSONObject jsonObject, String token);

    String createUser(JSONObject jsonObject, String token);

    String updateUser(JSONObject jsonObject, String token);

    String getUserList(JSONObject jsonObject, String token);

    String getRoleList(JSONObject jsonObject, String token);

    String createRole(JSONObject jsonObject, String token);

    String updateRole(JSONObject jsonObject, String token);

    String getOperationLogList(JSONObject jsonObject, String token);

    String getStoreList(JSONObject jsonObject, String token);

    String createStore(JSONObject jsonObject, String token);

    String updateStore(JSONObject jsonObject, String token);

}
