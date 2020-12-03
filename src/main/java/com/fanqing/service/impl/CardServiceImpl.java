package com.fanqing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fanqing.bean.*;
import com.fanqing.dao.*;
import com.fanqing.service.CardService;
import com.fanqing.util.Base64Util;
import com.fanqing.util.HttpUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private ProjectServiceRepository projectServiceRepository;
    @Autowired
    private InsureServiceRepository insureServiceRepository;
    @Autowired
    private ProductServiceRepository productServiceRepository;
    @Autowired
    private YbOrderInfoRepository ybOrderInfoRepository;
    @Autowired
    private SysOperationLogRepository sysOperationLogRepository;
    @Autowired
    private YbOrderInfoBackRepository ybOrderInfoBackRepository;

    private static final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    @Override
    public String ShowCheCity(HttpServletRequest request) {

        try {
            String content = HttpUtil.getPostByTextPlain(request);
//            String reqMsg = request.getParameter("reqMsg");
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object type = jsonObject.get("type");
                Object id = jsonObject.get("id");
                if (type != null && !"".equals(type) && id != null && !"".equals(id)) {
                    if ("1".equals(type.toString())) {//type=1，查询省
                        List<Che_City> provinceList = cityRepository.getProvinceList();
                        StringBuilder sb = new StringBuilder("{\"code\":\"200\",\"msg\":[");
                        for (Che_City province : provinceList) {
                            sb.append("{\"Id\":\"" + province.getProv_id() + "\",\"Name\":\"" + province.getProv_name() + "\"},");
                        }
                        if (sb.toString().contains("Id")) {
                            sb = sb.deleteCharAt(sb.length() - 1);
                        }
                        sb.append("]}");
                        return Base64Util.encoderBASE64(sb.toString());

                    } else if ("2".equals(type.toString())) {
                        int intId = Integer.parseInt(id.toString());
                        List<Che_City> cityList = cityRepository.getCityList(intId);
                        StringBuilder sb = new StringBuilder("{\"code\":\"200\",\"msg\":[");
                        for (Che_City city : cityList) {
                            sb.append("{\"Id\":\"" + city.getCity_id() + "\",\"Name\":\"" + city.getCity_name() + "\"},");
                        }
                        if (sb.toString().contains("Id")) {
                            sb = sb.deleteCharAt(sb.length() - 1);
                        }
                        sb.append("]}");
                        return Base64Util.encoderBASE64(sb.toString());
                    } else {
                        return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
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

    @Override
    public String ShowCarSeriesList(HttpServletRequest request) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
//            String reqMsg = request.getParameter("reqMsg");
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            System.out.println(reqMsg);
            //

            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);
                System.out.println(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object id = jsonObject.get("id");
                Object Search = jsonObject.get("Search");
                if (id != null && !"".equals(id)) {

                    if (Search != null && !"".equals(Search)) {//查询
                        if ("0".equals(id.toString())) {
                            List<Che_CarBrand> carBrandList;
                            if ("输入关键字或品牌名称拼音首字母".equals(Search.toString())) {//全查
                                carBrandList = cardBrandRepository.findAll();
                            } else {
                                carBrandList = cardBrandRepository.getCarBrandListLike(Search.toString());
                            }
                            StringBuilder sb = new StringBuilder("{\"code\":\"200\",\"msg\":[");
                            for (Che_CarBrand carBrand : carBrandList) {
                                sb.append("{\"brand_id\":\"" + carBrand.getBrand_id() + "\",\"brand_name\":\"" + carBrand.getBrand_name() + "\",\"initial\":\"" + carBrand.getInitial() + "\"},");
                            }
                            if (sb.toString().contains("brand_id")) {
                                sb = sb.deleteCharAt(sb.length() - 1);
                            }
                            sb.append("]}");
                            return Base64Util.encoderBASE64(sb.toString());
                        } else {
                            int intId = Integer.parseInt(id.toString());
                            List<Che_CarSeries> carSeriesList;
                            if ("输入车系关键字".equals(Search.toString())) {
                                carSeriesList = carSeriesRepository.getSeriesList(intId);
                            } else {
                                carSeriesList = carSeriesRepository.getSeriesListLike(intId, Search.toString());
                            }
                            StringBuilder sb = new StringBuilder("{\"code\":\"200\",\"msg\":[");
                            for (Che_CarSeries carSeries : carSeriesList) {
                                sb.append("{\"series_id\":\"" + carSeries.getSeries_id() + "\",\"series_name\":\"" + carSeries.getSeries_name() + "\"},");
                            }
                            if (sb.toString().contains("series_id")) {
                                sb = sb.deleteCharAt(sb.length() - 1);
                            }
                            sb.append("]}");
                            return Base64Util.encoderBASE64(sb.toString());
                        }
                    } else {
                        if ("0".equals(id.toString())) {//id=0，查全部车品牌
                            List<Che_CarBrand> carBrandList = cardBrandRepository.findAll();
                            StringBuilder sb = new StringBuilder("{\"code\":\"200\",\"msg\":[");
                            for (Che_CarBrand carBrand : carBrandList) {
                                sb.append("{\"brand_id\":\"" + carBrand.getBrand_id() + "\",\"brand_name\":\"" + carBrand.getBrand_name() + "\",\"initial\":\"" + carBrand.getInitial() + "\"},");
                            }
                            if (sb.toString().contains("brand_id")) {
                                sb = sb.deleteCharAt(sb.length() - 1);
                            }
                            sb.append("]}");
                            return Base64Util.encoderBASE64(sb.toString());

                        } else {//id 不=0，根据id查车列表
                            int intId = Integer.parseInt(id.toString());
                            List<Che_CarSeries> carSeriesList = carSeriesRepository.getSeriesList(intId);
                            StringBuilder sb = new StringBuilder("{\"code\":\"200\",\"msg\":[");
                            for (Che_CarSeries carSeries : carSeriesList) {
                                sb.append("{\"series_id\":\"" + carSeries.getSeries_id() + "\",\"series_name\":\"" + carSeries.getSeries_name() + "\"},");
                            }
                            if (sb.toString().contains("series_id")) {
                                sb = sb.deleteCharAt(sb.length() - 1);
                            }
                            sb.append("]}");
                            return Base64Util.encoderBASE64(sb.toString());
                        }
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

    @Override
    public String ShowCarList(HttpServletRequest request) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
//            String reqMsg = request.getParameter("reqMsg");
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object id = jsonObject.get("id");
                Object Search = jsonObject.get("Search");
                if (id != null && !"".equals(id)) {

                    if (Search != null && !"".equals(Search)) {//查询

                        int intId = Integer.parseInt(id.toString());
                        List<Che_Car> carList;
                        if ("输入车型关键字".equals(Search.toString())) {
                            carList = carRepository.getCarList(intId);
                        } else {
                            carList = carRepository.getCarListLike(intId, Search.toString());
                        }
                        StringBuilder sb = new StringBuilder("{\"code\":\"200\",\"msg\":[");
                        for (Che_Car car : carList) {
                            sb.append("{\"model_id\":\"" + car.getModel_id() + "\",\"model_name\":\"" + car.getModel_name() + "\"},");
                        }
                        if (sb.toString().contains("model_id")) {
                            sb = sb.deleteCharAt(sb.length() - 1);
                        }
                        sb.append("]}");
                        return Base64Util.encoderBASE64(sb.toString());

                    } else {

                        if ("-1".equals(id.toString())) {
                            return Base64Util.encoderBASE64("{\"code\":\"200\",\"msg\":[]}");
                        }

                        int intId = Integer.parseInt(id.toString());
                        List<Che_Car> carList = carRepository.getCarList(intId);
                        StringBuilder sb = new StringBuilder("{\"code\":\"200\",\"msg\":[");
                        for (Che_Car car : carList) {
                            sb.append("{\"model_id\":\"" + car.getModel_id() + "\",\"model_name\":\"" + car.getModel_name() + "\",\"model_year\":\"" + car.getModel_year() + "\",\"min_reg_year\":\"" + car.getMin_reg_year() + "\",\"max_reg_year\":\"" + car.getMax_reg_year() + "\",\"model_price\":\"" + car.getModel_price() + "\"},");
                        }
                        if (sb.toString().contains("model_id")) {
                            sb = sb.deleteCharAt(sb.length() - 1);
                        }
                        sb.append("]}");
                        return Base64Util.encoderBASE64(sb.toString());

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

    @Override
    public String GetUsedCarPrice(HttpServletRequest request) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object modelId = jsonObject.get("modelId");
                Object city = jsonObject.get("city");
                Object regDate = jsonObject.get("regDate");
                Object mile = jsonObject.get("mile");
                Object price = jsonObject.get("price");
                if (modelId != null && !"".equals(modelId) && city != null && !"".equals(city) && regDate != null && !"".equals(regDate) && mile != null && !"".equals(mile) && price != null && !"".equals(price)) {

                    String token = "123";
                    if (token != null && !"".equals(token)) {

                        int intMile = Integer.parseInt(mile.toString());
                        String normalCard = HttpUtil.sendPost("", "token=" + token + "&modelId=" + modelId.toString() + "&regDate=" + regDate.toString() + "&mile=" + intMile * 1.0 / 1000 + "&zone=" + city.toString());

                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, -1);
                        Date time = cal.getTime();
                        String currentRegDate = new SimpleDateFormat("yyyy-MM-dd").format(time);
                        String newCard = HttpUtil.sendPost("", "token=" + token + "&modelId=" + modelId.toString() + "&regDate=" + currentRegDate + "&mile=0&zone=" + city.toString());

                        if (newCard != null && !"".equals(newCard)) {

                        }
                        if (normalCard != null && !"".equals(normalCard)) {

                        }
                        return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"token异常\"}");
                    } else {
                        return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"token异常\"}");
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

    @Override
    public String GetProjectHtml(HttpServletRequest request) {
        try {
            List<Object[]> projectservices = projectServiceRepository.findProjectInfo();
            if (projectservices.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Object[] projectservice : projectservices) {
                    if ("DEFS".equals(projectservice[2])) {
                        sb.append("<option value=\"" + projectservice[0] + "\" selected=\"selected\">" + projectservice[1] + "</option>");
                    } else {
                        sb.append("<option value=\"" + projectservice[0] + "\" >" + projectservice[1] + "</option>");
                    }
                }
                return sb.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String GetInsurePlanHtml(HttpServletRequest request) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object price = jsonObject.get("price");//车价
                Object regDate = jsonObject.get("regDate");//登记日期
                Object pid = jsonObject.get("pid");//项目编号
                Object keepYears = jsonObject.get("keepYears");//原厂质保年限
                Object firstprice = jsonObject.get("firstprice");//第一年车价
                if (price != null && !"".equals(price) && pid != null && !"".equals(pid) && keepYears != null && !"".equals(keepYears)) {
                    int intPid = Integer.parseInt(pid.toString());
                    intPid = intPid == 0 ? 2 : intPid;
                    int total = 0;
                    Ins_projectservice project = projectServiceRepository.getProjectById(intPid);
                    if (project != null) {
                        List<Ins_insureservice> insInsureserviceList = insureServiceRepository.findAll();
                        if (insInsureserviceList.size() > 0) {

                            Ins_insureservice insureModel = null;

                            BigDecimal carPrice = new BigDecimal(price.toString());
                            BigDecimal firstCarPrice = new BigDecimal(firstprice.toString());

                            for (Ins_insureservice item : insInsureserviceList) {
                                if ("YB0101".equals(item.getIs_carpricenexus())) {//最低车价 < 保额车价(P) <= 最高车价
                                    if (carPrice.compareTo(new BigDecimal(item.getIs_mincarprice())) == 1 && carPrice.compareTo(new BigDecimal(item.getIs_maxcarprice())) < 1) {
                                        insureModel = item;
                                        break;
                                    }
                                } else if ("YB0102".equals(item.getIs_carpricenexus())) {//最低车价 < 保额车价(P) < 最高车价
                                    if (carPrice.compareTo(new BigDecimal(item.getIs_mincarprice())) == 1 && carPrice.compareTo(new BigDecimal(item.getIs_maxcarprice())) == -1) {
                                        insureModel = item;
                                        break;
                                    }
                                } else if ("YB0103".equals(item.getIs_carpricenexus())) {//保额车价(P) >= 最低车价
                                    if (carPrice.compareTo(new BigDecimal(item.getIs_mincarprice())) > -1) {
                                        insureModel = item;
                                        break;
                                    }
                                }
                            }

                            List<Integer> listOriPrice = new ArrayList<Integer>();   //原始保额
                            List<Integer> listPrePrice = new ArrayList<Integer>();   //实际保额
                            List<Integer> firstlistPrePrice = new ArrayList<Integer>();   //计算保费的实际保额

                            if (insureModel != null) {
                                //1、低档保额
                                Integer _lowPrice = new BigDecimal(insureModel.getIs_lowpremium() * 1.0 / 100).multiply(carPrice).setScale(0, BigDecimal.ROUND_CEILING).multiply(new BigDecimal("10000")).intValue();
                                listOriPrice.add(_lowPrice);
                                listPrePrice.add((new BigDecimal(_lowPrice).multiply(project.getPs_coefficient())).intValue());

                                int _firstlowPrice = new BigDecimal(insureModel.getIs_lowpremium() * 1.0 / 100).multiply(firstCarPrice).setScale(0, BigDecimal.ROUND_CEILING).multiply(new BigDecimal("10000")).intValue();
                                firstlistPrePrice.add(_firstlowPrice);
                                //2、中档保额
                                int _medianPrice = new BigDecimal(insureModel.getIs_medianpremium() * 1.0 / 100).multiply(carPrice).setScale(0, BigDecimal.ROUND_CEILING).multiply(new BigDecimal("10000")).intValue();
                                listOriPrice.add(_medianPrice);
                                listPrePrice.add((new BigDecimal(_medianPrice).multiply(project.getPs_coefficient())).intValue());

                                int _firstmedianPrice = new BigDecimal(insureModel.getIs_medianpremium() * 1.0 / 100).multiply(firstCarPrice).setScale(0, BigDecimal.ROUND_CEILING).multiply(new BigDecimal("10000")).intValue();
                                firstlistPrePrice.add(_firstmedianPrice);
                                //3、高档保额
                                int _highPrice = new BigDecimal(insureModel.getIs_highpremium() * 1.0 / 100).multiply(carPrice).setScale(0, BigDecimal.ROUND_CEILING).multiply(new BigDecimal("10000")).intValue();
                                listOriPrice.add(_highPrice);
                                listPrePrice.add((new BigDecimal(_highPrice).multiply(project.getPs_coefficient())).intValue());

                                int _firsthighPrice = new BigDecimal(insureModel.getIs_highpremium() * 1.0 / 100).multiply(firstCarPrice).setScale(0, BigDecimal.ROUND_CEILING).multiply(new BigDecimal("10000")).intValue();
                                firstlistPrePrice.add(_firsthighPrice);
                                if (listOriPrice.size() > 0 && listPrePrice.size() > 0) {

                                    //获取产品服务
                                    List<Ins_productservice> listProduct = productServiceRepository.findAll();
                                    if (listProduct.size() > 0) {
                                        Ins_productservice productModel = null;
                                        List<BigDecimal> listRebate = new ArrayList<BigDecimal>();

                                        for (Ins_productservice item : listProduct) {
                                            if ("YB0101".equals(item.getPs_carpricenexus()))    //最低车价 < 保额车价(P) <= 最高车价
                                            {
                                                if (carPrice.compareTo(new BigDecimal(item.getPs_mincarprice())) == 1 && carPrice.compareTo(new BigDecimal(item.getPs_maxcarprice())) < 1) {
                                                    productModel = item;
                                                    break;
                                                }
                                            } else if ("YB0102".equals(item.getPs_carpricenexus()))   //最低车价 < 保额车价(P) < 最高车价
                                            {
                                                if (carPrice.compareTo(new BigDecimal(item.getPs_mincarprice())) == 1 && carPrice.compareTo(new BigDecimal(item.getPs_maxcarprice())) == -1) {
                                                    productModel = item;
                                                    break;
                                                }
                                            } else if ("YB0103".equals(item.getPs_carpricenexus()))   //保额车价(P) >= 最低车价
                                            {
                                                if (carPrice.compareTo(new BigDecimal(item.getPs_mincarprice())) > -1) {
                                                    productModel = item;
                                                    break;
                                                }
                                            }
                                        }
                                        if (productModel != null) {
                                            listRebate.add(productModel.getPs_firstamount());
                                            listRebate.add(productModel.getPs_secondamount());
                                            listRebate.add(productModel.getPs_thirdamount());
                                            listRebate.add(productModel.getPs_fourthamount());
                                            listRebate.add(productModel.getPs_fifthamount());
                                        }

                                        if (listRebate.size() > 0) {
                                            if (keepYears == -1) {
                                                keepYears = 3;
                                            }
                                            if (Integer.parseInt(keepYears.toString()) >= 0) {

                                                String _regDate = regDate.toString();
                                                Calendar c = Calendar.getInstance();
                                                Date date = null;
                                                date = new SimpleDateFormat("yy-MM-dd").parse(_regDate);
                                                c.setTime(date);
                                                int daynew = c.get(Calendar.DATE);
                                                int yearnew = c.get(Calendar.YEAR);
                                                c.set(Calendar.DATE, daynew - 1);
                                                c.set(Calendar.YEAR, yearnew + Integer.parseInt(keepYears.toString()));
                                                String _insureDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
                                                Date d = new SimpleDateFormat("yyyy-MM-dd").parse(_insureDate);
                                                String _extendInsureDate;
                                                String _extendInsureDate1;
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                String html = "";
                                                //原厂保修期内
                                                if (d.compareTo(new Date()) == 1)    //原厂质保年限内
                                                {

                                                    switch (Integer.parseInt(keepYears.toString())) {
                                                        case 1:
                                                        case 2:
                                                        case 3: {

                                                            Calendar c1 = Calendar.getInstance();
                                                            Date date1 = null;
                                                            date1 = new SimpleDateFormat("yy-MM-dd").parse(_insureDate);
                                                            c1.setTime(date1);
                                                            int year1 = c1.get(Calendar.YEAR);
                                                            c1.set(Calendar.YEAR, year1 + 1);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td class='td-y-hov'><a id='a-y-1' data-year='1' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(0) + "' onclick='ChooseExtendYear(1)'>1年</a><span class='span-3'>至" + _extendInsureDate1 + "<span></td>";
                                                            html += "<td class='td-p-hov'><a id='a-p-1' data-price='" + listOriPrice.get(0) + "' data-price1='" + listPrePrice.get(0) + "' data-price2='" + firstlistPrePrice.get(0) + "' onclick='ChooseInsure(1)'>" + listPrePrice.get(0) + "</a></td>";
                                                            html += "</tr>";

                                                            c1.set(Calendar.YEAR, year1 + 2);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td><a id='a-y-2' data-year='2' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(1) + "' onclick='ChooseExtendYear(2)'>2年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                            html += "<td><a id='a-p-2' data-price='" + listOriPrice.get(1) + "' data-price1='" + listPrePrice.get(1) + "' data-price2='" + firstlistPrePrice.get(1) + "' onclick='ChooseInsure(2)'>" + listPrePrice.get(1) + "</a></td>";
                                                            html += "</tr>";

                                                            c1.set(Calendar.YEAR, year1 + 3);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td><a id='a-y-3' data-year='3' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(2) + "' onclick='ChooseExtendYear(3)'>3年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                            html += "<td><a id='a-p-3' data-price='" + listOriPrice.get(2) + "' data-price1='" + listPrePrice.get(2) + "' data-price2='" + firstlistPrePrice.get(2) + "' onclick='ChooseInsure(3)'>" + listPrePrice.get(2) + "</a></td>";
                                                            html += "</tr>";

                                                            c1.set(Calendar.YEAR, year1 + 4);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td><a id='a-y-4' data-year='4' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(3) + "' onclick='ChooseExtendYear(4)'>4年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                            html += "<td rowspan='2'><span id='s-insureType' style='display:none;'>" + insureModel.getIs_id() + "</span><span id='s-productType' style='display:none;'>" + productModel.getPs_id() + "</span><span id='s-firmDate' style='display:none;'>" + _insureDate + "</span></td>";
                                                            html += "</tr>";

                                                            c1.set(Calendar.YEAR, year1 + 5);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td><a id='a-y-5' data-year='5' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(4) + "' onclick='ChooseExtendYear(5)'>5年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                            html += "</tr>";


                                                        }
                                                        break;
                                                        case 4: {


                                                            Calendar c1 = Calendar.getInstance();
                                                            Date date1 = null;
                                                            date1 = new SimpleDateFormat("yy-MM-dd").parse(_insureDate);
                                                            c1.setTime(date1);
                                                            int year1 = c1.get(Calendar.YEAR);
                                                            c1.set(Calendar.YEAR, year1 + 1);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td class='td-y-hov'><a id='a-y-1' data-year='1' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(0) + "' onclick='ChooseExtendYear(1)'>1年</a><span class='span-3'>至" + _extendInsureDate1 + "<span></td>";
                                                            html += "<td class='td-p-hov'><a id='a-p-1' data-price='" + listOriPrice.get(0) + "' data-price1='" + listPrePrice.get(0) + "' data-price2='" + firstlistPrePrice.get(0) + "' onclick='ChooseInsure(1)'>" + listPrePrice.get(0) + "</a></td>";
                                                            html += "</tr>";

                                                            c1.set(Calendar.YEAR, year1 + 2);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td><a id='a-y-2' data-year='2' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(1) + "' onclick='ChooseExtendYear(2)'>2年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                            html += "<td><a id='a-p-2' data-price='" + listOriPrice.get(1) + "' data-price1='" + listPrePrice.get(1) + "' data-price2='" + firstlistPrePrice.get(1) + "' onclick='ChooseInsure(2)'>" + listPrePrice.get(1) + "</a></td>";
                                                            html += "</tr>";

                                                            c1.set(Calendar.YEAR, year1 + 3);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td><a id='a-y-3' data-year='3' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(2) + "' onclick='ChooseExtendYear(3)'>3年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                            html += "<td><a id='a-p-3' data-price='" + listOriPrice.get(2) + "' data-price1='" + listPrePrice.get(2) + "' data-price2='" + firstlistPrePrice.get(2) + "' onclick='ChooseInsure(3)'>" + listPrePrice.get(2) + "</a></td>";
                                                            html += "</tr>";

                                                            c1.set(Calendar.YEAR, year1 + 4);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td><a id='a-y-4' data-year='4' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(3) + "' onclick='ChooseExtendYear(4)'>4年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                            html += "<td><span id='s-insureType' style='display:none;'>" + insureModel.getIs_id() + "</span><span id='s-productType' style='display:none;'>" + productModel.getPs_id() + "</span><span id='s-firmDate' style='display:none;'>" + _insureDate + "</span></td>";
                                                            html += "</tr>";


                                                        }
                                                        break;
                                                        case 5: {


                                                            Calendar c1 = Calendar.getInstance();
                                                            Date date1 = null;
                                                            date1 = new SimpleDateFormat("yy-MM-dd").parse(_insureDate);
                                                            c1.setTime(date1);
                                                            int year1 = c1.get(Calendar.YEAR);
                                                            c1.set(Calendar.YEAR, year1 + 1);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td class='td-y-hov'><a id='a-y-1' data-year='1' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(0) + "' onclick='ChooseExtendYear(1)'>1年</a><span class='span-3'>至" + _extendInsureDate1 + "<span></td>";
                                                            html += "<td class='td-p-hov'><a id='a-p-1' data-price='" + listOriPrice.get(0) + "' data-price1='" + listPrePrice.get(0) + "' data-price2='" + firstlistPrePrice.get(0) + "' onclick='ChooseInsure(1)'>" + listPrePrice.get(0) + "</a></td>";
                                                            html += "</tr>";

                                                            c1.set(Calendar.YEAR, year1 + 2);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td><a id='a-y-2' data-year='2' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(1) + "' onclick='ChooseExtendYear(2)'>2年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                            html += "<td><a id='a-p-2' data-price='" + listOriPrice.get(1) + "' data-price1='" + listPrePrice.get(1) + "' data-price2='" + firstlistPrePrice.get(1) + "' onclick='ChooseInsure(2)'>" + listPrePrice.get(1) + "</a></td>";
                                                            html += "</tr>";

                                                            c1.set(Calendar.YEAR, year1 + 3);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td><a id='a-y-3' data-year='3' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(2) + "' onclick='ChooseExtendYear(3)'>3年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                            html += "<td><a id='a-p-3' data-price='" + listOriPrice.get(2) + "' data-price1='" + listPrePrice.get(2) + "' data-price2='" + firstlistPrePrice.get(2) + "' onclick='ChooseInsure(3)'>" + listPrePrice.get(2) + "</a></td>";
                                                            html += "</tr>";
                                                            html += "<tr style='display:none;'><td><span id='s-insureType' style='display:none;'>" + insureModel.getIs_id() + "</span><span id='s-productType' style='display:none;'>" + productModel.getPs_id() + "</span><span id='s-firmDate' style='display:none;'>" + _insureDate + "</span></td></tr>";

                                                        }
                                                        break;
                                                        case 6: {

                                                            Calendar c1 = Calendar.getInstance();
                                                            Date date1 = null;
                                                            date1 = new SimpleDateFormat("yy-MM-dd").parse(_insureDate);
                                                            c1.setTime(date1);
                                                            int year1 = c1.get(Calendar.YEAR);
                                                            c1.set(Calendar.YEAR, year1 + 1);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td class='td-y-hov'><a id='a-y-1' data-year='1' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(0) + "' onclick='ChooseExtendYear(1)'>1年</a><span class='span-3'>至" + _extendInsureDate1 + "<span></td>";
                                                            html += "<td class='td-p-hov'><a id='a-p-1' data-price='" + listOriPrice.get(0) + "' data-price1='" + listPrePrice.get(0) + "' data-price2='" + firstlistPrePrice.get(0) + "' onclick='ChooseInsure(1)'>" + listPrePrice.get(0) + "</a></td>";
                                                            html += "</tr>";

                                                            c1.set(Calendar.YEAR, year1 + 2);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td><a id='a-y-2' data-year='2' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(1) + "' onclick='ChooseExtendYear(2)'>2年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                            html += "<td><a id='a-p-2' data-price='" + listOriPrice.get(1) + "' data-price1='" + listPrePrice.get(1) + "' data-price2='" + firstlistPrePrice.get(1) + "' onclick='ChooseInsure(2)'>" + listPrePrice.get(1) + "</a></td>";
                                                            html += "</tr>";

                                                            html += "<tr>";
                                                            html += "<td rowspan='2'><span id='s-insureType' style='display:none;'>" + insureModel.getIs_id() + "</span><span id='s-productType' style='display:none;'>" + productModel.getPs_id() + "</span><span id='s-firmDate' style='display:none;'>" + _insureDate + "</span></td>";
                                                            html += "<td><a id='a-p-3' data-price='" + listOriPrice.get(2) + "' data-price1='" + listPrePrice.get(2) + "' data-price2='" + firstlistPrePrice.get(2) + "' onclick='ChooseInsure(3)'>" + listPrePrice.get(2) + "</a></td>";
                                                            html += "</tr>";


                                                        }
                                                        break;
                                                        case 7: {


                                                            Calendar c1 = Calendar.getInstance();
                                                            Date date1 = null;
                                                            date1 = new SimpleDateFormat("yy-MM-dd").parse(_insureDate);
                                                            c1.setTime(date1);
                                                            int year1 = c1.get(Calendar.YEAR);
                                                            c1.set(Calendar.YEAR, year1 + 1);
                                                            _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                            _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                            html += "<tr>";
                                                            html += "<td class='td-y-hov'><a id='a-y-1' data-year='1' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(0) + "' onclick='ChooseExtendYear(1)'>1年</a><span class='span-3'>至" + _extendInsureDate1 + "<span></td>";
                                                            html += "<td class='td-p-hov'><a id='a-p-1' data-price='" + listOriPrice.get(0) + "' data-price1='" + listPrePrice.get(0) + "' data-price2='" + firstlistPrePrice.get(0) + "' onclick='ChooseInsure(1)'>" + listPrePrice.get(0) + "</a></td>";
                                                            html += "</tr>";

                                                            html += "<tr>";
                                                            html += "<td rowspan='2'><span id='s-insureType' style='display:none;'>" + insureModel.getIs_id() + "</span><span id='s-productType' style='display:none;'>" + productModel.getPs_id() + "</span><span id='s-firmDate' style='display:none;'>" + _insureDate + "</span></td>";
                                                            html += "<td><a id='a-p-2' data-price='" + listOriPrice.get(1) + "' data-price1='" + listPrePrice.get(1) + "' data-price2='" + firstlistPrePrice.get(1) + "' onclick='ChooseInsure(2)'>" + listPrePrice.get(1) + "</a></td>";
                                                            html += "</tr>";

                                                            html += "<tr>";
                                                            html += "<td><a id='a-p-3' data-price='" + listOriPrice.get(2) + "' data-price1='" + listPrePrice.get(2) + "' onclick='ChooseInsure(3)'>" + listPrePrice.get(2) + "</a></td>";
                                                            html += "</tr>";
                                                        }
                                                        break;
                                                    }
                                                } else if (d.compareTo(new Date()) == -1)     //已过原厂质保年限
                                                {


                                                    long ts1 = System.currentTimeMillis();
                                                    long ts2 = sdf.parse(_insureDate).getTime();
                                                    long differentDays = (ts1 - ts2) / 86400000;

                                                    if (differentDays >= 0) {

                                                        Integer _years = new BigDecimal(differentDays * 1.0 / 365).setScale(0, BigDecimal.ROUND_UP).intValue();
                                                        int _reYears = 8 - Integer.parseInt(keepYears.toString()) - _years;
                                                        switch (_reYears) {
                                                            case 1: {
                                                                Calendar c1 = Calendar.getInstance();
                                                                Date date1 = new Date();
                                                                c1.setTime(date1);
                                                                int year1 = c1.get(Calendar.YEAR);
                                                                int day1 = c1.get(Calendar.DATE);
                                                                c1.set(Calendar.YEAR, year1 + 1);
                                                                c1.set(Calendar.DATE, day1 - 1);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());


                                                                html += "<tr>";
                                                                html += "<td class='td-y-hov'><a id='a-y-1' data-year='1' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(0) + "' onclick='ChooseExtendYear(1)'>1年</a><span class='span-3'>至" + _extendInsureDate1 + "<span></td>";
                                                                html += "<td class='td-p-hov'><a id='a-p-1' data-price='" + listOriPrice.get(0) + "' data-price1='" + listPrePrice.get(0) + "' data-price2='" + firstlistPrePrice.get(0) + "' onclick='ChooseInsure(1)'>" + listPrePrice.get(0) + "</a></td>";
                                                                html += "</tr>";

                                                                html += "<tr>";
                                                                html += "<td rowspan='2'><span id='s-insureType' style='display:none;'>" + insureModel.getIs_id() + "</span><span id='s-productType' style='display:none;'>" + productModel.getPs_id() + "</span><span id='s-firmDate' style='display:none;'>" + _insureDate + "</span></td>";
                                                                html += "<td><a id='a-p-2' data-price='" + listOriPrice.get(1) + "' data-price1='" + listPrePrice.get(1) + "' data-price2='" + firstlistPrePrice.get(1) + "' onclick='ChooseInsure(2)'>" + listPrePrice.get(1) + "</a></td>";
                                                                html += "</tr>";

                                                                html += "<tr>";
                                                                html += "<td><a id='a-p-3' data-price='" + listOriPrice.get(2) + "' data-price1='" + listPrePrice.get(2) + "' onclick='ChooseInsure(3)'>" + listPrePrice.get(2) + "</a></td>";
                                                                html += "</tr>";


                                                            }
                                                            break;
                                                            case 2: {
                                                                Calendar c1 = Calendar.getInstance();
                                                                Date date1 = new Date();
                                                                c1.setTime(date1);
                                                                int year1 = c1.get(Calendar.YEAR);
                                                                int day1 = c1.get(Calendar.DATE);
                                                                c1.set(Calendar.DATE, day1 - 1);
                                                                c1.set(Calendar.YEAR, year1 + 1);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td class='td-y-hov'><a id='a-y-1' data-year='1' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(0) + "' onclick='ChooseExtendYear(1)'>1年</a><span class='span-3'>至" + _extendInsureDate1 + "<span></td>";
                                                                html += "<td class='td-p-hov'><a id='a-p-1' data-price='" + listOriPrice.get(0) + "' data-price1='" + listPrePrice.get(0) + "' data-price2='" + firstlistPrePrice.get(0) + "' onclick='ChooseInsure(1)'>" + listPrePrice.get(0) + "</a></td>";
                                                                html += "</tr>";

                                                                c1.set(Calendar.YEAR, year1 + 2);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td><a id='a-y-2' data-year='2' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(1) + "' onclick='ChooseExtendYear(2)'>2年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                                html += "<td><a id='a-p-2' data-price='" + listOriPrice.get(1) + "' data-price1='" + listPrePrice.get(1) + "' data-price2='" + firstlistPrePrice.get(1) + "' onclick='ChooseInsure(2)'>" + listPrePrice.get(1) + "</a></td>";
                                                                html += "</tr>";

                                                                html += "<tr>";
                                                                html += "<td rowspan='2'><span id='s-insureType' style='display:none;'>" + insureModel.getIs_id() + "</span><span id='s-productType' style='display:none;'>" + productModel.getPs_id() + "</span><span id='s-firmDate' style='display:none;'>" + _insureDate + "</span></td>";
                                                                html += "<td><a id='a-p-3' data-price='" + listOriPrice.get(2) + "' data-price1='" + listPrePrice.get(2) + "' data-price2='" + firstlistPrePrice.get(2) + "' onclick='ChooseInsure(3)'>" + listPrePrice.get(2) + "</a></td>";
                                                                html += "</tr>";


                                                            }
                                                            break;
                                                            case 3: {
                                                                Calendar c1 = Calendar.getInstance();
                                                                Date date1 = new Date();
                                                                c1.setTime(date1);
                                                                int year1 = c1.get(Calendar.YEAR);
                                                                int day1 = c1.get(Calendar.DATE);
                                                                c1.set(Calendar.DATE, day1 - 1);
                                                                c1.set(Calendar.YEAR, year1 + 1);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td class='td-y-hov'><a id='a-y-1' data-year='1' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(0) + "' onclick='ChooseExtendYear(1)'>1年</a><span class='span-3'>至" + _extendInsureDate1 + "<span></td>";
                                                                html += "<td class='td-p-hov'><a id='a-p-1' data-price='" + listOriPrice.get(0) + "' data-price1='" + listPrePrice.get(0) + "' data-price2='" + firstlistPrePrice.get(0) + "' onclick='ChooseInsure(1)'>" + listPrePrice.get(0) + "</a></td>";
                                                                html += "</tr>";

                                                                c1.set(Calendar.YEAR, year1 + 2);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td><a id='a-y-2' data-year='2' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(1) + "' onclick='ChooseExtendYear(2)'>2年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                                html += "<td><a id='a-p-2' data-price='" + listOriPrice.get(1) + "' data-price1='" + listPrePrice.get(1) + "' data-price2='" + firstlistPrePrice.get(1) + "'onclick='ChooseInsure(2)'>" + listPrePrice.get(1) + "</a></td>";
                                                                html += "</tr>";

                                                                c1.set(Calendar.YEAR, year1 + 3);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td><a id='a-y-3' data-year='3' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(2) + "' onclick='ChooseExtendYear(3)'>3年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                                html += "<td><a id='a-p-3' data-price='" + listOriPrice.get(2) + "' data-price1='" + listPrePrice.get(2) + "' data-price2='" + firstlistPrePrice.get(2) + "' onclick='ChooseInsure(3)'>" + listPrePrice.get(2) + "</a></td>";
                                                                html += "</tr>";
                                                                html += "<tr style='display:none;'><td><span id='s-insureType' style='display:none;'>" + insureModel.getIs_id() + "</span><span id='s-productType' style='display:none;'>" + productModel.getPs_id() + "</span><span id='s-firmDate' style='display:none;'>" + _insureDate + "</span></td></tr>";

                                                            }
                                                            break;
                                                            case 4: {

                                                                Calendar c1 = Calendar.getInstance();
                                                                Date date1 = new Date();
                                                                c1.setTime(date1);
                                                                int year1 = c1.get(Calendar.YEAR);
                                                                int day1 = c1.get(Calendar.DATE);
                                                                c1.set(Calendar.DATE, day1 - 1);
                                                                c1.set(Calendar.YEAR, year1 + 1);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td class='td-y-hov'><a id='a-y-1' data-year='1' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(0) + "' onclick='ChooseExtendYear(1)'>1年</a><span class='span-3'>至" + _extendInsureDate1 + "<span></td>";
                                                                html += "<td class='td-p-hov'><a id='a-p-1' data-price='" + listOriPrice.get(0) + "' data-price1='" + listPrePrice.get(0) + "' data-price2='" + firstlistPrePrice.get(0) + "' onclick='ChooseInsure(1)'>" + listPrePrice.get(0) + "</a></td>";
                                                                html += "</tr>";

                                                                c1.set(Calendar.YEAR, year1 + 2);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td><a id='a-y-2' data-year='2' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(1) + "' onclick='ChooseExtendYear(2)'>2年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                                html += "<td><a id='a-p-2' data-price='" + listOriPrice.get(1) + "' data-price1='" + listPrePrice.get(1) + "' data-price2='" + firstlistPrePrice.get(1) + "' onclick='ChooseInsure(2)'>" + listPrePrice.get(1) + "</a></td>";
                                                                html += "</tr>";

                                                                c1.set(Calendar.YEAR, year1 + 3);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td><a id='a-y-3' data-year='3' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(2) + "' onclick='ChooseExtendYear(3)'>3年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                                html += "<td><a id='a-p-3' data-price='" + listOriPrice.get(2) + "' data-price1='" + listPrePrice.get(2) + "' data-price2='" + firstlistPrePrice.get(2) + "' onclick='ChooseInsure(3)'>" + listPrePrice.get(2) + "</a></td>";
                                                                html += "</tr>";

                                                                c1.set(Calendar.YEAR, year1 + 4);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td><a id='a-y-4' data-year='4' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(3) + "' onclick='ChooseExtendYear(4)'>4年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                                html += "<td><span id='s-insureType' style='display:none;'>" + insureModel.getIs_id() + "</span><span id='s-productType' style='display:none;'>" + productModel.getPs_id() + "</span><span id='s-firmDate' style='display:none;'>" + _insureDate + "</span></td>";
                                                                html += "</tr>";
                                                            }
                                                            break;
                                                            case 5:
                                                            case 6:
                                                            case 7: {
                                                                Calendar c1 = Calendar.getInstance();
                                                                Date date1 = new Date();
                                                                c1.setTime(date1);
                                                                int year1 = c1.get(Calendar.YEAR);
                                                                int day1 = c1.get(Calendar.DATE);
                                                                c1.set(Calendar.DATE, day1 - 1);
                                                                c1.set(Calendar.YEAR, year1 + 1);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td class='td-y-hov'><a id='a-y-1' data-year='1' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(0) + "' onclick='ChooseExtendYear(1)'>1年</a><span class='span-3'>至" + _extendInsureDate1 + "<span></td>";
                                                                html += "<td class='td-p-hov'><a id='a-p-1' data-price='" + listOriPrice.get(0) + "' data-price1='" + listPrePrice.get(0) + "' data-price2='" + firstlistPrePrice.get(0) + "' onclick='ChooseInsure(1)'>" + listPrePrice.get(0) + "</a></td>";
                                                                html += "</tr>";

                                                                c1.set(Calendar.YEAR, year1 + 2);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td><a id='a-y-2' data-year='2' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(1) + "' onclick='ChooseExtendYear(2)'>2年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                                html += "<td><a id='a-p-2' data-price='" + listOriPrice.get(1) + "' data-price1='" + listPrePrice.get(1) + "' data-price2='" + firstlistPrePrice.get(1) + "' onclick='ChooseInsure(2)'>" + listPrePrice.get(1) + "</a></td>";
                                                                html += "</tr>";

                                                                c1.set(Calendar.YEAR, year1 + 3);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td><a id='a-y-3' data-year='3' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(2) + "' onclick='ChooseExtendYear(3)'>3年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                                html += "<td><a id='a-p-3' data-price='" + listOriPrice.get(2) + "' data-price1='" + listPrePrice.get(2) + "' data-price2='" + firstlistPrePrice.get(2) + "' onclick='ChooseInsure(3)'>" + listPrePrice.get(2) + "</a></td>";
                                                                html += "</tr>";

                                                                c1.set(Calendar.YEAR, year1 + 4);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td><a id='a-y-4' data-year='4' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(3) + "' onclick='ChooseExtendYear(4)'>4年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                                html += "<td rowspan='2'><span id='s-insureType' style='display:none;'>" + insureModel.getIs_id() + "</span><span id='s-productType' style='display:none;'>" + productModel.getPs_id() + "</span><span id='s-firmDate' style='display:none;'>" + _insureDate + "</span></td>";
                                                                html += "</tr>";

                                                                c1.set(Calendar.YEAR, year1 + 5);
                                                                _extendInsureDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
                                                                _extendInsureDate1 = new SimpleDateFormat("yyyy年MM月dd日").format(c1.getTime());
                                                                html += "<tr>";
                                                                html += "<td><a id='a-y-5' data-year='5' data-date='" + _extendInsureDate + "' data-rebate='" + listRebate.get(4) + "' onclick='ChooseExtendYear(5)'>5年</a><span class='span-3'>至" + _extendInsureDate1 + "</span></td>";
                                                                html += "</tr>";

                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                                return html;

                                            } else {
                                                return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                                            }
                                        } else {
                                            return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                                        }
                                    } else {
                                        return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                                    }
                                } else {
                                    return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                                }
                            } else {
                                return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                            }
                        } else {
                            return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                        }
                    } else {
                        return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
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

    @Override
    public String GetExtendInsurePrice(HttpServletRequest request) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object year = jsonObject.get("year");
                Object price = jsonObject.get("price");
                Object rebate = jsonObject.get("rebate");
                Object pid = jsonObject.get("pid");
                Object regDate = jsonObject.get("regDate");
                Object keepYears = jsonObject.get("keepYears");
                Object carBrandId = jsonObject.get("carBrandId");
                BigDecimal _insurePrice;
                if (year != null && !"".equals(year) && price != null && !"".equals(price) && rebate != null && !"".equals(rebate)) {
                    if (Integer.valueOf(year.toString()) > 0 && Integer.valueOf(price.toString()) > 0 && new BigDecimal(rebate.toString()).compareTo(new BigDecimal("0")) == 1) {

                        int intPid = Integer.parseInt(pid.toString());
                        intPid = intPid == 0 ? 2 : intPid;
                        int total = 0;
                        int _maxInsureYears = -1; //最大延保年限
                        Ins_projectservice project = projectServiceRepository.getProjectById(intPid);
                        if (project != null) {

                            BigDecimal _rate = new BigDecimal("0.0"); //计算系数f(x)
                            int _insureCoefficient = Integer.parseInt(price.toString()) / 10000; //保额基数
                            if (Integer.valueOf(keepYears.toString()) == -1) {
                                keepYears = 3;
                            }
                            if (Integer.valueOf(keepYears.toString()) >= 0) {

                                String _regDate = regDate.toString();
                                Calendar c = Calendar.getInstance();
                                Date date = null;
                                date = new SimpleDateFormat("yy-MM-dd").parse(_regDate.toString());
                                c.setTime(date);
                                int daynew = c.get(Calendar.DATE);
                                int yearnew = c.get(Calendar.YEAR);
                                c.set(Calendar.DATE, daynew - 1);
                                c.set(Calendar.YEAR, yearnew + Integer.parseInt(keepYears.toString()));
                                String _insureDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
                                Date d = new SimpleDateFormat("yyyy-MM-dd").parse(_insureDate);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                if (d.compareTo(new Date()) == 1) //保修期内
                                {
                                    _maxInsureYears = 8 - Integer.valueOf(keepYears.toString());
                                }
                                if (d.compareTo(new Date()) == -1) //保修期外
                                {
                                    long ts1 = System.currentTimeMillis();
                                    long ts2 = sdf.parse(_insureDate).getTime();
                                    long differentYear = (ts1 - ts2) / 86400000 / 365;
                                    if (differentYear >= 0) {
                                        Integer _years = new BigDecimal(differentYear * 1.0 / 365).setScale(0, BigDecimal.ROUND_UP).intValue();
                                        _maxInsureYears = 8 - Integer.valueOf(keepYears.toString()) - _years;
                                    }
                                }

                                switch (_maxInsureYears) {
                                    case 1: {
                                        _rate = BigDecimal.valueOf(0.2157 * Math.exp(-0.3633 * _insureCoefficient))
                                                .add(BigDecimal.valueOf(0.05524 * Math.exp(-0.00364 * _insureCoefficient)));
                                    }
                                    break;
                                    case 2: {
                                        _rate = BigDecimal.valueOf(0.1746 * Math.exp(-0.3512 * _insureCoefficient))
                                                .add(BigDecimal.valueOf(0.04495 * Math.exp(-0.002345 * _insureCoefficient)));
                                    }
                                    break;
                                    case 3: {
                                        _rate = BigDecimal.valueOf(0.1494 * Math.exp(-0.3618 * _insureCoefficient))
                                                .add(BigDecimal.valueOf(0.0383 * Math.exp(-0.003527 * _insureCoefficient)));
                                    }
                                    break;
                                    case 4: {
                                        _rate = BigDecimal.valueOf(0.1358 * Math.exp(-0.3619 * _insureCoefficient))
                                                .add(BigDecimal.valueOf(0.0348 * Math.exp(-0.003526 * _insureCoefficient)));
                                    }
                                    break;
                                    case 5:
                                    case 6:
                                    case 7: {
                                        _rate = BigDecimal.valueOf(0.1246 * Math.exp(-0.3942 * _insureCoefficient))
                                                .add(BigDecimal.valueOf(0.03767 * Math.exp(-0.01595 * _insureCoefficient)));
                                    }
                                    break;
                                }
                            }

                            //增加年份系数
                            //计算当前年与登记日期年份差
                            String _firstregDate = regDate.toString();
                            BigDecimal _yearrate = new BigDecimal("1.00"); //计算年份系数f(x)
                            //第一年  1.00
                            //第二年  1.02
                            //第三年  1.05
                            //第四年  1.08
                            //第五年  1.19
                            //第六年  1.43
                            //第七年  1.71
                            Calendar c = Calendar.getInstance();
                            Date date = new Date();
                            c.setTime(date);
                            int daynew = c.get(Calendar.YEAR);
                            c.set(Calendar.YEAR, daynew - 1);
                            String lastYear = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
                            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(lastYear);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            long ly = sdf.parse(lastYear).getTime();
                            long fd = sdf.parse(_firstregDate).getTime();

                            long _firstyear = (ly - fd) / 86400000;
                            if (_firstyear <= 365) {
                                _yearrate = new BigDecimal("1.00");
                            } else if (_firstyear <= 365 * 2) {
                                _yearrate = new BigDecimal("1.02");
                            } else if (_firstyear <= 365 * 3) {
                                _yearrate = new BigDecimal("1.05");
                            } else if (_firstyear <= (365 * 4)) {
                                _yearrate = new BigDecimal("1.08");
                            } else if (_firstyear <= (365 * 5)) {
                                _yearrate = new BigDecimal("1.19");
                            } else if (_firstyear <= (365 * 6)) {
                                _yearrate = new BigDecimal("1.43");
                            } else if (_firstyear <= (365 * 7)) {
                                _yearrate = new BigDecimal("1.71");
                            }

                            BigDecimal _tempPrice = new BigDecimal(year.toString()).multiply(project.getPs_coefficient()).multiply(new BigDecimal(price.toString())).multiply(_rate).multiply(_yearrate);

                            //用户配置的折扣
                            BigDecimal zk = new BigDecimal("1");             //用户配置折扣

                            _insurePrice = _tempPrice.multiply(zk).divide(project.getPs_coefficient()).setScale(0, BigDecimal.ROUND_CEILING);

                            return String.valueOf(_insurePrice);
                        } else {
                            return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
                        }
                    } else {
                        return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"参数异常\"}");
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

    @Override
    public String uploadPhoto(HttpServletRequest request) {
        try {
            String content = HttpUtil.getPostByTextPlain(request);
//            String reqMsg = request.getParameter("reqMsg");
            String reqMsg = content.substring(content.indexOf("=") + 1, content.length());
            reqMsg = URLDecoder.decode(reqMsg);
            if (reqMsg != null && !"".equals(reqMsg)) {
                reqMsg = Base64Util.decoderBASE64(reqMsg);

                JSONObject jsonObject;
                jsonObject = JSONObject.parseObject(reqMsg);
                Object img_code = jsonObject.get("img_code");
                Object img_type = jsonObject.get("img_type");
                Object creator = jsonObject.get("creator");

                if (img_code != null && !"".equals(img_code) && img_type != null && !"".equals(img_type)
                        && creator != null && !"".equals(creator)) {

                    String FilePath = "/exchangeImg/default.jpg";
                    String img_base64 = img_code.toString();
                    img_base64 = img_base64.trim();
                    img_base64 = img_base64.replaceAll("\n", "");
                    img_base64 = img_base64.replaceAll("\r", "");
                    img_base64 = img_base64.replaceAll("\t", "");
                    img_base64 = img_base64.replaceAll(" ", "+");
                    if (img_base64 == null) { //图像数据为空
                        return Base64Util.encoderBASE64("{\"code\": 201,\"msg\": \"图像数据为空\"}");
                    }
                    BASE64Decoder decoder = new BASE64Decoder();
                    //Base64解码
                    byte[] b = decoder.decodeBuffer(img_base64);
                    for (int i = 0; i < b.length; ++i) {
                        if (b[i] < 0) {//调整异常数据
                            b[i] += 256;
                        }
                    }
                    long currentTime = System.currentTimeMillis();
                    //生成jpeg图片
                    String photoName = currentTime + "_" + img_type + "_" + creator;
//                    String imgFilePath = "/usr/local/tomcat/apache-tomcat-7.0.82/webapps/";//服务器
                    String imgFilePath = "E:/";//本地测试
                    FilePath = "/customerImg/" + photoName + ".jpg";//新生成的图片

                    OutputStream out = new FileOutputStream(imgFilePath + FilePath);
                    out.write(b);
                    out.flush();
                    out.close();

                    saveLog(creator.toString(), "长传订单图片：" + FilePath);
                    return Base64Util.encoderBASE64("{\"code\": 200,\"msg\": \"" + FilePath + "\"}");
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
    public String saveOrderInfo(HttpServletRequest request) {
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

                List<Yb_Order_Info> orderInfoList = ybOrderInfoRepository.getOrderList(yb_order_id.toString(),yb_customer_name.toString(),yb_creator.toString(),yb_car_number.toString(),
                        yb_car_vin_number.toString(),yb_order_store.toString(),bi_create_time_start,bi_create_time_end);
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
