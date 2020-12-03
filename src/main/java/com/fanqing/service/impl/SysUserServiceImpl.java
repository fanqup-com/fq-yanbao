package com.fanqing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fanqing.bean.*;
import com.fanqing.dao.SysAccountRepository;
import com.fanqing.dao.SysMenuRepository;
import com.fanqing.dao.SysOperationLogRepository;
import com.fanqing.dao.SysRoleRepository;
import com.fanqing.service.SysUserService;
import com.fanqing.util.Base64Util;
import com.fanqing.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysAccountRepository sysAccountRepository;
    @Autowired
    private SysRoleRepository sysRoleRepository;
    @Autowired
    private SysOperationLogRepository sysOperationLogRepository;
    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Override
    public String userLogin(HttpServletRequest request) {
        String user_name = request.getParameter("user_name");
        String password_md5 = request.getParameter("password");
        try {
            if (user_name != null && !"".equals(user_name) && password_md5 != null && !"".equals(password_md5)) {
                Sys_Account account = sysAccountRepository.getAccount(user_name, password_md5);
                if (account != null) {
                    long limit_time = System.currentTimeMillis()+ 604800000;
                    String tokenJson = "{\"user_name\":\"" + user_name + "\",\"limit_time\":\"" + limit_time + "\"}";
                    System.out.println(tokenJson);
                    String token = Base64Util.encoderBASE64(tokenJson).
                            replaceAll("\n","").
                            replaceAll("\r","").
                            replaceAll("\t","").
                            trim();

                    return "{\"code\": 200,\"msg\":{\"token\":\"" + token + "\"}}";
                } else {
                    return "{\"code\": 201,\"msg\": \"用户名或密码错误\"}";
                }
            } else {
                return "{\"code\": 201,\"msg\": \"参数异常\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"参数异常\"}";
        }
    }

    @Override
    public String getUserInfo(HttpServletRequest request) {
        String token = request.getParameter("token");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object user_name = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (user_name != null && !"".equals(user_name) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime){
                        return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    Sys_Account account = sysAccountRepository.getByUserName(user_name.toString());
                    if (account != null) {
                        Sys_Role sysRole = sysRoleRepository.getOne(account.getRole_id());
                        StringBuilder menuSb = new StringBuilder("[");
                        String[] menuArr = sysRole.getRole_permission().split(",");
                        for (String menuid : menuArr) {
                            Sys_Menu menu = sysMenuRepository.getById(Integer.valueOf(menuid));
                            menuSb.append("{");
                            menuSb.append("\"menu_addr\":\"" + menu.getMenu_addr() + "\",");
                            menuSb.append("\"menu_title\":\"" + menu.getMenu_title() + "\",");
                            menuSb.append("\"menu_intro\":\"" + menu.getMenu_intro() + "\"");
                            menuSb.append("},");
                        }
                        if (menuSb.toString().contains("menu_addr")) {
                            menuSb = menuSb.deleteCharAt(menuSb.length() - 1);
                        }
                        menuSb.append("]");

                        return "{\"code\": 200,\"msg\":{" +
                                "\"user_name\":\"" + user_name + "\"," +
                                "\"role_name\":\"" + sysRole.getRole_name() + "\"," +
                                "\"tel\":\"" + account.getTel() + "\"," +
                                "\"remark\":\"" + account.getRemark() + "\"," +
                                "\"store_id\":\"" + account.getStore_id() + "\"," +
                                "\"role_permission\":" + menuSb.toString() + "" +
                                "}}";
                    } else {
                        return "{\"code\": 201,\"msg\": \"用户不存在\"}";
                    }
                }else {
                    return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }


    @Override
    public String createUser(HttpServletRequest request) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
//            String reqMsg = request.getParameter("reqMsg");
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object user_name = jsonObject.get("user_name");
                Object password = jsonObject.get("password");
                Object password_md5 = jsonObject.get("password_md5");
                Object role_id = jsonObject.get("role_id");
                Object creator = jsonObject.get("creator");
                if (user_name != null && !"".equals(user_name) && password != null && !"".equals(password)
                        && password_md5 != null && !"".equals(password_md5) && role_id != null && !"".equals(role_id) && creator != null && !"".equals(creator)) {

                    Sys_Account account = new Sys_Account();
                    account.setUser_name(user_name.toString());
                    account.setPassword(password.toString());
                    account.setPassword_md5(password_md5.toString());
                    account.setCreate_time(BigInteger.valueOf(System.currentTimeMillis()));
                    account.setCreator(creator.toString());
                    account.setDelete_state(0);
                    account.setRole_id(Integer.parseInt(role_id.toString()));
                    sysAccountRepository.save(account);
                    saveLog(creator.toString(), "创建新用户：" + user_name);
                    return Base64Util.encoderBASE64("{\"code\": 200,\"msg\": \"用户创建成功\"}");
                } else {
                    return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                }
            } else {
                return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "eyJjb2RlIjogMjAxLCJtc2ciOiAi57O757uf5byC5bi4In0=";
        }
    }

    @Override
    public String updateUser(HttpServletRequest request) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
