package com.huobi.sdk.model.market;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by guoyankai on 2021/4/18
 */
@Data
public class MarketPrice {
    //品种代码
    private String symbol;
    //交易对
    private String contract_code;
    //指数价格
    private BigDecimal price;
}
