package com.huobi.sdk.parser.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.sdk.parser.HuobiModelParser;
import com.huobi.sdk.model.account.PointTransferResult;

import java.util.List;

public class PointTransferResultParser implements HuobiModelParser<PointTransferResult> {

  @Override
  public PointTransferResult parse(JSONObject json) {
    return json.toJavaObject(PointTransferResult.class);
  }

  @Override
  public PointTransferResult parse(JSONArray json) {
    return null;
  }

  @Override
  public List<PointTransferResult> parseArray(JSONArray jsonArray) {
    return null;
  }

}
