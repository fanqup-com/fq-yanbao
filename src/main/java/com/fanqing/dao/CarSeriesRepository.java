package com.fanqing.dao;

import com.fanqing.bean.Che_CarSeries;
import com.fanqing.bean.Che_City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarSeriesRepository extends JpaRepository<Che_CarSeries,Integer>{


    @Query(value = "SELECT * from che_carseries t where t.brand_id = ?1", nativeQuery = true)
    List<Che_CarSeries> getSeriesList(int brand_id);

    @Query(value = "SELECT * from che_carseries t where t.brand_id = ?1 AND t.series_name LIKE %?2%", nativeQuery = true)
    List<Che_CarSeries> getSeriesListLike(int brand_id,String series_name);

}
