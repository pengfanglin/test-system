package com.project.bean.system;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "pay_setting")
public class PaySettingBean {
	@Id
	@KeySql(dialect = IdentityDialect.MYSQL)
	private Integer id;//主键
	private String alipay_qr;//支付宝二维码
	private String wx_qr;//微信二维码
	private String bank_no;//银行卡号
	private String bank_open_name;//开户行
	private String bank_username;//开户人姓名

	public Integer getId() {
		return id;
	}

	public PaySettingBean setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getAlipay_qr() {
		return alipay_qr;
	}

	public PaySettingBean setAlipay_qr(String alipay_qr) {
		this.alipay_qr = alipay_qr;
		return this;
	}

	public String getWx_qr() {
		return wx_qr;
	}

	public PaySettingBean setWx_qr(String wx_qr) {
		this.wx_qr = wx_qr;
		return this;
	}

	public String getBank_no() {
		return bank_no;
	}

	public PaySettingBean setBank_no(String bank_no) {
		this.bank_no = bank_no;
		return this;
	}

	public String getBank_open_name() {
		return bank_open_name;
	}

	public PaySettingBean setBank_open_name(String bank_open_name) {
		this.bank_open_name = bank_open_name;
		return this;
	}

	public String getBank_username() {
		return bank_username;
	}

	public PaySettingBean setBank_username(String bank_username) {
		this.bank_username = bank_username;
		return this;
	}
}
