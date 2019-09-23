package com.project.controller;

import com.project.service.PayService;
import com.project.utils.JSONUtils;
import com.project.utils.LogUtils;
import com.project.utils.OthersUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/payController/")
public class PayController {
	@Autowired
	PayService payService;

	/**
	 * 支付宝支付回调
	 */
	@PostMapping("alipayPayNotify")
	public String alipayPayNotify(@RequestParam Map<String,String> params) {
		if(payService.alipayPayNotify(params)){
			return "success";
		}else{
			return "";
		}
	}
	/**
	 * 微信App支付回调
	 */
	@PostMapping("wechatPayNotify")
	public String wechatPayNotify(HttpServletRequest request) {
		Map<String, Object> params = OthersUtils.xmlToMap(OthersUtils.readDataFromRequest(request));
		params.put("pay_way","wechat_qr");
		//如果业务处理成功，则向微信发送确认信息
		Map<String,Object> result=payService.wechatPayNotify(params);
		if(result!=null){
			return OthersUtils.mapToXml(result);
		}else{
			return "";
		}
	}
}
