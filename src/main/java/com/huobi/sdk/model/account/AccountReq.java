package com.huobi.sdk.model.account;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountReq {

  private String topic;

  private Long ts;

  private String cid;

  private List<AccountBalance> balanceList;

}
