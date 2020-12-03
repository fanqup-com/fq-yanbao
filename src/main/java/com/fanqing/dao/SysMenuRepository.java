package com.fanqing.dao;

import com.fanqing.bean.Sys_Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysMenuRepository extends JpaRepository<Sys_Menu, Integer> {

    @Query(value = "SELECT * from sys_menu t where t.id = ?1 and delete_state = 0", nativeQuery = true)
    Sys_Menu getById(int id);

    @Query(value = "SELECT * from sys_menu t where if(?1=null && ?1 != '',delete_state = ?1,1=1)", nativeQuery = true)
    List<Sys_Menu> getByDeletState(int delete_state);

    @Query(value = "SELECT * from sys_menu", nativeQuery = true)
    List<Sys_Menu> getAllMenu();

}
