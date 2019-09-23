package com.project.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.project.bean.pay.CommonPayBean;
import com.project.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 第三方支付
 */
@Component
public class PayUtils {
	private static AlipayClient alipayClient;
	private static PayService payService;
	private static Logger logger = LoggerFactory.getLogger(PayUtils.class);
	//微信支付请求地址
	public static String WECHAT_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	//微信支付回调通知地址
	public static String WECHAT_PAY_NOTIFY = "http://exam.qubaotang.cn/api/payController/wechatPayNotify";
	//支付宝支付回调地址
	public static String ALIPAY_PAY_NOTIFY = "https://exam.qubaotang.cn/api/payController/alipayPayNotify";

	@Autowired
	public PayUtils(AlipayClient alipayClient, PayService payService) {
		PayUtils.alipayClient = alipayClient;
		PayUtils.payService = payService;
	}

	/**
	 * 根据支付类型，调用不同的支付接口
	 */
	public static Map<String, Object> pay(CommonPayBean commonPayBean) {
		if (commonPayBean.getPay_way() == null) {
			throw new RuntimeException("支付方式不存在");
		}
		if(commonPayBean.getPay_amount()<=0){
			throw new RuntimeException("支付金额必须大于0");
		}
		switch (commonPayBean.getPay_way()) {
			case "alipay_qr":
				return PayUtils.alipayPay(commonPayBean);
			case "wechat_qr":
				commonPayBean.setTrade_type("NATIVE");
				return PayUtils.wechatPay(commonPayBean);
			default:
				throw new RuntimeException("支付方式不存在");
		}
	}

