package com.project.bean.pay;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 缴费记录
 */
@Table(name = "pay_history")
public class PayHistoryBean {
	@Id
	@KeySql(dialect = IdentityDialect.MYSQL)
	private Integer pay_id;//主键
	private String order_no;//订单号
	private String trade_no;//交易流水号
	private Float pay_amount;//支付金额
	private String pay_way;//支付方式
	private String create_time;//支付时间
	private Integer institution_id;//机构id
	private String extra_data;//额外数据
	private String pay_state;//pending:等待支付 ok:支付成功 wait_audit:等待平台审核
	private String pay_type;//pay_student:学生缴费  pay_teacher:监考老师缴费
	private Integer less_price;//减免金额
	private Integer sum_less_price;//总减免金额
	private Float old_price;//原价
	@Transient
	private String pay_type_show;
	@Transient
	private String institution_name;//机构名称
	private String success_time;//确认交易时间

	public String getSuccess_time() {
		return success_time;
	}

	public PayHistoryBean setSuccess_time(String success_time) {
		this.success_time = success_time;
		return this;
	}

	public Integer getSum_less_price() {
		return sum_less_price;
	}

	public PayHistoryBean setSum_less_price(Integer sum_less_price) {
		this.sum_less_price = sum_less_price;
		return this;
	}

	public Float getOld_price() {
		return old_price;
	}

	public PayHistoryBean setOld_price(Float old_price) {
		this.old_price = old_price;
		return this;
	}

	public Integer getLess_price() {
		return less_price;
	}

	public PayHistoryBean setLess_price(Integer less_price) {
		this.less_price = less_price;
		return this;
	}

	public String getPay_type() {
		return pay_type;
	}

	public PayHistoryBean setPay_type(String pay_type) {
		this.pay_type = pay_type;
		this.pay_type_show="pay_student".equals(pay_type)?"学生缴费":"监考老师";
		return this;
	}

	public String getPay_type_show() {
		return pay_type_show;
	}

	public PayHistoryBean setPay_type_show(String pay_type_show) {
		this.pay_type_show = pay_type_show;
		return this;
	}

	public String getPay_state() {
		return pay_state;
	}

	public PayHistoryBean setPay_state(String pay_state) {
		this.pay_state = pay_state;
		return this;
	}

	public String getExtra_data() {
		return extra_data;
	}

	public PayHistoryBean setExtra_data(String extra_data) {
		this.extra_data = extra_data;
		return this;
	}

	public Integer getPay_id() {
		return pay_id;
	}

	public PayHistoryBean setPay_id(Integer pay_id) {
		this.pay_id = pay_id;
		return this;
	}

	public String getOrder_no() {
		return order_no;
	}

	public PayHistoryBean setOrder_no(String order_no) {
		this.order_no = order_no;
		return this;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public PayHistoryBean setTrade_no(String trade_no) {
		this.trade_no = trade_no;
		return this;
	}

	public Float getPay_amount() {
		return pay_amount;
	}

	public PayHistoryBean setPay_amount(Float pay_amount) {
		this.pay_amount = pay_amount;
		return this;
	}

	public String getPay_way() {
		return pay_way;
	}

	public PayHistoryBean setPay_way(String pay_way) {
		this.pay_way = pay_way;
		return this;
	}

	public String getCreate_time() {
		return create_time;
	}

	public PayHistoryBean setCreate_time(String create_time) {
		this.create_time = create_time;
		return this;
	}

	public Integer getInstitution_id() {
		return institution_id;
	}

	public PayHistoryBean setInstitution_id(Integer institution_id) {
		this.institution_id = institution_id;
		return this;
	}

	public String getInstitution_name() {
		return institution_name;
	}

	public PayHistoryBean setInstitution_name(String institution_name) {
		this.institution_name = institution_name;
		return this;
	}
}
