package com.huobi.sdk.parser.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.sdk.parser.HuobiModelParser;
import com.huobi.sdk.utils.DataUtils;
import com.huobi.sdk.model.account.AccountReq;

import java.util.List;

public class AccountReqParser implements HuobiModelParser<AccountReq> {

  @Override
  public AccountReq parse(JSONObject json) {
    String dataKey = DataUtils.getDataKey(json);
    return AccountReq.builder()
        .topic(json.getString("topic"))
        .ts(json.getLong("ts"))
        .cid(json.getString("cid"))
        .balanceList(new AccountBalanceParser().parseArray(json.getJSONArray(dataKey)))
        .build();
  }

  @Override
  public AccountReq parse(JSONArray json) {
    return null;
  }

  @Override
  public List<AccountReq> parseArray(JSONArray jsonArray) {
    return null;
  }
}
