package com.huobi.sdk.client.req.account;

import com.huobi.sdk.constant.enums.AccountUpdateModeEnum;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubAccountUpdateRequest {

  private AccountUpdateModeEnum accountUpdateMode;

}
