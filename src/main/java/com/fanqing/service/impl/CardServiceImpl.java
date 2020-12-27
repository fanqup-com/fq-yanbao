package com.fanqing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fanqing.bean.*;
import com.fanqing.dao.*;
import com.fanqing.service.CardService;
import com.fanqing.util.Base64Util;
import com.fanqing.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(value = "cardService")
public class CardServiceImpl implements CardService {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CardBrandRepository cardBrandRepository;
    @Autowired
    private CarSeriesRepository carSeriesRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private SysAccountRepository sysAccountRepository;
    @Autowired
    private SysStoreRepository sysStoreRepository;
    @Autowired
    private YbOrderInfoRepository ybOrderInfoRepository;
    @Autowired
    private SysOperationLogRepository sysOperationLogRepository;
    @Autowired
    private YbOrderInfoBackRepository ybOrderInfoBackRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private FixedQuotationRepository fixedQuotationRepository;

    private static final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    @Override
    public String showCity(JSONObject jsonObject1,String token) {

        String type = jsonObject1.getString("type");
        String id = jsonObject1.getString("id");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("获取省市信息,token：" + token + ",type：" + type + ",id：" + id);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(type)) {
                        return "{\"code\": 201,\"msg\": \"获取省市类型type不允许为空\"}";
                    }
                    StringBuilder sb = new StringBuilder("");
                    if ("1".equals(type)) {
                        List<Che_City> provinceList = cityRepository.getProvinceList();

                        if (provinceList.size() > 0) {
                            for (Che_City province : provinceList) {
                                sb.append("{" +
                                        "\"id\":\"" + province.getProv_id() + "\"," +
                                        "\"name\":\"" + province.getProv_name() + "\"},");
                            }
                            if (sb.toString().contains("name")) {
                                sb = sb.deleteCharAt(sb.length() - 1);
                            }
                        }

                    } else {
                        if (StringUtils.isEmpty(id)) {
                            return "{\"code\": 201,\"msg\": \"获取城市信息省份id不允许为空\"}";
                        }
                        List<Che_City> cityList = cityRepository.getCityList(Integer.parseInt(id));
                        if (cityList.size() > 0) {
                            for (Che_City city : cityList) {
                                sb.append("{" +
                                        "\"id\":\"" + city.getCity_id() + "\"," +
                                        "\"name\":\"" + city.getCity_name() + "\"},");
                            }
                            if (sb.toString().contains("name")) {
                                sb = sb.deleteCharAt(sb.length() - 1);
                            }
                        }
                    }
                    return "{\"code\": 200,\"msg\":\"success\",\"data\":[" + sb + "]}";
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String showCarBrandList(JSONObject jsonObject1,String token) {
        String car_brand_name = jsonObject1.getString("car_brand_name");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("获取车品牌列表,token：" + token + ",car_brand_name：" + car_brand_name);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    Sys_Account account = sysAccountRepository.getByUserName(creator.toString());
                    if (account != null) {
                        StringBuilder sb = new StringBuilder("");
                        int store_Id = account.getStore_id();
                        Sys_Store store = sysStoreRepository.getOne(store_Id);
                        String storeCarBrand = store.getStore_carbrand();
                        String[] carBrandArray = storeCarBrand.split(",");
                        List<String> brandList = new ArrayList<>(Arrays.asList(carBrandArray));
                        List<Che_CarBrand> carBrandList;
                        if (!StringUtils.isEmpty(car_brand_name)) {
                            carBrandList = cardBrandRepository.getCarBrandListLike(car_brand_name, brandList);
                        } else {
                            carBrandList = cardBrandRepository.getAllByCarBrandName(brandList);
                        }
                        if (carBrandList.size() > 0) {
                            for (Che_CarBrand carBrand : carBrandList) {
                                sb.append("{" +
                                        "\"brand_id\":\"" + carBrand.getBrand_id() + "\"," +
                                        "\"brand_name\":\"" + carBrand.getBrand_name() + "\"," +
                                        "\"initial\":\"" + carBrand.getInitial() + "\"},");
                            }
                            if (sb.toString().contains("name")) {
                                sb = sb.deleteCharAt(sb.length() - 1);
                            }
                        }
                        return "{\"code\": 200,\"msg\":\"success\",\"data\":[" + sb + "]}";
                    } else {
                        return "{\"code\": 201,\"msg\": \"未查询到操作人员信息\"}";
                    }
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String showCarSeriesList(JSONObject jsonObject1,String token) {
        String brand_id = jsonObject1.getString("brand_id");
        String car_series_name = jsonObject1.getString("car_series_name");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("获取车系列表,token：" + token + ",brand_id：" + brand_id + ",car_series_name：" + car_series_name);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(brand_id)) {
                        return "{\"code\": 201,\"msg\": \"车品牌id不允许为空\"}";
                    }
                    StringBuilder sb = new StringBuilder("");
                    List<Che_CarSeries> carSeriesList;
                    if (!StringUtils.isEmpty(car_series_name)) {
                        carSeriesList = carSeriesRepository.getSeriesListLike(Integer.parseInt(brand_id), car_series_name);
                    } else {
                        carSeriesList = carSeriesRepository.getSeriesList(Integer.parseInt(brand_id));
                    }
                    if (carSeriesList.size() > 0) {
                        for (Che_CarSeries carSeries : carSeriesList) {
                            sb.append("{" +
                                    "\"series_id\":\"" + carSeries.getSeries_id() + "\"," +
                                    "\"series_name\":\"" + carSeries.getSeries_name() + "\"," +
                                    "\"maker_type\":\"" + carSeries.getMaker_type() + "\"," +
                                    "\"brand_id\":\"" + carSeries.getBrand_id() + "\"," +
                                    "\"series_group_name\":\"" + carSeries.getSeries_group_name() + "\"},");
                        }
                        if (sb.toString().contains("name")) {
                            sb = sb.deleteCharAt(sb.length() - 1);
                        }
                    }
                    return "{\"code\": 200,\"msg\":\"success\",\"data\":[" + sb + "]}";
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String showCarList(JSONObject jsonObject1,String token) {
        String series_id = jsonObject1.getString("series_id");
        String car_model_name = jsonObject1.getString("car_model_name");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("获取车系列表,token：" + token + ",series_id" + series_id + ",car_model_name：" + car_model_name);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(series_id)) {
                        return "{\"code\": 201,\"msg\": \"车系id不允许为空\"}";
                    }
                    StringBuilder sb = new StringBuilder("");
                    List<Che_Car> carList;
                    if (!StringUtils.isEmpty(car_model_name)) {
                        carList = carRepository.getCarListLike(Integer.parseInt(series_id), car_model_name);
                    } else {
                        carList = carRepository.getCarList(Integer.parseInt(series_id));
                    }
                    if (carList.size() > 0) {
                        for (Che_Car car : carList) {
                            sb.append("{" +
                                    "\"model_id\":\"" + car.getModel_id() + "\"," +
                                    "\"model_name\":\"" + car.getModel_name() + "\"," +
                                    "\"short_name\":\"" + car.getShort_name() + "\"," +
                                    "\"model_price\":\"" + car.getModel_price() + "\"," +
                                    "\"model_year\":\"" + car.getModel_year() + "\"," +
                                    "\"min_reg_year\":\"" + car.getMin_reg_year() + "\"," +
                                    "\"liter\":\"" + car.getLiter() + "\"," +
                                    "\"gear_type\":\"" + car.getGear_type() + "\"," +
                                    "\"discharge_strandard\":\"" + car.getDischarge_standard() + "\"," +
                                    "\"seat_number\":\"" + car.getSeat_number() + "\"," +
                                    "\"series_id\":\"" + car.getSeries_id() + "\"},");
                        }
                        if (sb.toString().contains("name")) {
                            sb = sb.deleteCharAt(sb.length() - 1);
                        }
                    }
                    return "{\"code\": 200,\"msg\":\"success\",\"data\":[" + sb + "]}";
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String showStoreServiceList(JSONObject jsonObject1,String token) {
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("获取门店服务列表,token：" + token);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    Sys_Account account = sysAccountRepository.getByUserName(creator.toString());
                    if (account != null) {
                        StringBuilder sb = new StringBuilder("");
                        int store_Id = account.getStore_id();
                        Sys_Store store = sysStoreRepository.getOne(store_Id);
                        String storeServices = store.getStore_service();
                        String[] serviceArray = storeServices.split(",");
                        List<String> serviceList = new ArrayList<>(Arrays.asList(serviceArray));
                        List<Che_Service> cheServiceList = serviceRepository.getAllByServiceName(serviceList);
                        if (cheServiceList.size() > 0) {
                            for (Che_Service che_service : cheServiceList) {
                                sb.append("{" +
                                        "\"service_id\":\"" + che_service.getService_id() + "\"," +
                                        "\"service_name\":\"" + che_service.getService_name() + "\"," +
                                        "\"service_alias_name\":\"" + che_service.getService_alias_name() + "\"," +
                                        "\"service_remark\":\"" + che_service.getService_remark() + "\"," +
                                        "\"delete_state\":\"" + che_service.getDelete_state() + "\"},");
                            }
                            if (sb.toString().contains("name")) {
                                sb = sb.deleteCharAt(sb.length() - 1);
                            }
                        }
                        return "{\"code\": 200,\"msg\":\"success\",\"data\":[" + sb + "]}";
                    } else {
                        return "{\"code\": 201,\"msg\": \"未查询到操作人员信息\"}";
                    }
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }


    @Override
    public String GetExtendInsurePrice(JSONObject jsonObject1,String token) {
        String brand_id = jsonObject1.getString("brand_id");
        String series_id = jsonObject1.getString("series_id");
        String model_id = jsonObject1.getString("model_id");
        String service_id = jsonObject1.getString("service_id");
        String buy_time = jsonObject1.getString("buy_time");//登记时间
        String mileage = jsonObject1.getString("mileage");//行驶里程数
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("计算车延保价格,token：" + token + ",brand_id" + brand_id + ",series_id：" + series_id + ",model_id：" + model_id +
                        ",service_id：" + service_id + ",buy_time：" + buy_time + ",mileage：" + mileage);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(brand_id)) {
                        return "{\"code\": 201,\"msg\": \"车品牌id不允许为空\"}";
                    }
                    if (StringUtils.isEmpty(series_id)) {
                        return "{\"code\": 201,\"msg\": \"车系id不允许为空\"}";
                    }
                    if (StringUtils.isEmpty(model_id)) {
                        return "{\"code\": 201,\"msg\": \"车型号id不允许为空\"}";
                    }
                    if (StringUtils.isEmpty(service_id)) {
                        return "{\"code\": 201,\"msg\": \"车服务项目id不允许为空\"}";
                    }
                    if (StringUtils.isEmpty(buy_time)) {
                        return "{\"code\": 201,\"msg\": \"购车时间不允许为空\"}";
                    }
                    if (StringUtils.isEmpty(mileage)) {
                        return "{\"code\": 201,\"msg\": \"车辆行驶里程数不允许为空\"}";
                    }
                    Che_Car car = carRepository.findOne(Integer.valueOf(model_id));
                    if (car != null) {
                        int is_during_insurance = 0;
                        int is_new_car = 0;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        long unix_buy_time = sdf.parse(buy_time).getTime();//购车时间
                        long unix_warranty_year = car.getWarranty_year() * 31536000000L;//车型号保修时长
                        long unix_warranty_year_end = unix_buy_time + unix_warranty_year;//保修截止时间
                        long unix_now = System.currentTimeMillis();//当前时间
                        long unix_is_new_car = unix_now - 31536000000L;//1年前均未旧车
                        if (unix_now > unix_warranty_year_end) {//已过保修期
                            is_during_insurance = 1;
                        }
                        if (unix_buy_time > unix_is_new_car) {//购车时间大于1年,旧车
                            is_new_car = 1;
                        }
                        Sys_fixed_quotation fixed_quotation = fixedQuotationRepository.getPrice(Integer.parseInt(brand_id), Integer.parseInt(series_id), Integer.parseInt(service_id),
                                car.getLiter(), is_during_insurance, is_new_car);
                        if (fixed_quotation != null) {
                            return "{\"code\": 200,\"msg\":\"success\",\"data\":\"" + fixed_quotation.getAmount_money() + "\"}";
                        } else {
                            return "{\"code\": 201,\"msg\": \"未查询到相应研保价格\"}";
                        }
                    } else {
                        return "{\"code\": 201,\"msg\": \"车型号不存在\"}";
                    }
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String uploadPhoto(JSONObject jsonObject1,String token) {
        String img_code = jsonObject1.getString("img_code");
        String img_type = jsonObject1.getString("img_type");
        try {
            if (token != null && !"".equals(token)) {
                token = Base64Util.decoderBASE64(token);
                logger.info("上传订单图片,token：" + token + ",img_type" + img_type);
                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(token);
                Object creator = jsonObject.get("user_name");
                Object limit_time = jsonObject.get("limit_time");
                if (creator != null && !"".equals(creator) && limit_time != null && !"".equals(limit_time)) {
                    long currentTime = Long.parseLong(limit_time.toString());
                    if (System.currentTimeMillis() > currentTime) {
                        return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                    }
                    if (StringUtils.isEmpty(img_code)) {
                        return "{\"code\": 201,\"msg\": \"图片编码不允许为空\"}";
                    }
                    if (StringUtils.isEmpty(img_type)) {
                        return "{\"code\": 201,\"msg\": \"图片类型不允许为空\"}";
                    }
                    String FilePath = "/exchangeImg/default.jpg";
                    String img_base64 = img_code;
                    img_base64 = img_base64.trim();
                    img_base64 = img_base64.replaceAll("\n", "");
                    img_base64 = img_base64.replaceAll("\r", "");
                    img_base64 = img_base64.replaceAll("\t", "");
                    img_base64 = img_base64.replaceAll(" ", "+");

                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] b = decoder.decodeBuffer(img_base64);
                    for (int i = 0; i < b.length; ++i) {
                        if (b[i] < 0) {//调整异常数据
                            b[i] += 256;
                        }
                    }
                    String photoName = currentTime + "_" + img_type + "_" + creator;
//                    String imgFilePath = "/usr/local/tomcat/apache-tomcat-7.0.82/webapps/";//服务器
                    String imgFilePath = "E:/";//本地测试
                    FilePath = "/customerImg/" + photoName + ".jpg";//新生成的图片

                    OutputStream out = new FileOutputStream(imgFilePath + FilePath);
                    out.write(b);
                    out.flush();
                    out.close();
                    return "{\"code\": 200,\"msg\":\"success\",\"data\":\"" + FilePath + "\"}";
                } else {
                    return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
                }
            } else {
                return "{\"code\": 202,\"msg\": \"登录失效,请重新登录\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\": 201,\"msg\": \"系统异常\"}";
        }
    }

    @Override
    public String saveOrderInfo(HttpServletRequest request,String token) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
//            String reqMsg = request.getParameter("reqMsg");
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object yb_order_id = jsonObject.get("yb_order_id");
                Object yb_customer_name = jsonObject.get("yb_customer_name");
                Object yb_customer_cert_no = jsonObject.get("yb_customer_cert_no");
                Object yb_customer_address = jsonObject.get("yb_customer_address");
                Object yb_customer_tel = jsonObject.get("yb_customer_tel");
                Object yb_creator = jsonObject.get("yb_creator");
                Object yb_order_fee = jsonObject.get("yb_order_fee");
                Object yb_actual_fee = jsonObject.get("yb_actual_fee");
                Object yb_pay_type = jsonObject.get("yb_pay_type");
                Object yb_car_info = jsonObject.get("yb_car_info");
                Object yb_car_number = jsonObject.get("yb_car_number");
                Object yb_car_vin_number = jsonObject.get("yb_car_vin_number");
                Object yb_car_engine_number = jsonObject.get("yb_car_engine_number");
                Object yb_car_mileage = jsonObject.get("yb_car_mileage");
                Object yb_car_buytime = jsonObject.get("yb_car_buytime");
                Object yb_product_type = jsonObject.get("yb_product_type");
                Object yb_order_store = jsonObject.get("yb_order_store");
                Object yb_order_print_time = jsonObject.get("yb_order_print_time");
                Object yb_cert_no_front_path = jsonObject.get("yb_cert_no_front_path");
                Object yb_cert_no_back_path = jsonObject.get("yb_cert_no_back_path");
                Object yb_driving_permit_path = jsonObject.get("yb_driving_permit_path");
                Object yb_mileage_path = jsonObject.get("yb_mileage_path");
                Object yb_car_front_path = jsonObject.get("yb_car_front_path");
                Object yb_car_back_path = jsonObject.get("yb_car_back_path");

                if (yb_order_id != null && !"".equals(yb_order_id) && yb_customer_name != null && !"".equals(yb_customer_name)
                        && yb_creator != null && !"".equals(yb_creator) && yb_order_fee != null && !"".equals(yb_order_fee)
                        && yb_car_info != null && !"".equals(yb_car_info) && yb_car_mileage != null && !"".equals(yb_car_mileage)
                        && yb_product_type != null && !"".equals(yb_product_type) && yb_order_store != null && !"".equals(yb_order_store)
                ) {
                    Yb_Order_Info ybOrderInfo = new Yb_Order_Info();
                    ybOrderInfo.setYb_order_id(yb_order_id.toString());
                    ybOrderInfo.setYb_customer_name(yb_customer_name.toString());
                    ybOrderInfo.setYb_creator(yb_creator.toString());
                    ybOrderInfo.setYb_order_fee(new BigDecimal(yb_order_fee.toString()));
                    ybOrderInfo.setYb_car_info(yb_car_info.toString());
                    ybOrderInfo.setYb_car_mileage(yb_car_mileage.toString());
                    ybOrderInfo.setYb_product_type(yb_product_type.toString());
                    ybOrderInfo.setYb_order_store(yb_order_store.toString());
                    ybOrderInfo.setYb_create_time(BigInteger.valueOf(System.currentTimeMillis()));
                    ybOrderInfo.setYb_update_time(BigInteger.valueOf(System.currentTimeMillis()));
                    if (yb_customer_cert_no != null && !"".equals(yb_customer_cert_no)) {
                        ybOrderInfo.setYb_customer_cert_no(yb_customer_cert_no.toString());
                    }
                    if (yb_customer_address != null && !"".equals(yb_customer_address)) {
                        ybOrderInfo.setYb_customer_address(yb_customer_address.toString());
                    }
                    if (yb_customer_tel != null && !"".equals(yb_customer_tel)) {
                        ybOrderInfo.setYb_customer_tel(yb_customer_tel.toString());
                    }
                    if (yb_actual_fee != null && !"".equals(yb_actual_fee)) {
                        ybOrderInfo.setYb_actual_fee(new BigDecimal(yb_actual_fee.toString()));
                    }
                    if (yb_pay_type != null && !"".equals(yb_pay_type)) {
                        ybOrderInfo.setYb_pay_type(yb_pay_type.toString());
                    }
                    if (yb_car_number != null && !"".equals(yb_car_number)) {
                        ybOrderInfo.setYb_car_number(yb_car_number.toString());
                    }
                    if (yb_car_vin_number != null && !"".equals(yb_car_vin_number)) {
                        ybOrderInfo.setYb_car_vin_number(yb_car_vin_number.toString());
                    }
                    if (yb_car_engine_number != null && !"".equals(yb_car_engine_number)) {
                        ybOrderInfo.setYb_car_engine_number(yb_car_engine_number.toString());
                    }
                    if (yb_car_buytime != null && !"".equals(yb_car_buytime)) {
                        ybOrderInfo.setYb_car_buytime(yb_car_buytime.toString());
                    }
                    if (yb_order_print_time != null && !"".equals(yb_order_print_time)) {
                        ybOrderInfo.setYb_order_print_time(Integer.valueOf(yb_order_print_time.toString()));
                    }
                    if (yb_cert_no_front_path != null && !"".equals(yb_cert_no_front_path)) {
                        ybOrderInfo.setYb_cert_no_front_path(yb_cert_no_front_path.toString());
                    }
                    if (yb_cert_no_back_path != null && !"".equals(yb_cert_no_back_path)) {
                        ybOrderInfo.setYb_cert_no_back_path(yb_cert_no_back_path.toString());
                    }
                    if (yb_driving_permit_path != null && !"".equals(yb_driving_permit_path)) {
                        ybOrderInfo.setYb_driving_permit_path(yb_driving_permit_path.toString());
                    }
                    if (yb_mileage_path != null && !"".equals(yb_mileage_path)) {
                        ybOrderInfo.setYb_mileage_path(yb_mileage_path.toString());
                    }
                    if (yb_car_front_path != null && !"".equals(yb_car_front_path)) {
                        ybOrderInfo.setYb_car_front_path(yb_car_front_path.toString());
                    }
                    if (yb_car_back_path != null && !"".equals(yb_car_back_path)) {
                        ybOrderInfo.setYb_car_back_path(yb_car_back_path.toString());
                    }
                    ybOrderInfoRepository.save(ybOrderInfo);
                    saveLog(yb_creator.toString(), "创建订单：" + yb_order_id);
                    return Base64Util.encoderBASE64("{\"code\": 200,\"msg\": \"角色创建成功\"}");
                } else {
                    return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                }
            } else {
                return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "eyJjb2RlIjogMjAxLCJtc2ciOiAi57O757uf5byC5bi4In0=";
        }
    }

    @Override
    public String updateOrderInfo(HttpServletRequest request) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
