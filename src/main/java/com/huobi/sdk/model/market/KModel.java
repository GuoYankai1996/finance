package com.huobi.sdk.model.market;

import lombok.Data;

/**
 * Created by guoyankai on 2021/4/23
 * K线图
 */
@Data
public class KModel {
    private String symbol;
    //时间戳
    private String id;
    //收盘价
    private Integer close;

    private String formatTime;
}
