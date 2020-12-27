package com.fanqing.dao;

import com.fanqing.bean.Che_CarBrand;
import com.fanqing.bean.Sys_fixed_quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FixedQuotationRepository extends JpaRepository<Sys_fixed_quotation,Integer> {

    @Query(value = "SELECT * from sys_fixed_quotation t where t.brand_id = ?1 and t.series_id = ?2 and t.service_id = ?3 " +
            "and t.liter = ?4 and t.is_during_insurance = ?5 and t.is_new_car = ?6 and t.delete_state = 0", nativeQuery = true)
    Sys_fixed_quotation getPrice(int brand_id ,int series_id ,int service_id ,String liter ,int is_during_insurance ,int is_new_car);

}
