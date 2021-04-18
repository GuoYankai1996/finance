package com.huobi.sdk.model.market;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by guoyankai on 2021/4/18
 */
@Data
@Builder
public class FundingRateCompare {
    //品种代码
    private String symbol;
    private BigDecimal usdtRate;
    private BigDecimal usdRate;
    private BigDecimal maxRate;
}
