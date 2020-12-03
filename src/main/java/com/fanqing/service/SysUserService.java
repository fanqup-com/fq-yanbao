package com.fanqing.service;

import javax.servlet.http.HttpServletRequest;

public interface SysUserService {

    String userLogin(HttpServletRequest request);

    String getUserInfo(HttpServletRequest request);

    String createUser(HttpServletRequest request);

    String updateUser(HttpServletRequest request);

    String getUserList(HttpServletRequest request);

    String getRoleList(HttpServletRequest request);

    String createRole(HttpServletRequest request);

    String updateRole(HttpServletRequest request);

}
