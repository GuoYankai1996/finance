package com.huobi.sdk.constant.enums;

import lombok.Getter;

@Getter
public enum ExchangeEnum {

  HUOBI("com"),

  ;
  private final String code;
  ExchangeEnum(String code) {
    this.code = code;
  }

}
