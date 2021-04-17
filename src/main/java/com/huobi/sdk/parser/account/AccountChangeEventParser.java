package com.huobi.sdk.parser.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.sdk.parser.HuobiModelParser;
import com.huobi.sdk.model.account.AccountChangeEvent;

import java.util.List;

public class AccountChangeEventParser implements HuobiModelParser<AccountChangeEvent> {

  @Override
  public AccountChangeEvent parse(JSONObject json) {

    JSONObject data = json.getJSONObject("data");


    return AccountChangeEvent.builder()
        .event(data.getString("event"))
        .list(new AccountChangeParser().parseArray(data.getJSONArray("list")))
        .build();
  }

  @Override
  public AccountChangeEvent parse(JSONArray json) {
    return null;
  }


  @Override
  public List<AccountChangeEvent> parseArray(JSONArray jsonArray) {
    return null;
  }
}
