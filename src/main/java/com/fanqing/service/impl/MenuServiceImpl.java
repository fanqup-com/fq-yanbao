package com.fanqing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fanqing.bean.Sys_Account;
import com.fanqing.bean.Sys_Menu;
import com.fanqing.dao.SysMenuRepository;
import com.fanqing.service.MenuService;
import com.fanqing.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service(value = "menuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private SysMenuRepository sysMenuRepository;

    private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Override
    public String getMenuList(JSONObject jsonObject1) {
        String token = jsonObject1.getString("token");
        String delete_state = jsonObject1.getString("delete_state");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("获取菜单列表,token：" + token + ",delete_state：" + delete_state);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 201,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    List<Sys_Menu> menuList = new ArrayList<>();
                    if (delete_state != null && !"".equals(delete_state)) {
                        menuList = sysMenuRepository.getByDeletState(Integer.parseInt(delete_state));
                    } else {
                        menuList = sysMenuRepository.getAllMenu();
                    }
                    if (menuList.size() > 0) {
                        StringBuilder menuSb = new StringBuilder("[");
                        for (Sys_Menu menu : menuList) {
                            menuSb.append("{");
                            menuSb.append("\"id\":\"" + menu.getId() + "\",");
                            menuSb.append("\"menu_path\":\"" + menu.getMenu_path() + "\",");
                            menuSb.append("\"menu_title\":\"" + menu.getMenu_title() + "\",");
                            menuSb.append("\"menu_name\":\"" + menu.getMenu_name() + "\",");
                            menuSb.append("\"menu_route_name\":\"" + menu.getMenu_route_name() + "\",");
                            menuSb.append("\"menu_father\":\"" + menu.getMenu_father() + "\",");
                            menuSb.append("\"menu_icon\":\"" + menu.getMenu_icon() + "\",");
                            menuSb.append("\"has_child\":\"" + menu.getHas_child() + "\",");
                            menuSb.append("\"is_menu\":\"" + menu.getIs_menu() + "\",");
                            menuSb.append("\"menu_create_time\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(menu.getMenu_create_time()) + "\",");
                            menuSb.append("\"menu_update_time\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(menu.getMenu_update_time()) + "\",");
                            menuSb.append("\"delete_state\":\"" + menu.getDelete_state() + "\"");
                            menuSb.append("},");
                        }
                        if (menuSb.toString().contains("menu_path")) {
                            menuSb = menuSb.deleteCharAt(menuSb.length() - 1);
                        }
                        menuSb.append("]");
                        return "{\"code\": 200,\"msg\":\"success\",\"data\":" + menuSb + "}";
                    } else {
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
            return "{\"code\": 201,\"msg\": \"参数异常\"}";
        }
    }
}
