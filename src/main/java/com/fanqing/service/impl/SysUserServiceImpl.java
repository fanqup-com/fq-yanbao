package com.fanqing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fanqing.bean.*;
import com.fanqing.dao.SysAccountRepository;
import com.fanqing.dao.SysMenuRepository;
import com.fanqing.dao.SysOperationLogRepository;
import com.fanqing.dao.SysRoleRepository;
import com.fanqing.service.SysUserService;
import com.fanqing.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.math.BigInteger;
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

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Override
    public String userLogin(JSONObject jsonObject) {
        String user_name = jsonObject.getString("user_name");
        String password_md5 = jsonObject.getString("password");
        logger.info("用户登录,用户名：" + user_name + ",密码：" + password_md5);
        try {
            if (user_name != null && !"".equals(user_name) && password_md5 != null && !"".equals(password_md5)) {
                Sys_Account account = sysAccountRepository.getAccount(user_name, password_md5);
                if (account != null) {
                    long limit_time = System.currentTimeMillis() + 604800000;
                    String tokenJson = "{\"user_name\":\"" + user_name + "\",\"limit_time\":\"" + limit_time + "\"}";
                    System.out.println(tokenJson);
                    String token = Base64Util.encoderBASE64(tokenJson).
                            replaceAll("\n", "").
                            replaceAll("\r", "").
                            replaceAll("\t", "").
                            trim();

                    return "{\"code\": 200,\"msg\":\"success\",\"data\":{\"token\":\"" + token + "\"}}";
                } else {
                    return "{\"code\": 201,\"msg\": \"用户名或密码错误\"}";
                }
            } else {
                return "{\"code\": 201,\"msg\": \"参数异常\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String getUserInfo(JSONObject jsonObject1) {
        String token = jsonObject1.getString("token");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("获取个人信息,token：" + token);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object user_name = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (user_name != null && !"".equals(user_name) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    Sys_Account account = sysAccountRepository.getByUserName(user_name.toString());
                    if (account != null) {
                        Sys_Role sysRole = sysRoleRepository.getOne(account.getRole_id());
                        StringBuilder menuSb = new StringBuilder("[");
                        String[] menuArr = sysRole.getRole_permission().split(",");
                        for (String menuid : menuArr) {
                            Sys_Menu menu = sysMenuRepository.getById(Integer.parseInt(menuid));
                            menuSb.append("{");
                            menuSb.append("\"menu_path\":\"" + menu.getMenu_path() + "\",");
                            menuSb.append("\"menu_name\":\"" + menu.getMenu_name() + "\",");
                            menuSb.append("\"route_name\":\"" + menu.getMenu_route_name() + "\",");
                            menuSb.append("\"meta\":{\"title\":\""+menu.getMenu_title()+"\",\"icon\":\""+menu.getMenu_icon()+"\"},");
                            menuSb.append("\"isMenu\":\""+menu.getIs_menu()+"\",");
                            menuSb.append("\"hasChild\":\""+menu.getHas_child()+"\",");
                            menuSb.append("\"father\":\""+menu.getMenu_father()+"\"");
                            menuSb.append("},");
                        }
                        if (menuSb.toString().contains("path")) {
                            menuSb = menuSb.deleteCharAt(menuSb.length() - 1);
                        }
                        menuSb.append("]");

                        return "{\"code\": 200,\"msg\":\"success\",\"data\":{" +
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
                } else {
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
    public String createUser(JSONObject jsonObject1) {
        String token = jsonObject1.getString("token");
        String user_name = jsonObject1.getString("user_name");
        String password = jsonObject1.getString("password");
        String tel = jsonObject1.getString("tel");
        String remark = jsonObject1.getString("remark");
        String store_id = jsonObject1.getString("store_id");
        String role_id = jsonObject1.getString("role_id");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("创建新用户,token：" + token + ",user_name：" + user_name + ",password：" + password +
                        ",tel：" + tel + ",remark：" + remark + ",store_id：" + store_id + ",role_id：" + role_id);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(user_name)){
                        return "{\"code\": 201,\"msg\": \"用户名不允许为空\"}";
                    }else if (StringUtils.isEmpty(password)){
                        return "{\"code\": 201,\"msg\": \"密码不允许为空\"}";
                    }else if (StringUtils.isEmpty(store_id)){
                        return "{\"code\": 201,\"msg\": \"门店id不允许为空\"}";
                    }else if (StringUtils.isEmpty(role_id)){
                        return "{\"code\": 201,\"msg\": \"角色id不允许为空\"}";
                    }

                    Sys_Account account = new Sys_Account();
                    String password_md5 = DigestUtils.md5DigestAsHex(password.getBytes()).toLowerCase();
                    account.setUser_name(user_name);
                    account.setPassword(password);
                    account.setPassword_md5(password_md5);
                    account.setTel(tel);
                    account.setRemark(remark);
                    account.setCreator(creator.toString());
                    account.setStore_id(Integer.parseInt(store_id));
                    account.setRole_id(Integer.parseInt(role_id));
                    account.setCreate_time(BigInteger.valueOf(System.currentTimeMillis()));
                    account.setDelete_state(0);
                    sysAccountRepository.save(account);
                    saveLog(creator.toString(),"创建用户" + user_name);
                    return "{\"code\": 200,\"msg\": \"用户"+user_name+"创建成功\"}";

                } else {
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
    public String updateUser(JSONObject jsonObject1) {
        String token = jsonObject1.getString("token");
        String user_name = jsonObject1.getString("user_name");
        String password = jsonObject1.getString("password");
        String tel = jsonObject1.getString("tel");
        String remark = jsonObject1.getString("remark");
        String store_id = jsonObject1.getString("store_id");
        String role_id = jsonObject1.getString("role_id");
        String delete_state = jsonObject1.getString("delete_state");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("修改用户信息,token：" + token + ",user_name：" + user_name + ",password：" + password +
                        ",tel：" + tel + ",remark：" + remark + ",store_id：" + store_id + ",role_id：" + role_id + ",delete_state：" + delete_state);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(user_name)){
                        return "{\"code\": 201,\"msg\": \"用户名不允许为空\"}";
                    }
                    Sys_Account account = sysAccountRepository.getByUserName(user_name);
                    if (account != null){
                        if (!StringUtils.isEmpty(password)){
                            String password_md5 = DigestUtils.md5DigestAsHex(password.getBytes()).toLowerCase();
                            account.setPassword_md5(password_md5);
                            account.setPassword(password);
                        }
                        if (!StringUtils.isEmpty(tel)){
                            account.setTel(tel);
                        }
                        if (!StringUtils.isEmpty(remark)){
                            account.setRemark(remark);
                        }
                        if (!StringUtils.isEmpty(store_id)){
                            account.setStore_id(Integer.parseInt(store_id));
                        }
                        if (!StringUtils.isEmpty(role_id)){
                            account.setRole_id(Integer.parseInt(role_id));
                        }
                        if (!StringUtils.isEmpty(delete_state)){
                            account.setDelete_state(Integer.parseInt(delete_state));
                        }
                        sysAccountRepository.save(account);
                        saveLog(creator.toString(),"修改用户" + user_name + "信息");
                        return "{\"code\": 200,\"msg\": \"用户【"+user_name+"】信息修改成功\"}";

                    }else {
                        return "{\"code\": 201,\"msg\": \"用户【"+user_name+"】不存在\"}";
                    }
                } else {
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
    public String getUserList(JSONObject jsonObject1) {
        String token = jsonObject1.getString("token");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("获取用户列表,token：" + token);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object user_name = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (user_name != null && !"".equals(user_name) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    List<Sys_Account> accountList = sysAccountRepository.findAll();
                    StringBuilder sb = new StringBuilder("");
                    if (accountList.size() > 0) {
                        for (Sys_Account account : accountList) {
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
                    if (sb.toString().contains("user_name")) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    return "{\"code\": 200,\"msg\":\"success\",\"data\":[" + sb + "]}";
                } else {
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
    public String getRoleList(JSONObject jsonObject1) {
        String token = jsonObject1.getString("token");
        String delete_state =jsonObject1.getString("delete_state");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("获取角色列表,token：" + token + ",delete_state：" + delete_state);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object user_name = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (user_name != null && !"".equals(user_name) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                    }
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
                                    "\"role_code\":\"" + role.getRole_code() + "\"," +
                                    "\"role_name\":\"" + role.getRole_name() + "\"," +
                                    "\"role_intro\":\"" + role.getRole_intro() + "\"," +
                                    "\"create_time\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(role.getCreate_time()) + "\"," +
                                    "\"creator\":\"" + role.getCreator() + "\"," +
                                    "\"role_permission\":\"" + role.getRole_permission() + "\"," +
                                    "\"delete_state\":\"" + role.getDelete_state() + "\"},");
                        }
                        if (sb.toString().contains("role")) {
                            sb = sb.deleteCharAt(sb.length() - 1);
                        }
                    }
                    return "{\"code\": 200,\"msg\":\"success\",\"data\": [" + sb + "]}";
                } else {
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
    public String createRole(JSONObject jsonObject1) {
        String token = jsonObject1.getString("token");
        String role_code = jsonObject1.getString("role_code");
        String role_name = jsonObject1.getString("role_name");
        String role_intro = jsonObject1.getString("role_code");
        String role_permission = jsonObject1.getString("role_permission");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("创建新角色,token：" + token + ",role_code：" + role_code + ",role_name：" + role_name +
                        ",role_intro：" + role_intro + ",role_permission：" + role_permission);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(role_code)){
                        return "{\"code\": 201,\"msg\": \"角色code不允许为空\"}";
                    }else if (StringUtils.isEmpty(role_name)){
                        return "{\"code\": 201,\"msg\": \"角色名称不允许为空\"}";
                    }else if (StringUtils.isEmpty(role_permission)){
                        return "{\"code\": 201,\"msg\": \"角色权限不允许为空\"}";
                    }

                    Sys_Role role = new Sys_Role();
                    role.setRole_code(role_code);
                    role.setRole_name(role_name);
                    role.setRole_intro(role_intro);
                    role.setRole_permission(role_permission);
                    role.setCreator(creator.toString());
                    role.setCreate_time(BigInteger.valueOf(System.currentTimeMillis()));
                    role.setDelete_state(0);
                    sysRoleRepository.save(role);
                    saveLog(creator.toString(),"创建角色" + role_name);
                    return "{\"code\": 200,\"msg\": \"角色"+role_name+"创建成功\"}";
                } else {
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
    public String updateRole(JSONObject jsonObject1) {
        String token = jsonObject1.getString("token");
        String role_id = jsonObject1.getString("role_id");
        String role_code = jsonObject1.getString("role_code");
        String role_name = jsonObject1.getString("role_name");
        String role_intro = jsonObject1.getString("role_code");
        String role_permission = jsonObject1.getString("role_permission");
        String delete_state = jsonObject1.getString("delete_state");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("修改用户信息,token：" + token + ",role_id：" + role_id + ",role_code：" + role_code + ",role_name：" + role_name +
                        ",role_intro：" + role_intro + ",role_permission：" + role_permission + ",delete_state：" + delete_state);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(role_id)){
                        return "{\"code\": 201,\"msg\": \"角色id不允许为空\"}";
                    }
                    Sys_Role role = sysRoleRepository.getOne(Integer.valueOf(role_id));
                    if (role != null){
                        if (!StringUtils.isEmpty(role_code)){
                            role.setRole_code(role_code);
                        }
                        if (!StringUtils.isEmpty(role_name)){
                            role.setRole_name(role_name);
                        }
                        if (!StringUtils.isEmpty(role_intro)){
                            role.setRole_intro(role_intro);
                        }
                        if (!StringUtils.isEmpty(role_permission)){
                            role.setRole_permission(role_permission);
                        }

                        if (!StringUtils.isEmpty(delete_state)){
                            role.setDelete_state(Integer.parseInt(delete_state));
                        }
                        sysRoleRepository.save(role);
                        saveLog(creator.toString(),"修改角色" + role_id + "信息");
                        return "{\"code\": 200,\"msg\": \"角色【"+role_id+"】信息修改成功\"}";

                    }else {
                        return "{\"code\": 201,\"msg\": \"角色【"+role_id+"】不存在\"}";
                    }
                } else {
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
    public String getOperationLogList(JSONObject jsonObject1) {
        String token = jsonObject1.getString("token");
        String user_name = jsonObject1.getString("user_name");
        String start_time = jsonObject1.getString("start_time");
        String end_time = jsonObject1.getString("end_time");
        String size = jsonObject1.getString("size");
        String page = jsonObject1.getString("page");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("查询操作日志,token：" + token + ",user_name：" + user_name + ",start_time：" + start_time + ",end_time：" + end_time +
                        ",size：" + size + ",page：" + page);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(size)){
                        size = "10";
                    }
                    if (StringUtils.isEmpty(page)){
                        page = "1";
                    }
                    BigInteger start_time_long = null;
                    BigInteger end_time_long = null;
                    if (!StringUtils.isEmpty(start_time)){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date d = sdf.parse(start_time);
                        start_time_long = new BigInteger(String.valueOf(d.getTime()));
                    }
                    if (!StringUtils.isEmpty(end_time)){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date d = sdf.parse(end_time);
                        end_time_long = new BigInteger(String.valueOf(d.getTime()));
                    }
                    int page_int = Integer.parseInt(page) - 1;
                    int size_int = Integer.parseInt(size);


                    List<Sys_Operation_Log> operationLogList = sysOperationLogRepository.queryList(user_name,start_time_long,end_time_long,page_int*size_int,size_int);
                    if (operationLogList.size() > 0){
                        StringBuilder operationLogSb = new StringBuilder("[");
                        for (Sys_Operation_Log operationLog : operationLogList) {
                            operationLogSb.append("{");
                            operationLogSb.append("\"id\":\"" + operationLog.getId() + "\",");
                            operationLogSb.append("\"user_name\":\"" + operationLog.getUser_name() + "\",");
                            operationLogSb.append("\"operation_time\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(operationLog.getOperation_time()) + "\",");
                            operationLogSb.append("\"operation_info\":\"" + operationLog.getOperation_info() + "\"");
                            operationLogSb.append("},");
                        }
                        if (operationLogSb.toString().contains("user_name")) {
                            operationLogSb = operationLogSb.deleteCharAt(operationLogSb.length() - 1);
                        }
                        operationLogSb.append("]");
                        return "{\"code\": 200,\"msg\":\"success\",\"data\":" + operationLogSb + "}";
                    }else {
                        return "{\"code\": 200,\"msg\":\"success\",\"data\": []}";
                    }
                } else {
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


    public void saveLog(String user_name, String operation_log) {

        Sys_Operation_Log sysOperationLog = new Sys_Operation_Log();
        sysOperationLog.setUser_name(user_name);
        sysOperationLog.setOperation_time(BigInteger.valueOf(System.currentTimeMillis()));
        sysOperationLog.setOperation_info(operation_log);
        sysOperationLogRepository.save(sysOperationLog);
    }

}
