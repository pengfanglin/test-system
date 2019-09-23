package com.project.bean.pay;

import java.util.Map;

/**
 * 公共支付Bean
 */
public class CommonPayBean {
  private String order_no;//商户订单号(随机生成)
  private String trade_no;//支付宝交易流水号
  private String order_name;//用户支付时显示的支付名称
  private Float pay_amount;//支付金额
  private String pay_way;//支付方式(用户自定义)
  private String trade_type;//支付类型（平台定义）
  private String body;//介绍信息
  private String business_type;//业务类型
  private String openid;//微信openid,仅限公众号支付
  private Map<String,Object> metadata;//额外参数
  private Map<String,Object> extraData;//用户自定义参数

  public String getTrade_type() {
    return trade_type;
  }

  public CommonPayBean setTrade_type(String trade_type) {
    this.trade_type = trade_type;
    return this;
  }

  public String getOpenid() {
    return openid;
  }

  public CommonPayBean setOpenid(String openid) {
    this.openid = openid;
    return this;
  }

  public String getBusiness_type() {
    return business_type;
  }

  public CommonPayBean setBusiness_type(String business_type) {
    this.business_type = business_type;
    return this;
  }

  public String getTrade_no() {
    return trade_no;
  }

  public CommonPayBean setTrade_no(String trade_no) {
    this.trade_no = trade_no;
    return this;
  }

  public String getBody() {
    return body;
  }

  public CommonPayBean setBody(String body) {
    this.body = body;
    return this;
  }

  public String getOrder_no() {
    return order_no;
  }

  public CommonPayBean setOrder_no(String order_no) {
    this.order_no = order_no;
    return this;
  }

  public String getOrder_name() {
    return order_name;
  }

  public CommonPayBean setOrder_name(String order_name) {
    this.order_name = order_name;
    return this;
  }

  public Float getPay_amount() {
    return pay_amount;
  }

  public CommonPayBean setPay_amount(Float pay_amount) {
//    this.pay_amount = pay_amount;
		this.pay_amount=0.01f;
    return this;
  }

  public String getPay_way() {
    return pay_way;
  }

  public CommonPayBean setPay_way(String pay_way) {
    this.pay_way = pay_way;
    return this;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public CommonPayBean setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public Map<String, Object> getExtraData() {
    return extraData;
  }

  public CommonPayBean setExtraData(Map<String, Object> extraData) {
    this.extraData = extraData;
    return this;
  }
}
