package com.huobi.sdk.model.account;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by guoyankai on 2021/4/17
 */
@Data
public class AccountDsdt {
    //交易对
    private String contract_code;
    //保证金率
    private BigDecimal risk_rate;
    //预估强平价
    private BigDecimal liquidation_price;
    //账户权益
    private BigDecimal margin_balance;

}
