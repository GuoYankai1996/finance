package com.huobi.sdk.model.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountLedgerResult {

  private Long nextId;

  private List<AccountLedger> ledgerList;

}
