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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Scheduled(fixedRate = 300 * 1000L)
    public void scheduleAtFixedRate() throws Exception {
        users.forEach(u -> {
            HuobiMarketingService s = new HuobiMarketingService(HuobiOptions.builder()
                    //密钥对。向不同用户发送
                    .apiKey(u.getAccessToken())
                    .secretKey(u.getAccessSecret())
                    .build());
            List<MarketPrice> usdtMarkeing = s.getUSDTMarkeing();
            List<MarketPrice> usdMarkeing = s.getUSDMarkeing();
            List<PriceCompare> compare = usdtMarkeing.stream().map(usdt -> {
                Optional<MarketPrice> usdPrice = usdMarkeing.stream().filter(usd -> usd.getSymbol().equals(usdt.getSymbol())).findAny();
                if (usdPrice.isPresent()) {
                    return new PriceCompare(usdt.getSymbol(), usdt.getPrice(), usdPrice.get().getPrice(), null);
                }
                return null;
            }).filter(e -> e != null).collect(Collectors.toList());
            List<PriceCompare> compares = compare.stream().map(c -> {
                BigDecimal usdPrice = c.getUsdPrice();
                BigDecimal usdTPrice = c.getUsdTPrice();
                BigDecimal rateAmount = usdPrice.subtract(usdTPrice).abs();
                //加权差异比率。    （ a比率+b比率）/2
                BigDecimal rate = rateAmount.divide(usdTPrice, 8, RoundingMode.HALF_EVEN).add(rateAmount.divide(usdPrice, 8, RoundingMode.HALF_EVEN)).divide(new BigDecimal(2), 8, RoundingMode.HALF_EVEN);
                c.setRate(rate.multiply(new BigDecimal("100")));
                return c;
            }).sorted((a, b) -> b.getRate().compareTo(a.getRate())).collect(Collectors.toList());
            //大于8个点就发通知！！！
            List<String> messages = compares.stream().filter(c -> c.getRate().compareTo(new BigDecimal("8")) > 0).map(c -> {
                return String.format("%s的币本位合约与USDT本位合约出现%s%%差异，可以行动！。usd=%s，usdt=%s", c.getSymbol(), c.getRate().toPlainString(),c.getUsdPrice().toPlainString(),c.getUsdTPrice().toPlainString());
            }).collect(Collectors.toList());
            if (!messages.isEmpty()) {
                MessageUtil.sendMessage(u,messages);
            }
        });
    };


}
