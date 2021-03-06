package com.huobi.sdk.constant;

import com.huobi.sdk.constant.enums.ExchangeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HuobiOptions implements Options {
  //"https://api.btcgateway.pro"; 国内访问用这个测试
  @Builder.Default
  private String restHost ="https://api.hbdm.com";


  @Builder.Default
  private String websocketHost = "wss://api.huobi.pro";

  private String apiKey;

  private String secretKey;

  @Builder.Default
  private boolean websocketAutoConnect = true;

  @Override
  public String getApiKey() {
    return this.apiKey;
  }

  @Override
  public String getSecretKey() {
    return this.secretKey;
  }

  @Override
  public ExchangeEnum getExchange() {
    return ExchangeEnum.HUOBI;
  }

  @Override
  public String getRestHost() {
    return this.restHost;
  }

  @Override
  public String getWebSocketHost() {
    return this.websocketHost;
  }

  @Override
  public boolean isWebSocketAutoConnect() {
    return this.websocketAutoConnect;
  }

}