//            String reqMsg = request.getParameter("reqMsg");
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object yb_order_id = jsonObject.get("yb_order_id");
                Object yb_customer_name = jsonObject.get("yb_customer_name");
                Object yb_creator = jsonObject.get("yb_creator");
                Object yb_car_number = jsonObject.get("yb_car_number");
                Object yb_car_vin_number = jsonObject.get("yb_car_vin_number");
                Object yb_order_store = jsonObject.get("yb_order_store");
                Object create_time_start = jsonObject.get("create_time_start");
                Object create_time_end = jsonObject.get("create_time_end");
                BigInteger bi_create_time_start = new BigInteger("1577808000000");
                BigInteger bi_create_time_end = new BigInteger("1924963199000");
                if (create_time_start != null && !"".equals(create_time_start)) {
                    long start_long = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(create_time_start.toString()).getTime();
                    bi_create_time_start = new BigInteger(String.valueOf(start_long));
                }
                if (create_time_end != null && !"".equals(create_time_end)) {
                    long end_long = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(create_time_end.toString()).getTime();
                    bi_create_time_end = new BigInteger(String.valueOf(end_long));
                }

                List<Yb_Order_Info> orderInfoList = ybOrderInfoRepository.getOrderList(yb_order_id.toString(), yb_customer_name.toString(), yb_creator.toString(), yb_car_number.toString(),
                        yb_car_vin_number.toString(), yb_order_store.toString(), bi_create_time_start, bi_create_time_end);
                return Base64Util.encoderBASE64("{\"code\": 200,\"msg\": \"参数异常\"}");
            } else {
                return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "eyJjb2RlIjogMjAxLCJtc2ciOiAi57O757uf5byC5bi4In0=";
        }
    }

    @Override
    public String getOrderInfoList(HttpServletRequest request) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
