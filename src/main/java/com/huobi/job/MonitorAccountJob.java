package com.huobi.job;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.huobi.notice.MessageUtil;
import com.huobi.sdk.client.impl.HuobiAccountService;
import com.huobi.sdk.constant.HuobiOptions;
import com.huobi.sdk.model.UserEntity;
import com.huobi.sdk.model.account.AccountDetail;
import com.huobi.sdk.model.account.MariginTradeAccount;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import com.zjiecode.wxpusher.client.bean.MessageResult;
import com.zjiecode.wxpusher.client.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by guoyankai on 2021/4/18
 */
@Component
@Slf4j
public class MonitorAccountJob {
    private static final List<UserEntity> users = Lists.newArrayList(
            //guo
            new UserEntity("7e987a55-46138975-gr4edfki8l-36827", "9217c11f-b232cba6-73ef94ac-3658e", "UID_BkZzfxxYtyNMhpzmiJDllCObK5TU", "郭颜恺")
            //wtr
            , new UserEntity("rfhfg2mkl3-da7f7a36-02fd456c-3584b", "cbe9ffd8-691a8ad7-6d9cb111-80323", "UID_NuNElLdv7LNgTFZwkOY9cuEa7oF8", "王天任")
    );

//    public static void main(String[] args) throws Exception {
//        HuobiAccountService walletService = new HuobiAccountService(HuobiOptions.builder()
//                //密钥对。向不同用户发送
//                .apiKey("7e987a55-46138975-gr4edfki8l-36827")
//                .secretKey("9217c11f-b232cba6-73ef94ac-3658e")
//                .restHost("https://api.huobi.pro")
//                .build());
//        List<MariginTradeAccount> marginTrading = walletService.getMarginTrading();
//        List<String> strings = validMarginTrade(marginTrading);
//        System.out.println(JSON.toJSONString(strings));
//    }
    @PostConstruct
    public void init(){
        users.forEach(u->{
            MessageUtil.sendMessage(u,Lists.newArrayList("监听job上线"));
        });
    }
    @Scheduled(fixedRate = 60 * 1000L)
    public void scheduleAtFixedRate() throws Exception {
        log.info("监控账户job运行");
        users.forEach(u -> {
            try {
                List<String> messages=new LinkedList<>();
                //合约账户
                HuobiAccountService walletService = new HuobiAccountService(HuobiOptions.builder()
                        //密钥对。向不同用户发送
                        .apiKey(u.getAccessToken())
                        .secretKey(u.getAccessSecret())
                        .build());
                //U本位监控
                List<AccountDetail> usdtAccountDetail = walletService.getUsdtAccountDetail();
                messages.addAll(validUsdtAccountDetail(usdtAccountDetail));
                //币本位监控
                List<AccountDetail> usdAccountDetail = walletService.getUsdAccountDetail();
                messages.addAll(validUsdAccountDetail(usdAccountDetail));
                //现货账户
                HuobiAccountService marginWalletService = new HuobiAccountService(HuobiOptions.builder()
                        //密钥对。向不同用户发送
                        .apiKey(u.getAccessToken())
                        .secretKey(u.getAccessSecret())
                        .restHost("https://api.huobi.pro")
                        .build());
                //现货逐仓杠杆监控
                List<MariginTradeAccount> marginTrading = marginWalletService.getMarginTrading();
                messages.addAll(validMarginTrade(marginTrading));

                if (!messages.isEmpty()) {
                    MessageUtil.sendMessage(u, messages);
                }
            } catch (Exception e) {
                log.error("监控账户job出现异常", e);
                MessageUtil.sendMessage(u, Lists.newArrayList("监控账户job出现异常:" + e.getMessage()));
            }
        });
        log.info("监控账户job结束");

    }



    private List<String> validUsdtAccountDetail(List<AccountDetail> usdtAccountDetail) {
        ArrayList<String> notice = Lists.newArrayList();
        usdtAccountDetail.forEach(a -> {

            if (a.getRisk_rate() != null && a.getRisk_rate().compareTo(new BigDecimal("0.5")) < 0) {
                String r = String.format("USDT本位永续合约：%s，保证金率：%s%%，账户权益：%s（USDT），预估强平价：%s（USDT）",
                        a.getContract_code(),
                        a.getRisk_rate().multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_EVEN).toPlainString(),
                        a.getMargin_balance().setScale(2, RoundingMode.HALF_EVEN).toPlainString(),
                        a.getLiquidation_price().setScale(10, RoundingMode.HALF_EVEN).toPlainString());
                notice.add(r);
            }
        });
        return notice;
    }
    private List<String> validUsdAccountDetail(List<AccountDetail> usdtAccountDetail) {
        ArrayList<String> notice = Lists.newArrayList();
        usdtAccountDetail.forEach(a -> {

            if (a.getRisk_rate() != null
                    && a.getRisk_rate().compareTo(new BigDecimal("0.5")) < 0
            ) {
                String r = String.format("币本位永续合约：%s，保证金率：%s%%，账户权益：%s（%s），预估强平价：%s（USD）",
                        a.getContract_code(),
                        a.getRisk_rate().multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_EVEN).toPlainString(),
                        a.getMargin_balance().setScale(2, RoundingMode.HALF_EVEN).toPlainString(),
                        a.getSymbol(),
                        a.getLiquidation_price().setScale(10, RoundingMode.HALF_EVEN).toPlainString());
                notice.add(r);
            }
        });
        return notice;
    }
    private List<String> validMarginTrade(List<MariginTradeAccount> accountDetail) {
        ArrayList<String> notice = Lists.newArrayList();
        accountDetail.forEach(a -> {
            BigDecimal riskRate = new BigDecimal(a.getRiskRate()).multiply(new BigDecimal("100"));
            if (riskRate.compareTo(new BigDecimal("130"))<0
            ) {
                String r = String.format("现货逐仓杠杆预警：%s，风险率：%s%%",
                        a.getSymbol(),riskRate.setScale(3,RoundingMode.HALF_EVEN).toPlainString());
                notice.add(r);
            }
        });
        return notice;
    }
}
