package com.fanqing.dao;

import com.fanqing.bean.Che_Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Che_Service,Integer> {

    @Query(value = "SELECT * from che_service where service_name in (?1) and delete_state = 0", nativeQuery = true)
    List<Che_Service> getAllByServiceName(List<String> ServiceNameList);
}
