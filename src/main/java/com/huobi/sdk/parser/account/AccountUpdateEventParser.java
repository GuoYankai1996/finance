package com.huobi.sdk.parser.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.sdk.parser.HuobiModelParser;
import com.huobi.sdk.model.account.AccountUpdateEvent;

import java.util.List;

public class AccountUpdateEventParser implements HuobiModelParser<AccountUpdateEvent> {

  @Override
  public AccountUpdateEvent parse(JSONObject json) {

    JSONObject data = json.getJSONObject("data");
    return AccountUpdateEvent.builder()
        .action(json.getString("action"))
        .topic(json.getString("ch"))
        .accountUpdate(new AccountUpdateParser().parse(data))
        .build();
  }

  @Override
  public AccountUpdateEvent parse(JSONArray json) {
    return null;
  }


  @Override
  public List<AccountUpdateEvent> parseArray(JSONArray jsonArray) {
    return null;
  }
}
