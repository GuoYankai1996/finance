package com.huobi.sdk.model.account;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountBalance {

  private Long id;

  private String type;

  private String state;

  private List<Balance> list;

  private String subType;

}
