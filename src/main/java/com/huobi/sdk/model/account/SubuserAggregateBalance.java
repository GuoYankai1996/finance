package com.huobi.sdk.model.account;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubuserAggregateBalance {

  private String currency;

  private String type;

  private BigDecimal balance;
}
