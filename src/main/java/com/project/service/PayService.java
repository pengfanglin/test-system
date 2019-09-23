package com.project.service;

import com.project.bean.institution.ExamRoomBean;
import com.project.bean.institution.InstitutionBean;
import com.project.bean.institution.StudentBean;
import com.project.bean.pay.CommonPayBean;
import com.project.bean.pay.PayHistoryBean;
import com.project.dao.DaoFactory;
import com.project.utils.JSONUtils;
import com.project.utils.OthersUtils;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class PayService {

	private final static Logger logger = LoggerFactory.getLogger(PayService.class);
	@Autowired
	DaoFactory daoFactory;
	/**
	 * 支付成功后的业务处理
	 */
	@Transactional
	public boolean paySuccessHandler(CommonPayBean commonPayBean) {
		PayHistoryBean payHistoryBean=daoFactory.payHistoryDao.selectOne(new PayHistoryBean().setOrder_no(commonPayBean.getOrder_no()));
		if(payHistoryBean==null){
			throw new RuntimeException("支付记录不存在");
		}
		int num=0;
		switch (commonPayBean.getBusiness_type()){
			case "student_pay":{
				num=daoFactory.payHistoryDao.updateByPrimaryKeySelective(new PayHistoryBean().setPay_id(payHistoryBean.getPay_id()).setPay_state("ok"));
				if(num==0){
					throw new RuntimeException("修改支付状态失败");
				}
				return daoFactory.othersDao.payStudentSuccess(payHistoryBean.getExtra_data())>0;
			}
			case "teacher_pay":
				Map<String,Object> extraData=commonPayBean.getExtraData();
				String institution_student_ids=daoFactory.othersDao.getInstitutionStudentIds(new StudentBean().setInstitution_id(payHistoryBean.getInstitution_id()));
				if(OthersUtils.isEmpty(institution_student_ids)){
					throw new RuntimeException("请先缴费，缴费后再进行排考安排！");
				}
				String ids[]=institution_student_ids.split(",");
				int nowIndex=0;
				InstitutionBean institutionBean=daoFactory.institutionDao.selectByPrimaryKey(payHistoryBean.getInstitution_id());
				if(institutionBean==null){
					throw new RuntimeException("机构不存在");
				}
				List<ExamRoomBean> examRoomBeans=JSONUtils.jsonToObject(payHistoryBean.getExtra_data(),List.class, ExamRoomBean.class);
				if(examRoomBeans!=null&&examRoomBeans.size()>0){
					for(ExamRoomBean examRoomBean:examRoomBeans){
						List<String> final_ids=new LinkedList<>();
						for(int i=nowIndex;i<nowIndex+examRoomBean.getExam_number();i++){
							final_ids.add(ids[i]);
						}
						nowIndex+=examRoomBean.getExam_number();
						String student_ids= StringUtils.join(final_ids,',');
						num=daoFactory.examRoomDao.insertSelective(examRoomBean.setInstitution_id(payHistoryBean.getInstitution_id()).setBatch_no(institutionBean.getBatch_no()));
						if(num==0){
							throw new RuntimeException("考场信息录入失败");
						}
						//修改学生考场分配状态
						num=daoFactory.othersDao.updateStudentAllotState(new StudentBean().setStudent_ids(student_ids).setRoom_id(examRoomBean.getRoom_id()));
						if(num==0){
							throw new RuntimeException("考生状态修改失败");
						}
					}
				}
				return true;
		}
		return true;
	}

	/**
	 * 支付宝支付通知处理
	 */
	@Transactional
	public boolean alipayPayNotify(Map<String, String> params) {
		//支付成功
		if (params.get("trade_status").equals("TRADE_SUCCESS")) {
			CommonPayBean commonPayBean = new CommonPayBean();
			//生成支付的时候，后端生成的订单号
			commonPayBean
							//支付方式
							.setPay_way("alipay_app")
							.setOrder_no(params.get("out_trade_no"))
							//支付宝生成的交易流水号
							.setTrade_no(params.get("trade_no"))
							//支付金额
							.setPay_amount(Float.valueOf(params.get("total_amount")));
			//发起支付的时候传递给支付宝的额外参数
			if (params.get("passback_params") != null && !params.get("passback_params").equals("")) {
				Map<String, Object> extraData;
				try {
					extraData = JSONUtils.jsonToObject(URLDecoder.decode(params.get("passback_params"),"utf-8"), Map.class);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					throw new RuntimeException("参数解码失败");
				}
				commonPayBean.setBusiness_type(extraData.get("business_type").toString());
				commonPayBean.setExtraData(extraData);
			}
			//进行业务处理
			return this.paySuccessHandler(commonPayBean);
		}
		return true;
	}

	/**
	 * 微信支付通知处理
	 */
	@Transactional
	public Map<String, Object> wechatPayNotify(Map<String, Object> params) {
		//app端调起支付成功
		if (params.get("return_code").equals("SUCCESS")) {
			//支付成功
			if (params.get("result_code").equals("SUCCESS")) {
				CommonPayBean commonPayBean = new CommonPayBean();
				//生成支付的时候，后端生成的订单号
				commonPayBean
								//支付方式
								.setPay_way(params.get("pay_way").toString())
								.setOrder_no(params.get("out_trade_no").toString())
								//微信生成的交易流水号
								.setTrade_no(params.get("transaction_id").toString())
								//支付金额
								.setPay_amount(Float.valueOf(params.get("total_fee").toString())/100);
				//发起支付的时候传递给微信的额外参数
				if (params.get("attach") != null && !params.get("attach").equals("")) {
					Map<String, Object> extraData = JSONUtils.jsonToObject(params.get("attach").toString(), Map.class);
					commonPayBean.setBusiness_type(extraData.get("business_type").toString());
					commonPayBean.setExtraData(extraData);
				}
				//如果业务处理成功，向微信发送确认消息
				if (this.paySuccessHandler(commonPayBean)) {
					logger.error("订单号【" + params.get("out_trade_no") + "】支付成功，响应微信请求");
					Map<String, Object> result = new HashMap<>();
					result.put("return_code", "SUCCESS");
					result.put("return_msg", "OK");
					return result;
				}
			} else {
				//支付失败，打印错误信息
				String sb = "错误码:" + params.get("err_code") +
								"错误代码描述:" + params.get("err_code_des") +
								"交易类型:" + params.get("trade_type") +
								"付款银行:" + params.get("bank_type") +
								"支付金额:" + params.get("total_fee") +
								"微信支付订单号:" + params.get("transaction_id") +
								"商户订单号:" + params.get("out_trade_no") +
								"支付完成时间:" + params.get("time_end");
				logger.error(sb);
			}
		} else {
			//app端调起支付失败，打印错误信息
			logger.error(params.get("return_msg").toString());
		}
		return null;
	}

}
