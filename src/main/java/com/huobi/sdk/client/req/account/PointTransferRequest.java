package com.huobi.sdk.client.req.account;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointTransferRequest {

  private Long fromUid;

  private Long toUid;

  private Long groupId;

  private BigDecimal amount;

}
