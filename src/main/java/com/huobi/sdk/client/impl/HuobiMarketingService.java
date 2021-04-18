package com.huobi.sdk.client.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.huobi.sdk.connection.HuobiRestConnection;
import com.huobi.sdk.constant.HuobiOptions;
import com.huobi.sdk.constant.Options;
import com.huobi.sdk.model.market.FundingRate;
import com.huobi.sdk.model.market.FundingRateCompare;
import com.huobi.sdk.model.market.MarketPrice;
import com.huobi.sdk.model.market.RentChoice;
import com.huobi.sdk.signature.UrlParamsBuilder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                .build());
        List<FundingRate> fundingRate_usdt = s.getFundingRate_USDT();
        List<FundingRate> fundingRate_usd = s.getFundingRate_USD();
        List<FundingRateCompare> collect = fundingRate_usdt.stream().map(t -> {
            Optional<FundingRate> dO = fundingRate_usd.stream().filter(d -> d.getSymbol().equals(t.getSymbol())).findAny();
            if (dO.isPresent()) {
                BigDecimal dRate = new BigDecimal(dO.get().getFunding_rate()).multiply(new BigDecimal("100")).setScale(3, RoundingMode.HALF_EVEN);
                BigDecimal tRate = new BigDecimal(t.getFunding_rate()).multiply(new BigDecimal("100")).setScale(3, RoundingMode.HALF_EVEN);
                BigDecimal abs1 = dRate.subtract(tRate).abs();
                BigDecimal temp1 = (abs1.compareTo(tRate.abs()) > 0) ? abs1 : tRate.abs();
                BigDecimal max = temp1.compareTo(dRate.abs()) > 0 ? temp1 : dRate.abs();
                return FundingRateCompare.builder().usdRate(dRate)
                        .usdtRate(tRate)
                        .maxRate(max)
                        .symbol(dO.get().getSymbol()).build()
                        ;
            }
            return null;
        }).filter(a -> a != null).sorted((a, b) -> b.getMaxRate().compareTo(a.getMaxRate())).collect(Collectors.toList());

    }
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

    public  List<RentChoice> getRentChoice(){
        List<RentChoice> rentChoice1 = getRentChoice1();
        return rentChoice1;
//        List<RentChoice> rentChoice2 = getRentChoice2();
//        List<RentChoice> collect = rentChoice1.stream().filter(r1 -> {
//            return rentChoice2.stream().anyMatch(r2 -> r1.getCurrency().equals(r2.getCurrency()));
//        }).collect(Collectors.toList());
//        return collect;
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
}
