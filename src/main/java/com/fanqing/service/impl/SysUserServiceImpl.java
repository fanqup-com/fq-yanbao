package com.fanqing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fanqing.bean.*;
import com.fanqing.dao.*;
import com.fanqing.service.SysUserService;
import com.fanqing.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private SysStoreRepository sysStoreRepository;

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
    public String getUserInfo(JSONObject jsonObject1, String token) {
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
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
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
                            menuSb.append("\"meta\":{\"title\":\"" + menu.getMenu_title() + "\",\"icon\":\"" + menu.getMenu_icon() + "\"},");
                            menuSb.append("\"isMenu\":\"" + menu.getIs_menu() + "\",");
                            menuSb.append("\"hasChild\":\"" + menu.getHas_child() + "\",");
                            menuSb.append("\"father\":\"" + menu.getMenu_father() + "\"");
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
                                "\"name\":\"" + account.getName() + "\"," +
                                "\"email\":\"" + account.getEmail() + "\"," +
                                "\"is_employee\":\"" + account.getIs_employee() + "\"," +
                                "\"remark\":\"" + account.getRemark() + "\"," +
                                "\"store_id\":\"" + account.getStore_id() + "\"," +
                                "\"role_permission\":" + menuSb.toString() + "" +
                                "}}";
                    } else {
                        return "{\"code\": 201,\"msg\": \"用户不存在\"}";
                    }
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }


    @Override
    public String createUser(JSONObject jsonObject1, String token) {
        String user_name = jsonObject1.getString("user_name");
        String password = jsonObject1.getString("password");
        String name = jsonObject1.getString("name");
        String is_employee = jsonObject1.getString("is_employee");
        String tel = jsonObject1.getString("tel");
        String email = jsonObject1.getString("email");
        String remark = jsonObject1.getString("remark");
        String store_id = jsonObject1.getString("store_id");
        String role_id = jsonObject1.getString("role_id");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("创建新用户,token：" + token + ",user_name：" + user_name + ",password：" + password + ",name：" + name + ",is_employee：" + is_employee +
                        ",tel：" + tel + ",email：" + email + ",remark：" + remark + ",store_id：" + store_id + ",role_id：" + role_id);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(user_name)) {
                        return "{\"code\": 201,\"msg\": \"用户名不允许为空\"}";
                    } else if (StringUtils.isEmpty(password)) {
                        return "{\"code\": 201,\"msg\": \"密码不允许为空\"}";
                    } else if (StringUtils.isEmpty(store_id)) {
                        return "{\"code\": 201,\"msg\": \"门店id不允许为空\"}";
                    } else if (StringUtils.isEmpty(role_id)) {
                        return "{\"code\": 201,\"msg\": \"角色id不允许为空\"}";
                    }

                    Sys_Account account = new Sys_Account();
                    String password_md5 = DigestUtils.md5DigestAsHex(password.getBytes()).toLowerCase();
                    account.setUser_name(user_name);
                    account.setPassword(password);
                    account.setPassword_md5(password_md5);
                    account.setTel(tel);
                    account.setName(name);
                    account.setIs_employee(Integer.parseInt(is_employee));
                    account.setEmail(email);
                    account.setRemark(remark);
                    account.setCreator(creator.toString());
                    account.setStore_id(Integer.parseInt(store_id));
                    account.setRole_id(Integer.parseInt(role_id));
                    account.setCreate_time(BigInteger.valueOf(System.currentTimeMillis()));
                    account.setDelete_state(0);
                    sysAccountRepository.save(account);
                    saveLog(creator.toString(), "创建用户" + user_name);
                    return "{\"code\": 200,\"msg\": \"用户" + user_name + "创建成功\"}";

                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String updateUser(JSONObject jsonObject1, String token) {
        String user_name = jsonObject1.getString("user_name");
        String password = jsonObject1.getString("password");
        String tel = jsonObject1.getString("tel");
        String name = jsonObject1.getString("name");
        String email = jsonObject1.getString("email");
        String is_employee = jsonObject1.getString("is_employee");
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
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(user_name)) {
                        return "{\"code\": 202,\"msg\": \"用户名不允许为空\"}";
                    }
                    Sys_Account account = sysAccountRepository.getByUserName(user_name);
                    if (account != null) {
                        if (!StringUtils.isEmpty(password)) {
                            String password_md5 = DigestUtils.md5DigestAsHex(password.getBytes()).toLowerCase();
                            account.setPassword_md5(password_md5);
                            account.setPassword(password);
                        }
                        if (!StringUtils.isEmpty(tel)) {
                            account.setTel(tel);
                        }
                        if (!StringUtils.isEmpty(remark)) {
                            account.setRemark(remark);
                        }
                        if (!StringUtils.isEmpty(store_id)) {
                            account.setStore_id(Integer.parseInt(store_id));
                        }
                        if (!StringUtils.isEmpty(role_id)) {
                            account.setRole_id(Integer.parseInt(role_id));
                        }
                        if (!StringUtils.isEmpty(delete_state)) {
                            account.setDelete_state(Integer.parseInt(delete_state));
                        }
                        if (!StringUtils.isEmpty(name)) {
                            account.setName(name);
                        }
                        if (!StringUtils.isEmpty(email)) {
                            account.setEmail(email);
                        }
                        if (!StringUtils.isEmpty(is_employee)) {
                            account.setIs_employee(Integer.parseInt(is_employee));
                        }
                        sysAccountRepository.save(account);
                        saveLog(creator.toString(), "修改用户" + user_name + "信息");
                        return "{\"code\": 200,\"msg\": \"用户【" + user_name + "】信息修改成功\"}";

                    } else {
                        return "{\"code\": 201,\"msg\": \"用户【" + user_name + "】不存在\"}";
                    }
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String getUserList(JSONObject jsonObject1, String token) {
        String page = jsonObject1.getString("page");
        String size = jsonObject1.getString("size");
        String account_user_name = jsonObject1.getString("user_name");
        String name = jsonObject1.getString("name");
        String is_employee = jsonObject1.getString("is_employee");
        String store_id = jsonObject1.getString("store_id");
        String role_id = jsonObject1.getString("role_id");
        String start_time = jsonObject1.getString("start_time");
        String end_time = jsonObject1.getString("end_time");
        String delete_state = jsonObject1.getString("delete_state");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("获取用户列表,token：" + token + ",user_name：" + account_user_name + ",name：" + name + ",is_employee：" + is_employee +
                        ",store_id：" + store_id + ",role_id：" + role_id + ",start_time：" + start_time + ",end_time：" + end_time + ",delete_state：" + delete_state);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object user_name = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (user_name != null && !"".equals(user_name) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    Sort sort = new Sort(Sort.Direction.DESC,"id");
                    int int_page = 0;
                    int int_size = 10000;
                    if (page != null && !"".equals(page)){
                        int_page = Integer.parseInt(page) - 1;
                        if (int_page < 0){
                            int_page = 0;
                        }
                    }
                    if (size != null && !"".equals(size)){
                        int_size = Integer.parseInt(size);
                        if (int_size < 0){
                            int_size = 10000;
                        }
                    }
                    if (start_time == null || "".equals(start_time)){
                        start_time = "1577808000000";
                    }
                    if (end_time == null || "".equals(end_time)){
                        end_time = "4102416000000";
                    }
                    Pageable pageable = new PageRequest(int_page,int_size,sort);
                    Page<Sys_Account> accountList = sysAccountRepository.findPage(account_user_name, name, is_employee, store_id, role_id, delete_state, new BigInteger(start_time), new BigInteger(end_time),pageable);

                    StringBuilder sb = new StringBuilder("");
                    if (accountList.getContent().size() > 0) {
                        for (Sys_Account account : accountList) {
                            Sys_Role sysRole = sysRoleRepository.getOne(account.getRole_id());

                            sb.append("{" +
                                    "\"id\":\"" + account.getId() + "\"," +
                                    "\"user_name\":\"" + account.getUser_name() + "\"," +
                                    "\"role_name\":\"" + sysRole.getRole_name() + "\"," +
                                    "\"tel\":\"" + account.getTel() + "\"," +
                                    "\"name\":\"" + account.getName() + "\"," +
                                    "\"is_employee\":\"" + account.getIs_employee() + "\"," +
                                    "\"email\":\"" + account.getEmail() + "\"," +
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
                    return "{\"code\": 200,\"msg\":\"success\",\"data\": {\"totalElements\":\"" + accountList.getTotalElements() + "\"," +
                            "\"totalPages\":\"" + accountList.getTotalPages() + "\",\"content\":[" + sb + "]}}";
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String getRoleList(JSONObject jsonObject1, String token) {
        String page = jsonObject1.getString("page");
        String size = jsonObject1.getString("size");
        String start_time = jsonObject1.getString("start_time");
        String end_time = jsonObject1.getString("end_time");
        String role_code = jsonObject1.getString("role_code");
        String role_name = jsonObject1.getString("role_name");
        String creator = jsonObject1.getString("creator");
        String delete_state = jsonObject1.getString("delete_state");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("获取角色列表,token：" + token + ",start_time：" + start_time + ",end_time：" + end_time + ",role_code：" + role_code +
                        ",role_name：" + role_name + ",creator：" + creator + ",delete_state：" + delete_state + ",page：" + page + ",size：" + size);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object user_name = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (user_name != null && !"".equals(user_name) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    Sort sort = new Sort(Sort.Direction.DESC,"id");
                    int int_page = 0;
                    int int_size = 10000;
                    if (page != null && !"".equals(page)){
                        int_page = Integer.parseInt(page) - 1;
                        if (int_page < 0){
                            int_page = 0;
                        }
                    }
                    if (size != null && !"".equals(size)){
                        int_size = Integer.parseInt(size);
                        if (int_size < 0){
                            int_size = 10000;
                        }
                    }
                    if (start_time == null || "".equals(start_time)){
                        start_time = "1577808000000";
                    }
                    if (end_time == null || "".equals(end_time)){
                        end_time = "4102416000000";
                    }

                    Pageable pageable = new PageRequest(int_page,int_size,sort);
                    Page<Sys_Role> roleList = sysRoleRepository.findPage(role_code, role_name, creator, new BigInteger(start_time), new BigInteger(end_time),delete_state,pageable);
                    StringBuilder sb = new StringBuilder("");
                    if (roleList.getContent().size() > 0) {
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
                    return "{\"code\": 200,\"msg\":\"success\",\"data\": {\"totalElements\":\"" + roleList.getTotalElements() + "\"," +
                            "\"totalPages\":\"" + roleList.getTotalPages() + "\",\"content\":[" + sb + "]}}";
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String createRole(JSONObject jsonObject1, String token) {
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
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(role_code)) {
                        return "{\"code\": 201,\"msg\": \"角色code不允许为空\"}";
                    } else if (StringUtils.isEmpty(role_name)) {
                        return "{\"code\": 201,\"msg\": \"角色名称不允许为空\"}";
                    } else if (StringUtils.isEmpty(role_permission)) {
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
                    saveLog(creator.toString(), "创建角色" + role_name);
                    return "{\"code\": 200,\"msg\": \"角色" + role_name + "创建成功\"}";
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String updateRole(JSONObject jsonObject1, String token) {
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
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(role_id)) {
                        return "{\"code\": 202,\"msg\": \"角色id不允许为空\"}";
                    }
                    Sys_Role role = sysRoleRepository.getOne(Integer.valueOf(role_id));
                    if (role != null) {
                        if (!StringUtils.isEmpty(role_code)) {
                            role.setRole_code(role_code);
                        }
                        if (!StringUtils.isEmpty(role_name)) {
                            role.setRole_name(role_name);
                        }
                        if (!StringUtils.isEmpty(role_intro)) {
                            role.setRole_intro(role_intro);
                        }
                        if (!StringUtils.isEmpty(role_permission)) {
                            role.setRole_permission(role_permission);
                        }

                        if (!StringUtils.isEmpty(delete_state)) {
                            role.setDelete_state(Integer.parseInt(delete_state));
                        }
                        sysRoleRepository.save(role);
                        saveLog(creator.toString(), "修改角色" + role_id + "信息");
                        return "{\"code\": 200,\"msg\": \"角色【" + role_id + "】信息修改成功\"}";

                    } else {
                        return "{\"code\": 201,\"msg\": \"角色【" + role_id + "】不存在\"}";
                    }
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String getOperationLogList(JSONObject jsonObject1, String token) {
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
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    Sort sort = new Sort(Sort.Direction.DESC,"id");
                    int int_page = 0;
                    int int_size = 10000;
                    if (page != null && !"".equals(page)){
                        int_page = Integer.parseInt(page) - 1;
                        if (int_page < 0){
                            int_page = 0;
                        }
                    }
                    if (size != null && !"".equals(size)){
                        int_size = Integer.parseInt(size);
                        if (int_size < 0){
                            int_size = 10000;
                        }
                    }
                    if (start_time == null || "".equals(start_time)){
                        start_time = "1577808000000";
                    }
                    if (end_time == null || "".equals(end_time)){
                        end_time = "4102416000000";
                    }

                    Pageable pageable = new PageRequest(int_page,int_size,sort);
                    Page<Sys_Operation_Log> operationLogList = sysOperationLogRepository.findPage(user_name, new BigInteger(start_time), new BigInteger(end_time), pageable);
                    if (operationLogList.getContent().size() > 0) {
                        StringBuilder operationLogSb = new StringBuilder("");
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
                        return "{\"code\": 200,\"msg\":\"success\",\"data\": {\"totalElements\":\"" + operationLogList.getTotalElements() + "\"," +
                                "\"totalPages\":\"" + operationLogList.getTotalPages() + "\",\"content\":[" + operationLogSb + "]}}";
                    } else {
                        return "{\"code\": 200,\"msg\":\"success\",\"data\": []}";
                    }
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String getStoreList(JSONObject jsonObject1, String token) {
        String store_name = jsonObject1.getString("store_name");
        String store_province = jsonObject1.getString("store_province");
        String store_city = jsonObject1.getString("store_city");
        String store_code = jsonObject1.getString("store_code");
        String start_time = jsonObject1.getString("start_time");
        String end_time = jsonObject1.getString("end_time");
        String delete_state = jsonObject1.getString("delete_state");
        String page = jsonObject1.getString("page");
        String size = jsonObject1.getString("size");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("获取门店列表,token：" + token + ",store_name：" + store_name + ",store_province：" + store_province + ",store_city：" + store_city +
                        ",store_code：" + store_code + ",start_time：" + start_time + ",end_time：" + end_time + ",delete_state：" + delete_state);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object user_name = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (user_name != null && !"".equals(user_name) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    Sort sort = new Sort(Sort.Direction.DESC,"id");
                    int int_page = 0;
                    int int_size = 10000;
                    if (page != null && !"".equals(page)){
                        int_page = Integer.parseInt(page) - 1;
                        if (int_page < 0){
                            int_page = 0;
                        }
                    }
                    if (size != null && !"".equals(size)){
                        int_size = Integer.parseInt(size);
                        if (int_size < 0){
                            int_size = 10000;
                        }
                    }
                    if (start_time == null || "".equals(start_time)){
                        start_time = "1577808000000";
                    }
                    if (end_time == null || "".equals(end_time)){
                        end_time = "4102416000000";
                    }

                    Pageable pageable = new PageRequest(int_page,int_size,sort);
                    Page<Sys_Store> storeList = sysStoreRepository.findPage(store_name,store_province,store_city,store_code,new BigInteger(start_time),new BigInteger(end_time),delete_state,pageable);
                    StringBuilder sb = new StringBuilder("");
                    if (storeList.getContent().size() > 0) {
                        for (Sys_Store store : storeList) {
                            sb.append("{" +
                                    "\"id\":\"" + store.getId() + "\"," +
                                    "\"store_name\":\"" + store.getStore_name() + "\"," +
                                    "\"store_code\":\"" + store.getStore_code() + "\"," +
                                    "\"store_province\":\"" + store.getStore_province() + "\"," +
                                    "\"store_city\":\"" + store.getStore_city() + "\"," +
                                    "\"store_manager\":\"" + store.getStore_manager() + "\"," +
                                    "\"store_address\":\"" + store.getStore_address() + "\"," +
                                    "\"store_service\":\"" + store.getStore_service() + "\"," +
                                    "\"store_carbrand\":\"" + store.getStore_carbrand() + "\"," +
                                    "\"store_years\":\"" + store.getStore_years() + "\"," +
                                    "\"store_intro\":\"" + store.getStore_intro() + "\"," +
                                    "\"authorized_repairer\":\"" + store.getAuthorized_repairer() + "\"," +
                                    "\"store_tel\":\"" + store.getStore_tel() + "\"," +
                                    "\"store_create_time\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(store.getStore_create_time()) + "\"," +
                                    "\"store_update_time\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(store.getStore_update_time()) + "\"," +
                                    "\"delete_state\":\"" + store.getDelete_state() + "\"},");
                        }
                        if (sb.toString().contains("store_code")) {
                            sb = sb.deleteCharAt(sb.length() - 1);
                        }
                    }
                    return "{\"code\": 200,\"msg\":\"success\",\"data\": {\"totalElements\":\"" + storeList.getTotalElements() + "\"," +
                            "\"totalPages\":\"" + storeList.getTotalPages() + "\",\"content\":[" + sb + "]}}";
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String createStore(JSONObject jsonObject1, String token) {
        String store_name = jsonObject1.getString("store_name");
        String store_code = jsonObject1.getString("store_code");
        String store_province = jsonObject1.getString("store_province");
        String store_city = jsonObject1.getString("store_city");
        String store_manager = jsonObject1.getString("store_manager");
        String store_address = jsonObject1.getString("store_address");
        String store_service = jsonObject1.getString("store_service");
        String store_carbrand = jsonObject1.getString("store_carbrand");
        String store_years = jsonObject1.getString("store_years");
        String store_intro = jsonObject1.getString("store_intro");
        String authorized_repairer = jsonObject1.getString("authorized_repairer");
        String store_tel = jsonObject1.getString("store_tel");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("创建新门店,token：" + token + ",store_name：" + store_name + ",store_code：" + store_code + ",store_province：" + store_province +
                        ",store_city：" + store_city + ",store_manager：" + store_manager + ",store_address：" + store_address + ",store_service：" + store_service +
                        ",store_carbrand：" + store_carbrand + ",store_years：" + store_years + ",store_intro：" + store_intro + ",authorized_repairer：" + authorized_repairer +
                        ",store_tel：" + store_tel);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(store_code)) {
                        return "{\"code\": 201,\"msg\": \"门店编码不允许为空\"}";
                    }
                    if (StringUtils.isEmpty(store_province)) {
                        return "{\"code\": 201,\"msg\": \"门店所在省份不允许为空\"}";
                    }
                    if (StringUtils.isEmpty(store_city)) {
                        return "{\"code\": 201,\"msg\": \"门店所在市不允许为空\"}";
                    }

                    Sys_Store store = new Sys_Store();
                    store.setStore_name(store_name);
                    store.setStore_code(store_code);
                    store.setStore_province(store_province);
                    store.setStore_city(store_city);
                    store.setStore_manager(store_manager);
                    store.setStore_address(store_address);
                    store.setStore_service(store_service);
                    store.setStore_carbrand(store_carbrand);
                    store.setStore_years(store_years);
                    store.setStore_intro(store_intro);
                    store.setAuthorized_repairer(authorized_repairer);
                    store.setStore_tel(store_tel);
                    store.setStore_create_time(BigInteger.valueOf(System.currentTimeMillis()));
                    store.setStore_update_time(BigInteger.valueOf(System.currentTimeMillis()));
                    store.setDelete_state(0);
                    sysStoreRepository.save(store);
                    saveLog(creator.toString(), "创建新门店" + store_name);
                    return "{\"code\": 200,\"msg\": \"门店" + store_name + "创建成功\"}";
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String updateStore(JSONObject jsonObject1, String token) {
        String store_id = jsonObject1.getString("store_id");
        String store_name = jsonObject1.getString("store_name");
        String store_code = jsonObject1.getString("store_code");
        String store_province = jsonObject1.getString("store_province");
        String store_city = jsonObject1.getString("store_city");
        String store_manager = jsonObject1.getString("store_manager");
        String store_address = jsonObject1.getString("store_address");
        String store_service = jsonObject1.getString("store_service");
        String store_carbrand = jsonObject1.getString("store_carbrand");
        String store_years = jsonObject1.getString("store_years");
        String store_intro = jsonObject1.getString("store_intro");
        String authorized_repairer = jsonObject1.getString("authorized_repairer");
        String store_tel = jsonObject1.getString("store_tel");
        String delete_state = jsonObject1.getString("delete_state");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("修改门店信息,token：" + token + ",store_name：" + store_name + ",store_code：" + store_code + ",store_province：" + store_province +
                        ",store_city：" + store_city + ",store_manager：" + store_manager + ",store_address：" + store_address + ",store_service：" + store_service +
                        ",store_carbrand：" + store_carbrand + ",store_years：" + store_years + ",store_intro：" + store_intro + ",authorized_repairer：" + authorized_repairer +
                        ",store_tel：" + store_tel + ",delete_state：" + delete_state);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(store_id)) {
                        return "{\"code\": 201,\"msg\": \"门店id不允许为空\"}";
                    }
                    Sys_Store store = sysStoreRepository.getOne(Integer.valueOf(store_id));
                    if (store != null) {
                        if (!StringUtils.isEmpty(store_name)) {
                            store.setStore_name(store_name);
                        }
                        if (!StringUtils.isEmpty(store_code)) {
                            store.setStore_code(store_code);
                        }
                        if (!StringUtils.isEmpty(store_province)) {
                            store.setStore_province(store_province);
                        }
                        if (!StringUtils.isEmpty(store_city)) {
                            store.setStore_city(store_city);
                        }
                        if (!StringUtils.isEmpty(store_manager)) {
                            store.setStore_manager(store_manager);
                        }
                        if (!StringUtils.isEmpty(store_address)) {
                            store.setStore_manager(store_address);
                        }
                        if (!StringUtils.isEmpty(store_service)) {
                            store.setStore_service(store_service);
                        }
                        if (!StringUtils.isEmpty(store_carbrand)) {
                            store.setStore_carbrand(store_carbrand);
                        }
                        if (!StringUtils.isEmpty(store_years)) {
                            store.setStore_years(store_years);
                        }
                        if (!StringUtils.isEmpty(store_intro)) {
                            store.setStore_intro(store_intro);
                        }
                        if (!StringUtils.isEmpty(authorized_repairer)) {
                            store.setAuthorized_repairer(authorized_repairer);
                        }
                        if (!StringUtils.isEmpty(store_tel)) {
                            store.setStore_tel(store_tel);
                        }
                        if (!StringUtils.isEmpty(delete_state)) {
                            store.setDelete_state(Integer.parseInt(delete_state));
                        }
                        sysStoreRepository.save(store);
                        saveLog(creator.toString(), "修改门店" + store_id + "信息");
                        return "{\"code\": 200,\"msg\": \"门店【" + store_id + "】信息修改成功\"}";

                    } else {
                        return "{\"code\": 201,\"msg\": \"门店【" + store_id + "】不存在\"}";
                    }
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
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
