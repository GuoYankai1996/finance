package com.binance.client.model.market;

import com.binance.client.constant.BinanceApiConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;


/**
 * {
 *     "exchangeFilters": [],
 *     "rateLimits": [ // API访问的限制
 *         {
 *             "interval": "MINUTE", // 按照分钟计算
 *             "intervalNum": 1, // 按照1分钟计算
 *             "limit": 2400, // 上限次数
 *             "rateLimitType": "REQUEST_WEIGHT" // 按照访问权重来计算
 *         },
 *         {
 *             "interval": "MINUTE",
 *             "intervalNum": 1,
 *             "limit": 1200,
 *             "rateLimitType": "ORDERS" // 按照订单数量来计算
 *         }
 *     ],
 *     "serverTime": 1565613908500, // 系统时间
 *     "symbols": [ // 交易对信息
 *         {
 *             "symbol": "BLZUSDT",  // 交易对
 *             "pair": "BLZUSDT",  // 标的交易对
 *             "contractType": "PERPETUAL",    // 合约类型
 *             "deliveryDate": 4133404800000,  // 交割日期
 *             "onboardDate": 1598252400000,     // 上线日期
 *             "status": "TRADING",  // 交易对状态
 *             "maintMarginPercent": "2.5000",  // 请忽略
 *             "requiredMarginPercent": "5.0000", // 请忽略
 *             "baseAsset": "BLZ",  // 标的资产
 *             "quoteAsset": "USDT", // 报价资产
 *             "marginAsset": "USDT", // 保证金资产
 *             "pricePrecision": 5,  // 价格小数点位数
 *             "quantityPrecision": 0,  // 数量小数点位数
 *             "baseAssetPrecision": 8,  // 标的资产精度
 *             "quotePrecision": 8,  // 报价资产精度
 *             "underlyingType": "COIN",
 *             "underlyingSubType": ["STORAGE"],
 *             "settlePlan": 0,
 *             "triggerProtect": "0.15", // 开启"priceProtect"的条件订单的触发阈值
 *             "filters": [
 *                 {
 *                     "filterType": "PRICE_FILTER", // 价格限制
 *                     "maxPrice": "300", // 价格上限, 最大价格
 *                     "minPrice": "0.0001", // 价格下限, 最小价格
 *                     "tickSize": "0.0001" // 步进间隔
 *                 },
 *                 {
 *                     "filterType": "LOT_SIZE", // 数量限制
 *                     "maxQty": "10000000", // 数量上限, 最大数量
 *                     "minQty": "1", // 数量下限, 最小数量
 *                     "stepSize": "1" // 允许的步进值
 *                 },
 *                 {
 *                     "filterType": "MARKET_LOT_SIZE", // 市价订单数量限制
 *                     "maxQty": "590119", // 数量上限, 最大数量
 *                     "minQty": "1", // 数量下限, 最小数量
 *                     "stepSize": "1" // 允许的步进值
 *                 },
 *                 {
 *                     "filterType": "MAX_NUM_ORDERS", // 最多订单数限制
 *                     "limit": 200
 *                 },
 *                 {
 *                     "filterType": "MAX_NUM_ALGO_ORDERS", // 最多条件订单数限制
 *                     "limit": 100
 *                 },
 *                 {
 *                     "filterType": "MIN_NOTIONAL",  // 最小名义价值
 *                     "notional": "1",
 *                 },
 *                 {
 *                     "filterType": "PERCENT_PRICE", // 价格比限制
 *                     "multiplierUp": "1.1500", // 价格上限百分比
 *                     "multiplierDown": "0.8500", // 价格下限百分比
 *                     "multiplierDecimal": 4
 *                 }
 *             ],
 *             "OrderType": [ // 订单类型
 *                 "LIMIT",  // 限价单
 *                 "MARKET",  // 市价单
 *                 "STOP", // 止损单
 *                 "STOP_MARKET", // 止损市价单
 *                 "TAKE_PROFIT", // 止盈单
 *                 "TAKE_PROFIT_MARKET", // 止盈暑市价单
 *                 "TRAILING_STOP_MARKET" // 跟踪止损市价单
 *             ],
 *             "timeInForce": [ // 有效方式
 *                 "GTC", // 成交为止, 一直有效
 *                 "IOC", // 无法立即成交(吃单)的部分就撤销
 *                 "FOK", // 无法全部立即成交就撤销
 *                 "GTX" // 无法成为挂单方就撤销
 *             ]
 *         }
 *     ],
 *     "timezone": "UTC" // 服务器所用的时间区域
 * }
 **/
public class ExchangeInformation {

    private String timezone;

    private Long serverTime;

    private List<RateLimit> rateLimits;

    private List<ExchangeFilter> exchangeFilters;

    private List<ExchangeInfoEntry> symbols;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public List<RateLimit> getRateLimits() {
        return rateLimits;
    }

    public void setRateLimits(List<RateLimit> rateLimits) {
        this.rateLimits = rateLimits;
    }

    public List<ExchangeFilter> getExchangeFilters() {
        return exchangeFilters;
    }

    public void setExchangeFilters(List<ExchangeFilter> exchangeFilters) {
        this.exchangeFilters = exchangeFilters;
    }

    public List<ExchangeInfoEntry> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<ExchangeInfoEntry> symbols) {
        this.symbols = symbols;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE).append("timezone", timezone)
                .append("serverTime", serverTime).append("rateLimits", rateLimits)
                .append("exchangeFilters", exchangeFilters).append("symbols", symbols).toString();
    }
}
