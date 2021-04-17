package com.huobi.sdk.parser.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.sdk.parser.HuobiModelParser;
import com.huobi.sdk.model.account.SubuserAggregateBalance;

import java.util.ArrayList;
import java.util.List;

public class SubuserAggregateBalanceParser implements HuobiModelParser<SubuserAggregateBalance> {

  @Override
  public SubuserAggregateBalance parse(JSONObject json) {
    SubuserAggregateBalance balance = json.toJavaObject(SubuserAggregateBalance.class);
    balance.setType(json.getString("type"));
    return balance;
  }

  @Override
  public SubuserAggregateBalance parse(JSONArray json) {
    return null;
  }

  @Override
  public List<SubuserAggregateBalance> parseArray(JSONArray jsonArray) {
    List<SubuserAggregateBalance> balanceList = new ArrayList<>(jsonArray.size());
    for (int i = 0; i < jsonArray.size(); i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i);
      balanceList.add(parse(jsonObject));
    }
    return balanceList;
  }
}
