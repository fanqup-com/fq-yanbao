package com.fanqing.dao;

import com.fanqing.bean.Che_City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<Che_City,Integer>{

    @Query(value = "SELECT * from che_city GROUP BY prov_id", nativeQuery = true)
    List<Che_City> getProvinceList();

    @Query(value = "SELECT * from che_city where prov_id = ?1", nativeQuery = true)
    List<Che_City> getCityList(int id);


}
