package com.huobi.sdk.model.account;

import lombok.*;

import java.math.BigDecimal;

/**
 * The balance of specified account.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Balance {

  private String currency;

  private String type;

  private BigDecimal balance;

}
