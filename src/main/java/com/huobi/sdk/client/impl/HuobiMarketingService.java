package com.huobi.sdk.client.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.huobi.sdk.connection.HuobiRestConnection;
import com.huobi.sdk.constant.HuobiOptions;
import com.huobi.sdk.constant.Options;
import com.huobi.sdk.model.market.*;
import com.huobi.sdk.signature.UrlParamsBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by guoyankai on 2021/4/18
 */
public class HuobiMarketingService {
    public static final String GET_USDT_MARKETING = "/linear-swap-ex/market/trade";

    public static final String GET_USD_MARKETING ="/swap-ex/market/trade";
    //逐仓杠杆查询
    public static final String Get_RENT_1="/v1/margin/loan-info";
    //全仓杠杆查询
    public static final String Get_RENT_2="/v1/cross-margin/loan-info";
    //批量获取USDT合约资金费率
    public static final String GET_FUNDING_RATE_USDT ="/linear-swap-api/v1/swap_batch_funding_rate";
    //批量获取币本位合约资金费率
    public static final String GET_FUNDING_RATE_USD="/swap-api/v1/swap_batch_funding_rate";
    //获取交割合约k线
    public static final String GET_DELIVERY_K="/market/history/kline";
    //获取现货K线
    public static final String GET_MARKET_K="/market/history/kline";
    private Options options;

    private HuobiRestConnection restConnection;

    public HuobiMarketingService(Options options) {
        this.options = options;
        this.restConnection = new HuobiRestConnection(options);
    }

    public static void main(String[] args) {
        HuobiMarketingService s = new HuobiMarketingService(HuobiOptions.builder()
                //密钥对。向不同用户发送
                .apiKey("7e987a55-46138975-gr4edfki8l-36827")
                .secretKey("9217c11f-b232cba6-73ef94ac-3658e")
                .restHost("https://api.huobi.pro")
                .build());
        s.getMarketing();
    }
//    public static void main(String[] args) {
//        HuobiMarketingService s = new HuobiMarketingService(HuobiOptions.builder()
//                //密钥对。向不同用户发送
//                .apiKey("7e987a55-46138975-gr4edfki8l-36827")
//                .secretKey("9217c11f-b232cba6-73ef94ac-3658e")
//                .build());
//        List<KModel> btc_cw = s.getDeliveryMarkeingK("BTC_CQ", "1day", 500);
//        HuobiMarketingService m = new HuobiMarketingService(HuobiOptions.builder()
//                //密钥对。向不同用户发送
//                .apiKey("7e987a55-46138975-gr4edfki8l-36827")
//                .secretKey("9217c11f-b232cba6-73ef94ac-3658e")
//                .restHost("https://api.huobi.pro")
//                .build());
//        List<KModel> btcusdt = m.getMarketK("btcusdt", "1day", 500);
//        List<String> json = btc_cw.stream().map(b -> {
//            String time = b.getId();
//            KModel uModel = btcusdt.stream().filter(u -> u.getId().equals(time))
//                    .findAny().get();
//            BigDecimal bAmount = new BigDecimal(b.getClose());
//            BigDecimal uAmount = new BigDecimal(uModel.getClose());
//            BigDecimal rate = bAmount.subtract(uAmount).divide(uAmount, 4, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
//            return String.format("币种：%s，时间：%s，合约价格：%s，现货价格：%s，溢价率：%s%%",
//                    b.getSymbol(), b.getFormatTime(), b.getClose(), uModel.getClose(), rate.toPlainString());
//        }).collect(Collectors.toList());
//        System.out.println(JSON.toJSONString(json));
//    }
    public List<MarketPrice> getUSDTMarkeing() {

        JSONObject jsonObject = restConnection.executeGet(GET_USDT_MARKETING, UrlParamsBuilder.build());
        String status = jsonObject.getString("status");
        if(!StringUtils.equals("ok",status)){
            throw new RuntimeException("火币接口getUSDTMarkeing响应出错，status="+status);
        }
        String data = jsonObject.getJSONObject("tick").get("data").toString();
        List<MarketPrice> marketPrices = JSON.parseObject(data, new TypeReference<List<MarketPrice>>() {
        });
        marketPrices.forEach(m->{
            m.setSymbol(m.getContract_code().split("-")[0]);
        });
        return marketPrices;
    }
    public List<MarketPrice> getUSDMarkeing() {

        JSONObject jsonObject = restConnection.executeGet(GET_USD_MARKETING, UrlParamsBuilder.build());
        String status = jsonObject.getString("status");
        if(!StringUtils.equals("ok",status)){
            throw new RuntimeException("火币接口响应出错，status="+status);
        }
        String data = jsonObject.getJSONObject("tick").get("data").toString();
        List<MarketPrice> marketPrices = JSON.parseObject(data, new TypeReference<List<MarketPrice>>() {
        });
        marketPrices.forEach(m->{
            m.setSymbol(m.getContract_code().split("-")[0]);
        });
        return marketPrices;
    }
    //现货市场最新交易对
    public List<MarketPrice> getMarketing(){
        JSONObject jsonObject = restConnection.executeGet("/market/tickers", UrlParamsBuilder.build());
        JSONArray data = jsonObject.getJSONArray("data");
        List<MarketPrice> list=new LinkedList<>();
        for (int i=0;i<data.size();i++){
            JSONObject d = data.getJSONObject(i);
            MarketPrice m = new MarketPrice();
            m.setContract_code(d.getString("symbol"));
            m.setPrice(new BigDecimal(d.getString("close")));
            m.setSymbol(d.getString("symbol").replace("usdt",""));
            list.add(m);
        }
        return list;
    }

