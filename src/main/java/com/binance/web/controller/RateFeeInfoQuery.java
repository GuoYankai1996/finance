package com.binance.web.controller;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.constant.PrivateConfig;
import com.binance.client.model.market.ExchangeInfoEntry;
import com.binance.client.model.market.ExchangeInformation;
import com.binance.client.model.market.FundingRate;
import com.binance.client.model.trade.PositionRisk;
import com.binance.web.util.BigDecimalCollectorsUtil;
import com.binance.web.util.DateUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class RateFeeInfoQuery {

    public static FundingRate get_funding_rate_by_symbol(SyncRequestClient syncRequestClient, String symbol) {
        List<FundingRate> fundingRates = syncRequestClient.getFundingRate(symbol, null, null, 1);
        if (fundingRates.size() > 0) {
            return fundingRates.get(0);
        }
        return null;
    }

    public static SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
            new RequestOptions());

    public static void main(String[] args) {
        RateFeeInfoQuery rateFeeInfoQuery = new RateFeeInfoQuery();
        //打印所有交易对的资金费
        rateFeeInfoQuery.printAllSymbolsFundingRate();
        //打印持有的交易对信息以及对应的资金费
        rateFeeInfoQuery.printPositionAndRateFeeInfo();
    }

    public void printPositionAndRateFeeInfo() {
        //持仓的资金费率
        List<FundingRate> rateList = Lists.newArrayList();
        List<BigDecimal> feeList = Lists.newArrayList();
        //用户持仓信息
        List<PositionRisk> allPositions = syncRequestClient.getPositionRisk();
        allPositions.stream()
                .filter(p -> p.getPositionAmt().longValue() != 0)
                .forEach(positionRisk -> {
                    //持仓价值
                    BigDecimal value = positionRisk.getPositionAmt().multiply(positionRisk.getMarkPrice()).abs();
                    FundingRate fundingRate = get_funding_rate_by_symbol(syncRequestClient, positionRisk.getSymbol());
                    if (fundingRate != null) {
                        //上期资金费
                        BigDecimal fee = fundingRate.getFundingRate().multiply(value);
                        System.out.println("交易对：" + positionRisk.getSymbol() + ",持仓价值：" + value + ",上期资金费率：" + fundingRate.getFundingRate() + ",上期资金费：" + fee);
                        feeList.add(fee);
                        rateList.add(fundingRate);
                    }
                });

        BigDecimal avage = rateList.stream().collect(BigDecimalCollectorsUtil.averagingBigDecimal(FundingRate::getFundingRate, 8, BigDecimal.ROUND_HALF_UP));
        System.out.println("#####################上期平均资金费率=" + avage);
        BigDecimal sum = feeList.stream().collect(BigDecimalCollectorsUtil.summingBigDecimal(p -> p));
        BigDecimal max = rateList.stream().collect(BigDecimalCollectorsUtil.maxBy(FundingRate::getFundingRate));
        BigDecimal min = rateList.stream().collect(BigDecimalCollectorsUtil.minBy(FundingRate::getFundingRate));
        System.out.println("#####################上期资金费总和=" + sum + "，最大资金费率：" + max + ",最小资金费率：" + min);
    }

    /**
     * @Description: 打印所有交易对资金费率
     **/
    public void printAllSymbolsFundingRate() {
        //获取交易对信息
        ExchangeInformation exInfo = syncRequestClient.getExchangeInformation();
        List<ExchangeInfoEntry> symbols = exInfo.getSymbols();

        List<FundingRate> fundingRates = Lists.newArrayList();
        symbols.forEach(symbol -> {
            //查询对应币种上期资金费
            List<FundingRate> fundings = syncRequestClient.getFundingRate(symbol.getSymbol(), null, null, 1);
            if (!CollectionUtils.isEmpty(fundings)) {
                fundingRates.add(fundings.get(0));
            }
        });

        fundingRates.sort(Comparator.comparing(FundingRate::getFundingRate).reversed());

        //打印所有交易对资金费
        System.out.println("#####################资金费率（百分制）#####################");
        fundingRates.forEach(fundingRate -> {
            System.out.println(DateUtil.timeStamp2DataString(fundingRate.getFundingTime()) + ":" + fundingRate.getSymbol() + "：" + fundingRate.getFundingRate() + "%");
        });
        //四舍五入，8位小数
        BigDecimal avage = fundingRates.stream().collect(BigDecimalCollectorsUtil.averagingBigDecimal(FundingRate::getFundingRate, 8, BigDecimal.ROUND_HALF_UP));
        System.out.println("#####################上期平均资金费率=" + avage);
    }
}
