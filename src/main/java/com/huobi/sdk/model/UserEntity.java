package com.huobi.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by guoyankai on 2021/4/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    //火币密钥
    private String accessToken;
    private String accessSecret;
    //微信消息通知uid
    private String uid;
    private String userName;
}
