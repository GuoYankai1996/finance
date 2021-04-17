package com.huobi.sdk.parser.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.sdk.parser.HuobiModelParser;
import com.huobi.sdk.model.account.AccountLedger;

import java.util.List;

public class AccountLedgerParser implements HuobiModelParser<AccountLedger> {

  @Override
  public AccountLedger parse(JSONObject json) {
    return null;
  }

  @Override
  public AccountLedger parse(JSONArray json) {
    return null;
  }

  @Override
  public List<AccountLedger> parseArray(JSONArray jsonArray) {
    return jsonArray.toJavaList(AccountLedger.class);
  }
}
