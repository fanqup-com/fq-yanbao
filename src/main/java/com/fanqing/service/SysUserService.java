package com.fanqing.service;

import com.alibaba.fastjson.JSONObject;

public interface SysUserService {

    String userLogin(JSONObject jsonObject);

    String getUserInfo(JSONObject jsonObject);

    String createUser(JSONObject jsonObject);

    String updateUser(JSONObject jsonObject);

    String getUserList(JSONObject jsonObject);

    String getRoleList(JSONObject jsonObject);

    String createRole(JSONObject jsonObject);

    String updateRole(JSONObject jsonObject);

    String getOperationLogList(JSONObject jsonObject);

}
