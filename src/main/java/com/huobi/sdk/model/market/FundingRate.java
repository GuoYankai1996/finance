package com.huobi.sdk.model.market;

import lombok.Builder;
import lombok.Data;

/**
 * Created by guoyankai on 2021/4/18
 * 资金费
 */
@Data
public class FundingRate {
    //品种代码
    private String symbol;
    //当期资金费
    private String funding_rate;
}