	/**
	 * 微信支付
	 */
	private static Map<String, Object> wechatPay(CommonPayBean commonPayBean) {
		//请求参数
		Map<String, Object> params = new LinkedHashMap<>();
		//微信移动应用appid
		params.put("appid", ConfigUtils.wx_app_id);
		//商户号
		params.put("mch_id", ConfigUtils.wx_merchants_id);
		//支付金额(单位分)‘
		if(commonPayBean.getPay_amount()==null){
			throw new RuntimeException("支付金额不能为空!");
		}else{
			params.put("total_fee", OthersUtils.toInteger(commonPayBean.getPay_amount() * 100));
		}
		//商户订单号
		if(commonPayBean.getOrder_no()==null){
			throw new RuntimeException("订单号不能为空!");
		}else{
			params.put("out_trade_no", commonPayBean.getOrder_no());
			params.put("nonce_str", UUIDUtils.nextId());
		}
		//支付描述
		if(commonPayBean.getBody()==null){
			params.put("body", "师傅上门Pro-微信支付");
		}else{
			params.put("body", commonPayBean.getBody());
		}
		//支付终端ip
		params.put("spbill_create_ip", "120.79.95.51");
		//回调通知地址
		params.put("notify_url", WECHAT_PAY_NOTIFY);
		//设置支付类型
		if(commonPayBean.getTrade_type()==null){
			throw new RuntimeException("支付类型不能为空");
		}else{
			params.put("trade_type", commonPayBean.getTrade_type());
		}
		//业务类型
		if(commonPayBean.getBusiness_type()==null||commonPayBean.getBusiness_type().equals("")){
			throw new RuntimeException("业务类型不能为空");
		}else{
			if(commonPayBean.getExtraData()==null){
				Map<String,Object> extraData=new HashMap<>();
				commonPayBean.setExtraData(extraData);
			}
			commonPayBean.getExtraData().put("business_type",commonPayBean.getBusiness_type());
		}
		//如果额外数据不为空，将额外数据转成json放到微信参数中
		if (commonPayBean.getExtraData() != null) {
			params.put("attach", JSONUtils.objectToJson(commonPayBean.getExtraData()));
		}
		//对参数进行字典序排序(从小到大)
		List<String> keys = new ArrayList<>(params.keySet());
		//对key键值按字典升序排序
		Collections.sort(keys);
		Map<String, Object> sortParams = new LinkedHashMap<>();
		for (String key : keys) {
			sortParams.put(key, params.get(key));
		}
		//拼接需要签名的字符串
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : sortParams.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		//拼接商户的api_key
		sb.append("key=").append(ConfigUtils.wx_app_key);
		//对参数进行签名
		sortParams.put("sign", EncodeUtils.MD5Encode(sb.toString()).toUpperCase());
		String result = HttpUtils.wechatPostByXml(WECHAT_PAY_URL, sortParams);
		Map<String, Object> return_param = OthersUtils.xmlToMap(result);
		if ("SUCCESS".equals(return_param.get("return_code"))) {
			if ("SUCCESS".equals(return_param.get("result_code"))) {
				return_param.put("packageValue","Sign=WXPay");
				return_param.put("timestamp",(System.currentTimeMillis()/1000)+"");
				//对微信返回数据进行二次签名
				params.clear();
				params.put("appid",ConfigUtils.wx_app_id);
				params.put("partnerid",ConfigUtils.wx_merchants_id);
				params.put("prepayid",return_param.get("prepay_id"));
				params.put("package","Sign=WXPay");
				params.put("noncestr",return_param.get("nonce_str"));
				params.put("timestamp",return_param.get("timestamp"));
				//对参数进行字典序排序(从小到大)
				keys = new ArrayList<>(params.keySet());
				//对key键值按字典升序排序
				Collections.sort(keys);
				sortParams.clear();
				for (String key : keys) {
					sortParams.put(key, params.get(key));
				}
				//拼接需要签名的字符串
				sb=new StringBuilder();
				for (Map.Entry<String, Object> entry : sortParams.entrySet()) {
					sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
				}
				//拼接商户的api_key
				sb.append("key=").append(ConfigUtils.wx_app_key);
				//对参数进行签名
				return_param.put("sign", EncodeUtils.MD5Encode(sb.toString()).toUpperCase());
				return_param.put("qr_code",return_param.get("code_url"));
				return return_param;
			} else {
				logger.error(return_param.get("err_code_des").toString());
				throw new RuntimeException("微信支付请求失败");
			}
		} else {
			logger.error(return_param.get("return_msg").toString());
			throw new RuntimeException("连接微信支付服务失败");
		}
	}
	/**
	 * 支付宝支付
	 */
	public static Map<String, Object> alipayPay(CommonPayBean commonPayBean) {
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradePrecreateRequest payRequest = new AlipayTradePrecreateRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
		//对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body
		if(commonPayBean.getBody()!=null){
			model.setBody(commonPayBean.getBody());
		}
		// 商品的标题/交易标题/订单标题/订单关键字等
		if(commonPayBean.getOrder_name()==null){
			model.setSubject("师傅上门网络科技-支付宝支付");
		}else{
			model.setSubject(commonPayBean.getOrder_name());
		}
		// 商户订单号(自动生成)
		if(commonPayBean.getOrder_no()==null){
			throw new RuntimeException("订单号不能为空");
		}else{
			model.setOutTradeNo(commonPayBean.getOrder_no());
		}
		// 支付金额
		if(commonPayBean.getPay_amount()==null){
			throw new RuntimeException("支付金额不能为空");
		}else{
			model.setTotalAmount(commonPayBean.getPay_amount().toString());
		}
		//业务类型
		if(commonPayBean.getBusiness_type()==null||commonPayBean.getBusiness_type().equals("")){
			throw new RuntimeException("业务类型不能为空");
		}else{
			if(commonPayBean.getExtraData()==null){
				Map<String,Object> extraData=new HashMap<>();
				commonPayBean.setExtraData(extraData);
			}
			commonPayBean.getExtraData().put("business_type",commonPayBean.getBusiness_type());
		}
		//支付超时时间
		model.setQrCodeTimeoutExpress("15h");
		//支付参数
		payRequest.setBizModel(model);
		// 回调地址
		payRequest.setNotifyUrl(ALIPAY_PAY_NOTIFY);
		AlipayTradePrecreateResponse response = null;
		try {
			//发起支付
			response = alipayClient.execute(payRequest);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			throw new RuntimeException("连接支付宝服务失败【"+response.getMsg()+"】");
		}
		//发起支付成功，将生成的支付凭证返给app
		if (response.isSuccess()) {
			Map<String, Object> result = new HashMap<>();
			result.put("qr_code", response.getQrCode());
			return result;
		} else {
			throw new RuntimeException("生成支付凭据失败【"+response.getMsg()+"】");
		}
	}
}
