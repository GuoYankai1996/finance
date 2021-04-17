package com.huobi.sdk.parser.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.sdk.parser.HuobiModelParser;
import com.huobi.sdk.model.account.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountParser implements HuobiModelParser<Account> {

  @Override
  public Account parse(JSONObject json) {
    Account account = json.toJavaObject(Account.class);
    account.setType(json.getString("type"));
    account.setState(json.getString("state"));

    return account;
  }

  @Override
  public Account parse(JSONArray json) {
    return null;
  }

  @Override
  public List<Account> parseArray(JSONArray jsonArray) {
    List<Account> accountList = new ArrayList<>(jsonArray.size());
    for (int i=0;i<jsonArray.size();i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i);
      accountList.add(parse(jsonObject));
    }

    return accountList;
  }
}
