package com.project.bean.pay;


import java.util.Map;

/**
 * 公共退款Bean
 */
public class CommonRefundBean {
  private String order_no;//商户订单号(随机生成)  商户订单号和平台流水号不能同时为空
  private String trade_no;//支付平台交易流水号
  private Float total_amount;//订单金额
  private Float refund_amount;//退款金额
  private String refund_way;//退款方式
  private String refund_no;//退款单号，要保证唯一性
  private String business_type;//业务类型
  private Map<String,Object> metadata;//额外参数
  private Map<String,Object> extraData;//用户自定义参数

  public Float getTotal_amount() {
    return total_amount;
  }

  public CommonRefundBean setTotal_amount(Float total_amount) {
    this.total_amount = total_amount;
    return this;
  }

  public String getBusiness_type() {
    return business_type;
  }

  public CommonRefundBean setBusiness_type(String business_type) {
    this.business_type = business_type;
    return this;
  }

  public String getRefund_no() {
    return refund_no;
  }

  public CommonRefundBean setRefund_no(String refund_no) {
    this.refund_no = refund_no;
    return this;
  }

  public String getOrder_no() {
    return order_no;
  }

  public CommonRefundBean setOrder_no(String order_no) {
    this.order_no = order_no;
    return this;
  }

  public String getTrade_no() {
    return trade_no;
  }

  public CommonRefundBean setTrade_no(String trade_no) {
    this.trade_no = trade_no;
    return this;
  }

  public Float getRefund_amount() {
    return refund_amount;
  }

  public CommonRefundBean setRefund_amount(Float refund_amount) {
    this.refund_amount = refund_amount;
    return this;
  }

  public String getRefund_way() {
    return refund_way;
  }

  public CommonRefundBean setRefund_way(String refund_way) {
    this.refund_way = refund_way;
    return this;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public CommonRefundBean setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public Map<String, Object> getExtraData() {
    return extraData;
  }

  public CommonRefundBean setExtraData(Map<String, Object> extraData) {
    this.extraData = extraData;
    return this;
  }
}
