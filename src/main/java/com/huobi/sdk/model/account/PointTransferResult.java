package com.huobi.sdk.model.account;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PointTransferResult {

  String transactId;

  Long transactTime;

}
