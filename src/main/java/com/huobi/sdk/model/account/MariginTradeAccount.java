package com.huobi.sdk.model.account;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by guoyankai on 2021/4/23
 */
@Data
public class MariginTradeAccount {
    //交易对 如 bttusdt
    private String symbol;
    //担保资产率，低于 110%会爆仓。  这里返回是 1.1
    @JSONField(name="risk-rate")
    private String riskRate;
}
