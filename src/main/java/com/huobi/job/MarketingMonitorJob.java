package com.huobi.job;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.huobi.notice.MessageUtil;
import com.huobi.sdk.client.impl.HuobiMarketingService;
import com.huobi.sdk.constant.HuobiOptions;
import com.huobi.sdk.model.UserEntity;
import com.huobi.sdk.model.market.MarketPrice;
import com.huobi.sdk.model.market.PriceCompare;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by guoyankai on 2021/4/18
 */
@Component
@Slf4j
public class MarketingMonitorJob {
    private static final List<UserEntity> users = Lists.newArrayList(
            //guo
            new UserEntity("7e987a55-46138975-gr4edfki8l-36827", "9217c11f-b232cba6-73ef94ac-3658e", "UID_BkZzfxxYtyNMhpzmiJDllCObK5TU", "郭颜恺")
            //wtr
            , new UserEntity("rfhfg2mkl3-da7f7a36-02fd456c-3584b", "cbe9ffd8-691a8ad7-6d9cb111-80323", "UID_NuNElLdv7LNgTFZwkOY9cuEa7oF8", "王天任")
    );

    @PostConstruct
    public void init() {
        users.forEach(u -> {
            //MessageUtil.sendMessage(u, Lists.newArrayList("市场机会job上线"));
        });
    }

    public static void main(String[] args) {
        new MarketingMonitorJob().scheduleAtFixedRate();
    }
    @Scheduled(fixedRate = 300 * 1000L)
    public void scheduleAtFixedRate() {
        HuobiMarketingService s = new HuobiMarketingService(HuobiOptions.builder()
                .build());
        List<MarketPrice> usdtMarkeing = s.getUSDTMarkeing();
        List<MarketPrice> usdMarkeing = s.getUSDMarkeing();
        HuobiMarketingService s1 = new HuobiMarketingService(HuobiOptions.builder()
                .restHost("https://api.huobi.pro")
                .build());
        List<MarketPrice> marketing = s1.getMarketing();
        List<String> messages=new LinkedList<>();
        //现货-usdt
        List<PriceCompare> priceCompares = getPriceCompares(marketing, usdtMarkeing);
        List<String> usdtString = getUsdtString(priceCompares);
        messages.addAll(usdtString);
        //现货-usd
        List<PriceCompare> priceCompares2 = getPriceCompares(marketing, usdMarkeing);
        List<String> usdString = getUsdString(priceCompares2);
        messages.addAll(usdString);
        System.out.println(JSON.toJSONString(messages));
        users.forEach(u -> {

            if (!messages.isEmpty()) {
                MessageUtil.sendMessage(u,messages);
            }
        });
    }

    private List<String> getUsdtString(List<PriceCompare> compares) {
        List<String> strings = compares.stream()
                .filter(c->c.getRate().compareTo(new BigDecimal("3"))>0)
                .map(c -> {
            return String.format("usdt-现货：品种：%s，差异比率 %s%%，现货价格%s，usdt价格%s", c.getSymbol(), c.getRate().setScale(2, RoundingMode.HALF_EVEN).toPlainString(), c.getPrice1(), c.getPrice2());
        }).collect(Collectors.toList());
        return strings;
    }
    private List<String> getUsdString(List<PriceCompare> compares) {
        List<String> strings = compares.stream()
                .filter(c->c.getRate().compareTo(new BigDecimal("3"))>0)
                .map(c -> {
            return String.format("USD-现货：品种：%s，差异比率 %s%%，现货价格%s，USD价格%s", c.getSymbol(), c.getRate().setScale(2, RoundingMode.HALF_EVEN).toPlainString(), c.getPrice1(), c.getPrice2());
        }).collect(Collectors.toList());
        return strings;
    }


    public static List<PriceCompare> getPriceCompares(List<MarketPrice> m1,List<MarketPrice> m2) {
        List<PriceCompare> compare = m1.stream().map(usdt -> {
            Optional<MarketPrice> usdPrice = m2.stream().filter(usd -> StringUtils.equalsIgnoreCase(usd.getSymbol(),usdt.getSymbol())).findAny();
            if (usdPrice.isPresent()) {
                return new PriceCompare(usdt.getSymbol(), usdt.getPrice(), usdPrice.get().getPrice(), null);
            }
            return null;
        }).filter(e -> e != null&&!StringUtils.equalsIgnoreCase(e.getSymbol(),"iota")).collect(Collectors.toList());
        List<PriceCompare> compares = compare.stream().map(c -> {
            BigDecimal usdPrice = c.getPrice2();
            BigDecimal usdTPrice = c.getPrice1();
            BigDecimal rateAmount = usdTPrice.subtract(usdPrice);
            //加权差异比率。    （ a比率+b比率）/2
            BigDecimal rate = rateAmount.divide(usdTPrice, 8, RoundingMode.HALF_EVEN).add(rateAmount.divide(usdPrice, 8, RoundingMode.HALF_EVEN)).divide(new BigDecimal(2), 8, RoundingMode.HALF_EVEN);
            c.setRate(rate.multiply(new BigDecimal("100")));
            return c;
        }).sorted((a, b) -> b.getRate().compareTo(a.getRate())).collect(Collectors.toList());
        return compares;
    }

    ;


}
