package com.huobi.sdk.parser.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.sdk.parser.HuobiModelParser;
import com.huobi.sdk.model.account.Balance;

import java.util.ArrayList;
import java.util.List;

public class BalanceParser implements HuobiModelParser<Balance> {

  @Override
  public Balance parse(JSONObject json) {
    Balance balance = json.toJavaObject(Balance.class);
    balance.setType(json.getString("type"));
    return balance;
  }

  @Override
  public Balance parse(JSONArray json) {
    return null;
  }

  @Override
  public List<Balance> parseArray(JSONArray jsonArray) {
    List<Balance> balanceList = new ArrayList<>(jsonArray.size());
    for (int i = 0; i < jsonArray.size(); i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i);
      balanceList.add(parse(jsonObject));
    }
    return balanceList;
  }
}
