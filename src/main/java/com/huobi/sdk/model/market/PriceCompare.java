package com.huobi.sdk.model.market;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by guoyankai on 2021/4/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceCompare {
    //品种代码
    private String symbol;
    private BigDecimal usdTPrice;
    private BigDecimal usdPrice;
    //价差比例(百分位) 大小 0-100
    private BigDecimal rate;
}