//            String reqMsg = request.getParameter("reqMsg");
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object creator = jsonObject.get("creator");
                Object user_name = jsonObject.get("user_name");
                Object password = jsonObject.get("password");
                Object password_md5 = jsonObject.get("password_md5");
                Object role_id = jsonObject.get("role_id");
                Object delete_state = jsonObject.get("delete_state");
                if (user_name != null && !"".equals(user_name) && creator != null && !"".equals(creator)) {

                    Sys_Account account = sysAccountRepository.getByUserName(user_name.toString());
                    account.setCreator(creator.toString());

                    if (password != null && !"".equals(password) && password_md5 != null && !"".equals(password_md5)) {
                        account.setPassword(password.toString());
                        account.setPassword_md5(password_md5.toString());
                    }
                    if (delete_state != null && !"".equals(delete_state)) {
                        account.setDelete_state(Integer.parseInt(delete_state.toString()));
                    }
                    if (role_id != null && !"".equals(role_id)) {
                        account.setRole_id(Integer.parseInt(role_id.toString()));
                    }
                    sysAccountRepository.save(account);
                    saveLog(creator.toString(), "修改用户信息：" + user_name + ",密码：" + password + ",权限：" + role_id + ",删除状态:" + delete_state);
                    return Base64Util.encoderBASE64("{\"code\": 200,\"msg\": \"用户修改成功\"}");
                } else {
                    return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                }
            } else {
                return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "eyJjb2RlIjogMjAxLCJtc2ciOiAi57O757uf5byC5bi4In0=";
        }
    }

    @Override
    public String getUserList(HttpServletRequest request) {
        String token = request.getParameter("token");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object user_name = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (user_name != null && !"".equals(user_name) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime){
                        return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    List<Sys_Account> accountList = sysAccountRepository.findAll();
                    StringBuilder sb = new StringBuilder("");
                    if (accountList.size() > 0) {
                        for (Sys_Account account:accountList) {
                            Sys_Role sysRole = sysRoleRepository.getOne(account.getRole_id());

                            sb.append("{" +
                                    "\"id\":\"" + account.getId() + "\"," +
                                    "\"user_name\":\"" + account.getUser_name() + "\"," +
                                    "\"role_name\":\"" + sysRole.getRole_name() + "\"," +
                                    "\"tel\":\"" + account.getTel() + "\"," +
                                    "\"remark\":\"" + account.getRemark() + "\"," +
                                    "\"delete_state\":\"" + account.getDelete_state() + "\"," +
                                    "\"create_time\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(account.getCreate_time()) + "\"," +
                                    "\"store_id\":\"" + account.getStore_id() + "\"" +
                                    "},");
                        }
                    }
                    if (sb.toString().contains("user_name")){
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    return "{\"code\": 200,\"msg\":["+sb+"]}";
                }else {
                    return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String getRoleList(HttpServletRequest request) {
        String delete_state = request.getParameter("delete_state");
        try {
            List<Sys_Role> roleList = new ArrayList<>();
            if (delete_state != null && !"".equals(delete_state)) {
                roleList = sysRoleRepository.getByDeletState(Integer.parseInt(delete_state));
            } else {
                roleList = sysRoleRepository.getAllRole();
            }
            StringBuilder sb = new StringBuilder("");
            if (roleList.size() > 0) {
                for (Sys_Role role : roleList) {
                    sb.append("{" +
                            "\"id\":\"" + role.getId() + "\"," +
                            "\"role_name\":\"" + role.getRole_name() + "\"," +
                            "\"role_intro\":\"" + role.getRole_intro() + "\"," +
                            "\"role_permission\":\"" + role.getRole_permission() + "\"," +
                            "\"role_code\":\"" + role.getRole_code() + "\"," +
                            "\"create_time\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(role.getCreate_time()) + "\"," +
                            "\"creator\":\"" + role.getCreator() + "\"," +
                            "\"delete_state\":\"" + role.getDelete_state() + "\"},");
                }
                if (sb.toString().contains("role")) {
                    sb = sb.deleteCharAt(sb.length() - 1);
                }
            }
            return Base64Util.encoderBASE64("{\"code\": 200,\"msg\": [" + sb + "]}");

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String createRole(HttpServletRequest request) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
//            String reqMsg = request.getParameter("reqMsg");
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object role_code = jsonObject.get("role_code");
                Object role_name = jsonObject.get("role_name");
                Object role_intro = jsonObject.get("role_intro");
                Object role_permission = jsonObject.get("role_permission");
                Object creator = jsonObject.get("creator");
                if (role_code != null && !"".equals(role_code) && role_name != null && !"".equals(role_name)
                        && role_intro != null && !"".equals(role_intro) && role_permission != null && !"".equals(role_permission)
                        && creator != null && !"".equals(creator)) {

                    Sys_Role role = new Sys_Role();
                    role.setRole_code(role_code.toString());
                    role.setRole_name(role_name.toString());
                    role.setRole_intro(role_intro.toString());
                    role.setRole_permission(role_permission.toString());
                    role.setCreate_time(BigInteger.valueOf(System.currentTimeMillis()));
                    role.setCreator(creator.toString());
                    role.setDelete_state(0);
                    sysRoleRepository.save(role);
                    saveLog(creator.toString(), "创建新角色：" + role_name);
                    return Base64Util.encoderBASE64("{\"code\": 200,\"msg\": \"角色创建成功\"}");
                } else {
                    return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                }
            } else {
                return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "eyJjb2RlIjogMjAxLCJtc2ciOiAi57O757uf5byC5bi4In0=";
        }
    }

    @Override
    public String updateRole(HttpServletRequest request) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
//            String reqMsg = request.getParameter("reqMsg");
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object creator = jsonObject.get("creator");
                Object role_id = jsonObject.get("role_id");
                Object role_name = jsonObject.get("role_name");
                Object role_intro = jsonObject.get("role_intro");
                Object role_permission = jsonObject.get("role_permission");
                Object delete_state = jsonObject.get("delete_state");
                if (creator != null && !"".equals(creator) && role_id != null && !"".equals(role_id)) {

                    Sys_Role role = sysRoleRepository.getOne(Integer.valueOf(role_id.toString()));
                    if (role_name != null && !"".equals(role_name)) {
                        role.setRole_name(role_name.toString());
                    }
                    if (role_intro != null && !"".equals(role_intro)) {
                        role.setRole_intro(role_intro.toString());
                    }
                    if (delete_state != null && !"".equals(delete_state)) {
                        role.setDelete_state(Integer.parseInt(delete_state.toString()));
                    }
                    if (role_permission != null && !"".equals(role_permission)) {
                        role.setRole_permission(role_permission.toString());
                    }
                    sysRoleRepository.save(role);
                    saveLog(creator.toString(), "修改用户角色信息：" + role_id + ",角色名称：" + role_name + ",权限：" + role_permission + ",删除状态:" + delete_state);
                    return Base64Util.encoderBASE64("{\"code\": 200,\"msg\": \"角色修改成功\"}");
                } else {
                    return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                }
            } else {
                return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "eyJjb2RlIjogMjAxLCJtc2ciOiAi57O757uf5byC5bi4In0=";
        }
    }


    public void saveLog(String user_name, String operation_log) {

        Sys_Operation_Log sysOperationLog = new Sys_Operation_Log();
        sysOperationLog.setUser_name(user_name);
        sysOperationLog.setOperation_time(BigInteger.valueOf(System.currentTimeMillis()));
        sysOperationLog.setOperation_info(operation_log);
        sysOperationLogRepository.save(sysOperationLog);
    }

}
