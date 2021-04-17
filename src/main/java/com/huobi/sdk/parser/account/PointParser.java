package com.huobi.sdk.parser.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.sdk.parser.HuobiModelParser;
import com.huobi.sdk.model.account.Point;

import java.util.List;

public class PointParser implements HuobiModelParser<Point> {

  @Override
  public Point parse(JSONObject json) {
    return json.toJavaObject(Point.class);
  }

  @Override
  public Point parse(JSONArray json) {
    return null;
  }

  @Override
  public List<Point> parseArray(JSONArray jsonArray) {
    return null;
  }

}
