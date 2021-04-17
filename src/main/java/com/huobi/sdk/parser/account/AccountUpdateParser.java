package com.huobi.sdk.parser.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.sdk.parser.HuobiModelParser;
import com.huobi.sdk.model.account.AccountUpdate;

import java.util.List;

public class AccountUpdateParser implements HuobiModelParser<AccountUpdate> {

  @Override
  public AccountUpdate parse(JSONObject json) {
    AccountUpdate update = json.toJavaObject(AccountUpdate.class);
    return update;
  }

  @Override
  public AccountUpdate parse(JSONArray json) {
    return null;
  }

  @Override
  public List<AccountUpdate> parseArray(JSONArray jsonArray) {
    return null;
  }
}
