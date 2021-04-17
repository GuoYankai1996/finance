package com.huobi.sdk.client.req.account;

import com.huobi.sdk.constant.enums.AccountTransferAccountTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransferRequest {

  private Long fromUser;

  private AccountTransferAccountTypeEnum fromAccountType;

  private Long fromAccount;

  private Long toUser;

  private AccountTransferAccountTypeEnum toAccountType;

  private Long toAccount;

  private String currency;

  private BigDecimal amount;

}
