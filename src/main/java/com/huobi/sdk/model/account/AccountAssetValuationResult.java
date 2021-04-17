package com.huobi.sdk.model.account;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountAssetValuationResult {

  BigDecimal balance;

  Long timestamp;

}
