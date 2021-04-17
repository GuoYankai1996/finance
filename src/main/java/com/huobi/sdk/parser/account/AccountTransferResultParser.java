package com.huobi.sdk.parser.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.sdk.parser.HuobiModelParser;
import com.huobi.sdk.model.account.AccountTransferResult;

import java.util.List;

public class AccountTransferResultParser implements HuobiModelParser<AccountTransferResult> {

  @Override
  public AccountTransferResult parse(JSONObject json) {
    return AccountTransferResult.builder()
        .transactId(json.getLong("transact-id"))
        .transactTime(json.getLong("transact-time"))
        .build();
  }

  @Override
  public AccountTransferResult parse(JSONArray json) {
    return null;
  }

  @Override
  public List<AccountTransferResult> parseArray(JSONArray jsonArray) {
    return null;
  }
}
