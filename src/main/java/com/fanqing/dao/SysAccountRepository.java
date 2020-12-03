package com.fanqing.dao;

import com.fanqing.bean.Che_CarBrand;
import com.fanqing.bean.Sys_Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysAccountRepository extends JpaRepository<Sys_Account,Integer> {


    @Query(value = "SELECT * from sys_account t where t.user_name = ?1 and t.password_md5 = ?2", nativeQuery = true)
    Sys_Account getAccount(String user_name,String password_md5);

    @Query(value = "SELECT * from sys_account t where t.user_name = ?1 ", nativeQuery = true)
    Sys_Account getByUserName(String user_name);




}
