package com.huobi.sdk.model.account;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountChange {

  private Long accountId;

  private String currency;

  private String type;

  private BigDecimal balance;

}
