package com.huobi.sdk.parser.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.sdk.parser.HuobiModelParser;
import com.huobi.sdk.model.account.AccountBalance;

import java.util.ArrayList;
import java.util.List;

public class AccountBalanceParser implements HuobiModelParser<AccountBalance> {

  @Override
  public AccountBalance parse(JSONObject json) {

    String subType = json.getString("subtype");
    if (subType == null) {
      subType = json.getString("symbol");
    }

    AccountBalance accountBalance = json.toJavaObject(AccountBalance.class);
    accountBalance.setType(json.getString("type"));
    accountBalance.setState(json.getString("state"));
    accountBalance.setSubType(subType);
    accountBalance.setList(new BalanceParser().parseArray(json.getJSONArray("list")));

    return accountBalance;
  }

  @Override
  public AccountBalance parse(JSONArray json) {
    return null;
  }

  @Override
  public List<AccountBalance> parseArray(JSONArray jsonArray) {

    if (jsonArray == null || jsonArray.size() <= 0) {
      return new ArrayList<>();
    }

    List<AccountBalance> list = new ArrayList<>();
    for (int i = 0; i < jsonArray.size(); i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i);
      list.add(parse(jsonObject));
    }

    return list;
  }
}
