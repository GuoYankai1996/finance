package com.huobi.sdk.model.account;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Point {

  Long accountId;

  String accountStatus;

  String acctBalance;

  List<Group> groupIds;
}
