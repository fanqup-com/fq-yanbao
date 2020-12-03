package com.fanqing.dao;

import com.fanqing.bean.Che_CarBrand;
import com.fanqing.bean.Che_CarSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardBrandRepository extends JpaRepository<Che_CarBrand,Integer>{


    @Query(value = "SELECT * from che_carbrand t where t.brand_name LIKE %?1% ORDER BY initial", nativeQuery = true)
    List<Che_CarBrand> getCarBrandListLike(String brand_name);



}
