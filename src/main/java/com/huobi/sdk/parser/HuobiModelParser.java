package com.huobi.sdk.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface HuobiModelParser<T> {


  T parse(JSONObject json);

  T parse(JSONArray json);

  List<T> parseArray(JSONArray jsonArray);

}
