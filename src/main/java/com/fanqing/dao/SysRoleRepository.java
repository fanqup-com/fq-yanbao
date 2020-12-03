package com.fanqing.dao;

import com.fanqing.bean.Sys_Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysRoleRepository extends JpaRepository<Sys_Role,Integer> {

    @Query(value = "SELECT * from sys_role where if(?1=null && ?1 != '',delete_state = ?1,1=1)", nativeQuery = true)
    List<Sys_Role> getByDeletState(int delete_state);

    @Query(value = "SELECT * from sys_role", nativeQuery = true)
    List<Sys_Role> getAllRole();



}
