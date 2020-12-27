package com.fanqing.dao;

import com.fanqing.bean.Sys_Menu;
import com.fanqing.bean.Sys_Operation_Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface SysOperationLogRepository extends JpaRepository<Sys_Operation_Log,Integer> {

    @Query(value = "SELECT * from sys_operation_log t where if(?1=null && ?1 != '',user_name = ?1,1=1) and if(?2=null && ?2 != '',operation_time > ?2,1=1) " +
            "and if(?3=null && ?3 != '',operation_time < ?3,1=1) ORDER BY id desc LIMIT ?4,?5", nativeQuery = true)
    List<Sys_Operation_Log> queryList(String user_name, BigInteger start_time,BigInteger end_time,int start,int size);

    @Query(value = "SELECT * from sys_operation_log where 1=1 " +
            "and if(?1 is not null && ?1 <> '',user_name like %?1%,1=1) " +
            "and operation_time between ?2 and ?3 order by ?#{#pageable}",
            countQuery = "SELECT count(*) from sys_operation_log where 1=1 " +
                    "and if(?1 is not null && ?1 <> '',user_name like %?1%,1=1) " +
                    "and operation_time between ?2 and ?3", nativeQuery = true)
    Page<Sys_Operation_Log> findPage(String user_name, BigInteger start_time, BigInteger end_time, Pageable pageable);

}
