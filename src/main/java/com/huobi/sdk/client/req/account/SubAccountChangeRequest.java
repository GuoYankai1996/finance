package com.huobi.sdk.client.req.account;

import com.huobi.sdk.constant.enums.BalanceModeEnum;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubAccountChangeRequest {

  private BalanceModeEnum balanceMode;

}
