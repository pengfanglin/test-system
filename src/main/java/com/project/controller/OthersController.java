package com.project.controller;

import com.project.bean.others.*;
import com.project.service.OthersService;
import com.project.service.SystemService;
import com.project.utils.OthersUtils;
import com.project.utils.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/othersController/")
public class OthersController  {
	
	@Autowired
	OthersService othersService;
	@Autowired
	SystemService systemService;
	/**
	 * 获取html页面内容
	 */
	@PostMapping("getHtmlContent")
	public Ajax getHtmlContent(String url){
		String desc=OthersUtils.readHtml(url);
		int start=desc.indexOf("<content>");
		int end=desc.indexOf("</content>");
		if(start>0&&end>0){
			desc=desc.substring(start+9,end);
		}
		return Ajax.ok(desc);
	}
	/**
	 * 上传多个文件
	 */
	@PostMapping("uploadFiles")
	public Ajax uploadFiles(HttpServletRequest request){
		Map<String, Object> map = OthersUtils.dealWithFormHaveFiles(request);
		if ("0".equals(map.get("result"))) {
			return Ajax.error("上传失败");
		} else {
			return Ajax.ok(map.get("file"));
		}
	}
	/**
	 * 上传单个文件
	 */
	@PostMapping("uploadFile")
	public Ajax uploadFile(HttpServletRequest request){
		Map<String, Object> result=OthersUtils.uploadFileForm(request);
		if(result.get("state").equals("0")) {
			return Ajax.error("上传失败");
		}else {
			return Ajax.ok(result.get("file"));
		}
	}
	/**
	 * 发送验证码
	 */
	@PostMapping("sendCode")
	public Ajax sendCode(CodeBean codeBean,String institution_name){
		codeBean.setCode(OthersUtils.createRandom(6));
		String content="";
		switch (codeBean.getCode_type()){
			case "institution_forget_password":
				content="【中美贵州考级办】尊敬的"+institution_name+",您的验证码是"+codeBean.getCode();
		}
		othersService.insertCode(codeBean.setCode_desc(content));
		SmsUtils.sms(codeBean.getMobile(),content);
		return Ajax.ok("验证码已发送");
	}
}
