package com.fanqing.dao;

import com.fanqing.bean.Sys_Role;
import com.fanqing.bean.Sys_Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface SysStoreRepository extends JpaRepository<Sys_Store,Integer> {

    @Query(value = "SELECT * from sys_store where if(?1=null && ?1 != '',delete_state = ?1,1=1)", nativeQuery = true)
    List<Sys_Store> getByDeletState(int delete_state);

    @Query(value = "SELECT * from sys_store", nativeQuery = true)
    List<Sys_Store> getAllStore();

    @Query(value = "SELECT * from sys_store where store_name in (?1) and delete_state = 1", nativeQuery = true)
    List<Sys_Store> getAllByNameList(List<String> storeNameList);

    @Query(value = "SELECT * from sys_store where 1=1 " +
            "and if(?1 is not null && ?1 <> '',store_name like %?1%,1=1) " +
            "and if(?2 is not null && ?2 <> '',store_province = ?2,1=1) " +
            "and if(?3 is not null && ?3 <> '',store_city = ?3,1=1) " +
            "and if(?4 is not null && ?4 <> '',store_code = ?4,1=1) " +
            "and if(?7 is not null && ?7 <> '',delete_state = ?7,1=1) " +
            "and store_create_time between ?5 and ?6 order by ?#{#pageable}",
            countQuery = "SELECT count(*) from sys_store where 1=1 " +
                    "and if(?1 is not null && ?1 <> '',store_name like %?1%,1=1) " +
                    "and if(?2 is not null && ?2 <> '',store_province = ?2,1=1) " +
                    "and if(?3 is not null && ?3 <> '',store_city = ?3,1=1) " +
                    "and if(?4 is not null && ?4 <> '',store_code = ?4,1=1) " +
                    "and if(?7 is not null && ?7 <> '',delete_state = ?7,1=1) " +
                    "and store_create_time between ?5 and ?6", nativeQuery = true)
    Page<Sys_Store> findPage(String store_name, String store_province, String store_city, String store_code, BigInteger start_time, BigInteger end_time, String delete_state, Pageable pageable);

}
