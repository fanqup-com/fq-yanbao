package com.fanqing.dao;

import com.fanqing.bean.Che_Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Che_Car,Integer>{


    @Query(value = "SELECT * from che_car t where t.series_id = ?1 AND t.model_year+8 >= DATE_FORMAT(NOW(), '%Y') ORDER BY model_year desc", nativeQuery = true)
    List<Che_Car> getCarList(int series_id);

    @Query(value = "SELECT * from che_car t where t.series_id = ?1 AND model_name LIKE %?2% AND t.model_year+8 >= DATE_FORMAT(NOW(), '%Y') ORDER BY model_year desc", nativeQuery = true)
    List<Che_Car> getCarListLike(int series_id,String model_name);

}