//            String reqMsg = request.getParameter("reqMsg");
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object yb_order_id = jsonObject.get("yb_order_id");
                Object yb_customer_name = jsonObject.get("yb_customer_name");
                Object yb_customer_cert_no = jsonObject.get("yb_customer_cert_no");
                Object yb_customer_address = jsonObject.get("yb_customer_address");
                Object yb_customer_tel = jsonObject.get("yb_customer_tel");
                Object yb_creator = jsonObject.get("yb_creator");
                Object yb_order_fee = jsonObject.get("yb_order_fee");
                Object yb_actual_fee = jsonObject.get("yb_actual_fee");
                Object yb_pay_type = jsonObject.get("yb_pay_type");
                Object yb_car_info = jsonObject.get("yb_car_info");
                Object yb_car_number = jsonObject.get("yb_car_number");
                Object yb_car_vin_number = jsonObject.get("yb_car_vin_number");
                Object yb_car_engine_number = jsonObject.get("yb_car_engine_number");
                Object yb_car_mileage = jsonObject.get("yb_car_mileage");
                Object yb_car_buytime = jsonObject.get("yb_car_buytime");
                Object yb_product_type = jsonObject.get("yb_product_type");
                Object yb_order_store = jsonObject.get("yb_order_store");
                Object yb_order_print_time = jsonObject.get("yb_order_print_time");
                Object yb_cert_no_front_path = jsonObject.get("yb_cert_no_front_path");
                Object yb_cert_no_back_path = jsonObject.get("yb_cert_no_back_path");
                Object yb_driving_permit_path = jsonObject.get("yb_driving_permit_path");
                Object yb_mileage_path = jsonObject.get("yb_mileage_path");
                Object yb_car_front_path = jsonObject.get("yb_car_front_path");
                Object yb_car_back_path = jsonObject.get("yb_car_back_path");

                if (yb_order_id != null && !"".equals(yb_order_id)) {


                    Yb_Order_Info ybOrderInfo = ybOrderInfoRepository.getByYbOrderId(yb_order_id.toString());
                    if (ybOrderInfo != null) {
                        Yb_Order_Info_Back ybOrderInfoBack = new Yb_Order_Info_Back();
                        BeanUtils.copyProperties(ybOrderInfo, ybOrderInfoBack);

                        ybOrderInfoBackRepository.save(ybOrderInfoBack);
                        if (yb_customer_name != null && !"".equals(yb_customer_name)) {
                            ybOrderInfo.setYb_customer_name(yb_customer_name.toString());
                        }
                        if (yb_order_fee != null && !"".equals(yb_order_fee)) {
                            ybOrderInfo.setYb_order_fee(new BigDecimal(yb_order_fee.toString()));
                        }
                        if (yb_car_info != null && !"".equals(yb_car_info)) {
                            ybOrderInfo.setYb_car_info(yb_car_info.toString());
                        }
                        if (yb_car_mileage != null && !"".equals(yb_car_mileage)) {
                            ybOrderInfo.setYb_car_mileage(yb_car_mileage.toString());
                        }
                        if (yb_product_type != null && !"".equals(yb_product_type)) {
                            ybOrderInfo.setYb_product_type(yb_product_type.toString());
                        }
                        if (yb_order_store != null && !"".equals(yb_order_store)) {
                            ybOrderInfo.setYb_order_store(yb_order_store.toString());
                        }
                        if (yb_customer_cert_no != null && !"".equals(yb_customer_cert_no)) {
                            ybOrderInfo.setYb_customer_cert_no(yb_customer_cert_no.toString());
                        }
                        if (yb_customer_address != null && !"".equals(yb_customer_address)) {
                            ybOrderInfo.setYb_customer_address(yb_customer_address.toString());
                        }
                        if (yb_customer_tel != null && !"".equals(yb_customer_tel)) {
                            ybOrderInfo.setYb_customer_tel(yb_customer_tel.toString());
                        }
                        if (yb_actual_fee != null && !"".equals(yb_actual_fee)) {
                            ybOrderInfo.setYb_actual_fee(new BigDecimal(yb_actual_fee.toString()));
                        }
                        if (yb_pay_type != null && !"".equals(yb_pay_type)) {
                            ybOrderInfo.setYb_pay_type(yb_pay_type.toString());
                        }
                        if (yb_car_number != null && !"".equals(yb_car_number)) {
                            ybOrderInfo.setYb_car_number(yb_car_number.toString());
                        }
                        if (yb_car_vin_number != null && !"".equals(yb_car_vin_number)) {
                            ybOrderInfo.setYb_car_vin_number(yb_car_vin_number.toString());
                        }
                        if (yb_car_engine_number != null && !"".equals(yb_car_engine_number)) {
                            ybOrderInfo.setYb_car_engine_number(yb_car_engine_number.toString());
                        }
                        if (yb_car_buytime != null && !"".equals(yb_car_buytime)) {
                            ybOrderInfo.setYb_car_buytime(yb_car_buytime.toString());
                        }
                        if (yb_order_print_time != null && !"".equals(yb_order_print_time)) {
                            ybOrderInfo.setYb_order_print_time(Integer.valueOf(yb_order_print_time.toString()));
                        }
                        if (yb_cert_no_front_path != null && !"".equals(yb_cert_no_front_path)) {
                            ybOrderInfo.setYb_cert_no_front_path(yb_cert_no_front_path.toString());
                        }
                        if (yb_cert_no_back_path != null && !"".equals(yb_cert_no_back_path)) {
                            ybOrderInfo.setYb_cert_no_back_path(yb_cert_no_back_path.toString());
                        }
                        if (yb_driving_permit_path != null && !"".equals(yb_driving_permit_path)) {
                            ybOrderInfo.setYb_driving_permit_path(yb_driving_permit_path.toString());
                        }
                        if (yb_mileage_path != null && !"".equals(yb_mileage_path)) {
                            ybOrderInfo.setYb_mileage_path(yb_mileage_path.toString());
                        }
                        if (yb_car_front_path != null && !"".equals(yb_car_front_path)) {
                            ybOrderInfo.setYb_car_front_path(yb_car_front_path.toString());
                        }
                        if (yb_car_back_path != null && !"".equals(yb_car_back_path)) {
                            ybOrderInfo.setYb_car_back_path(yb_car_back_path.toString());
                        }
                        ybOrderInfoRepository.save(ybOrderInfo);
                        saveLog(yb_creator.toString(), "修改订单：" + yb_order_id);
                        return Base64Util.encoderBASE64("{\"code\": 200,\"msg\": \"角色创建成功\"}");
                    } else {
                        return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"订单不存在\"}");
                    }

                } else {
                    return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                }
            } else {
                return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "eyJjb2RlIjogMjAxLCJtc2ciOiAi57O757uf5byC5bi4In0=";
        }
    }


    public void saveLog(String user_name, String operation_log) {

        Sys_Operation_Log sysOperationLog = new Sys_Operation_Log();
        sysOperationLog.setUser_name(user_name);
        sysOperationLog.setOperation_time(BigInteger.valueOf(System.currentTimeMillis()));
        sysOperationLog.setOperation_info(operation_log);
        sysOperationLogRepository.save(sysOperationLog);
    }

}
