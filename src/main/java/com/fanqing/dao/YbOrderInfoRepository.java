package com.fanqing.dao;

import com.fanqing.bean.Yb_Order_Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface YbOrderInfoRepository extends JpaRepository<Yb_Order_Info,Integer>{

    @Query(value = "SELECT * from yb_order_info t where t.yb_order_id = ?1", nativeQuery = true)
    Yb_Order_Info getByYbOrderId(String yb_order_id);


    @Query(value = "SELECT * from yb_order_info t where 1=1 AND if(?1=null && ?1 != '',yb_order_id = ?1,1=1) AND if(?2=null && ?2 != '',yb_customer_name = ?2,1=1) " +
            "and if(?3=null && ?3 != '',yb_creator = ?3,1=1) and if(?4=null && ?4 != '',yb_car_number = ?4,1=1) and if(?5=null && ?5 != '',yb_car_vin_number = ?5,1=1) " +
            "and if(?6=null && ?6 != '',yb_order_store = ?6,1=1) and yb_create_time BETWEEN ?7 and ?8", nativeQuery = true)
    List<Yb_Order_Info> getOrderList(String yb_order_id, String yb_customer_name, String yb_creator, String yb_car_number, String yb_car_vin_number,
                                     String yb_order_store, BigInteger create_time_start,BigInteger create_time_end);

}
