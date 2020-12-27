package com.fanqing.dao;

import com.fanqing.bean.Che_CarBrand;
import com.fanqing.bean.Sys_Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface SysAccountRepository extends JpaRepository<Sys_Account, Integer> {


    @Query(value = "SELECT * from sys_account t where t.user_name = ?1 and t.password_md5 = ?2 and t.delete_state = 0", nativeQuery = true)
    Sys_Account getAccount(String user_name, String password_md5);

    @Query(value = "SELECT * from sys_account t where t.user_name = ?1 ", nativeQuery = true)
    Sys_Account getByUserName(String user_name);

    @Query(value = "SELECT * from sys_account where 1=1 and if(?1 is not null && ?1 <> '',user_name like %?1%,1=1) " +
            "and if(?2 is not null && ?2 <> '',name like %?2%,1=1) " +
            "and if(?3 is not null && ?3 <> '',is_employee = ?3,1=1) " +
            "and if(?4 is not null && ?4 <> '',store_id = ?4,1=1) " +
            "and if(?5 is not null && ?5 <> '',role_id = ?5,1=1) " +
            "and if(?6 is not null && ?6 <> '',delete_state = ?6,1=1) " +
            "and create_time between ?7 and ?8 order by ?#{#pageable}",
            countQuery = "SELECT count(*) from sys_account where 1=1 and if(?1 is not null && ?1 <> '',user_name like %?1%,1=1) " +
                    "and if(?2 is not null && ?2 <> '',name like %?2%,1=1) " +
                    "and if(?3 is not null && ?3 <> '',is_employee = ?3,1=1) " +
                    "and if(?4 is not null && ?4 <> '',store_id = ?4,1=1) " +
                    "and if(?5 is not null && ?5 <> '',role_id = ?5,1=1) " +
                    "and if(?6 is not null && ?6 <> '',delete_state = ?6,1=1) " +
                    "and create_time between ?7 and ?8", nativeQuery = true)
    Page<Sys_Account> findPage(String user_name, String name, String is_employee, String store_id, String role_id, String delete_state, BigInteger start_time, BigInteger end_time, Pageable pageable);


}
