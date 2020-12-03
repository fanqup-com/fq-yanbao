package com.fanqing.dao;

import com.fanqing.bean.Ins_projectservice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectServiceRepository extends JpaRepository<Ins_projectservice,Integer>{


    @Query(value = "SELECT PS_Id,PS_Name,PS_Diy from ins_projectservice", nativeQuery = true)
    List<Object[]> findProjectInfo();

    @Query(value = "SELECT * from ins_projectservice where PS_Id = ?1 LIMIT 1", nativeQuery = true)
    Ins_projectservice getProjectById(int pid);

}
