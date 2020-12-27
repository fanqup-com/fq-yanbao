package com.fanqing.dao;

import com.fanqing.bean.Sys_Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface SysRoleRepository extends JpaRepository<Sys_Role, Integer> {

    @Query(value = "SELECT * from sys_role where if(?1=null && ?1 != '',delete_state = ?1,1=1)", nativeQuery = true)
    List<Sys_Role> getByDeletState(int delete_state);

    @Query(value = "SELECT * from sys_role", nativeQuery = true)
    List<Sys_Role> getAllRole();

    @Query(value = "SELECT * from sys_role where 1=1 " +
            "and if(?1 is not null && ?1 <> '',role_code = ?1,1=1) " +
            "and if(?2 is not null && ?2 <> '',role_name like (?2),1=1) " +
            "and if(?3 is not null && ?3 <> '',creator = ?3,1=1) " +
            "and if(?6 is not null && ?6 <> '',delete_state = ?6,1=1) " +
            "and create_time between ?4 and ?5 order by ?#{#pageable}",
            countQuery = "SELECT count(*) from sys_role where 1=1 " +
                    "and if(?1 is not null && ?1 <> '',role_code = ?1,1=1) " +
                    "and if(?2 is not null && ?2 <> '',role_name like (?2),1=1) " +
                    "and if(?3 is not null && ?3 <> '',creator = ?3,1=1) " +
                    "and if(?6 is not null && ?6 <> '',delete_state = ?6,1=1) " +
                    "and create_time between ?4 and ?5", nativeQuery = true)
    Page<Sys_Role> findPage(String role_code, String role_name, String creator, BigInteger start_time, BigInteger end_time, String delete_state, Pageable pageable);


}