    //逐仓可用杠杆
    public List<RentChoice> getRentChoice1(){
        JSONObject jsonObject = restConnection.executeGetWithSignature(Get_RENT_1, UrlParamsBuilder.build());
        String status = jsonObject.getString("status");
        if(!StringUtils.equals("ok",status)){
            throw new RuntimeException("火币接口响应出错，status="+status);
        }
        String data = jsonObject.get("data").toString();
        return JSON.parseObject(data, new TypeReference<List<RentChoice>>() {
        });
    }
    //全仓可用杠杆
    public List<RentChoice> getRentChoice2(){
        JSONObject jsonObject = restConnection.executeGetWithSignature(Get_RENT_2, UrlParamsBuilder.build());
        String status = jsonObject.getString("status");
        if(!StringUtils.equals("ok",status)){
            throw new RuntimeException("火币接口响应出错，status="+status);
        }
        String data = jsonObject.get("data").toString();
        return JSON.parseObject(data, new TypeReference<List<RentChoice>>() {
        });
    }
    public List<FundingRate> getFundingRate_USDT(){
        JSONObject jsonObject = restConnection.executeGet(GET_FUNDING_RATE_USDT, UrlParamsBuilder.build());
        String status = jsonObject.getString("status");
        if(!StringUtils.equals("ok",status)){
            throw new RuntimeException("火币接口响应出错，status="+status);
        }
        String data = jsonObject.get("data").toString();
        List<FundingRate> marketPrices = JSON.parseObject(data, new TypeReference<List<FundingRate>>() {
        });
        return marketPrices;
    }
    public List<FundingRate> getFundingRate_USD(){
        JSONObject jsonObject = restConnection.executeGet(GET_FUNDING_RATE_USD, UrlParamsBuilder.build());
        String status = jsonObject.getString("status");
        if(!StringUtils.equals("ok",status)){
            throw new RuntimeException("火币接口响应出错，status="+status);
        }
        String data = jsonObject.get("data").toString();
        List<FundingRate> marketPrices = JSON.parseObject(data, new TypeReference<List<FundingRate>>() {
        });
        return marketPrices;
    }


    public void get(){
        JSONObject jsonObject = restConnection.executeGet("/api/v1/contract_contract_info", UrlParamsBuilder.build());
        System.out.println(jsonObject.toJSONString());
    }

    public List<KModel> getDeliveryMarkeingK(String symbol,String period,Integer size){
        UrlParamsBuilder build = UrlParamsBuilder
                .build()
                .putToUrl("symbol",symbol)
                .putToUrl("period",period)
                .putToUrl("size",size);
        JSONObject jsonObject = restConnection.executeGet(GET_DELIVERY_K, build);
        String status = jsonObject.getString("status");
        if(!StringUtils.equals("ok",status)){
            throw new RuntimeException("火币接口响应出错，status="+status);
        }
        String data = jsonObject.get("data").toString();
        List<KModel> kModels = JSON.parseObject(data, new TypeReference<List<KModel>>() {
        });
        kModels.forEach(k->{
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1000*Long.parseLong(k.getId())));
            k.setFormatTime(format);
            k.setSymbol(symbol);
        });
        return kModels;
    }

    public List<KModel> getMarketK(String symbol,String period,Integer size){
        UrlParamsBuilder build = UrlParamsBuilder
                .build()
                .putToUrl("symbol",symbol)
                .putToUrl("period",period)
                .putToUrl("size",size);
        JSONObject jsonObject = restConnection.executeGet(GET_MARKET_K, build);
        String status = jsonObject.getString("status");
        if(!StringUtils.equals("ok",status)){
            throw new RuntimeException("火币接口响应出错，status="+status);
        }
        String data = jsonObject.get("data").toString();
        List<KModel> kModels = JSON.parseObject(data, new TypeReference<List<KModel>>() {
        });
        kModels.forEach(k->{
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1000*Long.parseLong(k.getId())));
            k.setFormatTime(format);
            k.setSymbol(symbol);
        });
        return kModels;
    }
}
