package com.binance.client.model.market;

import com.binance.client.constant.BinanceApiConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * {
 *     "symbol": "BTCUSDT",                // 交易对
 *     "markPrice": "11793.63104562",      // 标记价格
 *     "lastFundingRate": "0.00038246",    // 最近更新的资金费率
 *     "nextFundingTime": 1597392000000,   // 下次资金费时间
 *     "time": 1597370495002               // 更新时间
 * }
 **/
public class MarkPrice {

    private String symbol;

    private BigDecimal markPrice;

    private BigDecimal lastFundingRate;

    private Long nextFundingTime;

    private Long time;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getMarkPrice() {
        return markPrice;
    }

    public void setMarkPrice(BigDecimal markPrice) {
        this.markPrice = markPrice;
    }

    public BigDecimal getLastFundingRate() {
        return lastFundingRate;
    }

    public void setLastFundingRate(BigDecimal lastFundingRate) {
        this.lastFundingRate = lastFundingRate;
    }

    public Long getNextFundingTime() {
        return nextFundingTime;
    }

    public void setNextFundingTime(Long nextFundingTime) {
        this.nextFundingTime = nextFundingTime;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE).append("symbol", symbol)
                .append("markPrice", markPrice).append("lastFundingRate", lastFundingRate)
                .append("nextFundingTime", nextFundingTime).append("time", time).toString();
    }
}
