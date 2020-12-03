package com.fanqing.service.impl;

import com.fanqing.bean.Sys_Menu;
import com.fanqing.dao.SysMenuRepository;
import com.fanqing.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service(value = "menuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Override
    public String getMenuList(HttpServletRequest request) {
        String delete_state = request.getParameter("delete_state");
        try {
            List<Sys_Menu> menuList = new ArrayList<>();
            if (delete_state != null && !"".equals(delete_state)){
                menuList = sysMenuRepository.getByDeletState(Integer.parseInt(delete_state));
            }else {
                menuList = sysMenuRepository.getAllMenu();
            }
            if (menuList.size() > 0) {
                StringBuilder menuSb = new StringBuilder("[");
                for (Sys_Menu menu : menuList) {
                    menuSb.append("{");
                    menuSb.append("\"id\":\"" + menu.getId() + "\",");
                    menuSb.append("\"menu_addr\":\"" + menu.getMenu_addr() + "\",");
                    menuSb.append("\"menu_title\":\"" + menu.getMenu_title() + "\",");
                    menuSb.append("\"menu_intro\":\"" + menu.getMenu_intro() + "\",");
                    menuSb.append("\"menu_create_time\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(menu.getMenu_create_time()) + "\",");
                    menuSb.append("\"menu_update_time\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(menu.getMenu_update_time()) + "\",");
                    menuSb.append("\"delete_state\":\"" + menu.getDelete_state() + "\"");
                    menuSb.append("},");
                }
                if (menuSb.toString().contains("menu_addr")) {
                    menuSb = menuSb.deleteCharAt(menuSb.length() - 1);
                }
                menuSb.append("]");
                return "{\"code\": 200,\"msg\":"+menuSb+"}";
            } else {
                return "{\"code\": 201,\"msg\": []}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"参数异常\"}";
        }
    }
}
