package com.project.bean.system;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 系统账号
 */
@Table(name = "system_account")
public class SystemAccountBean {
	@Id
	@KeySql(dialect = IdentityDialect.MYSQL)
	private Integer account_id;//主键
	private String name;//账号名称
	private String username;//用户名
	private String password;//密码
	private String head_img;//头像
	private String email;//邮箱
	private String phone;//手机号
	@Transient
	private String system_old_password;//旧密码
	private String system_type;//system:平台 institution:机构
	@Transient
	private String system_type_show;
	@Transient
	private String module_ids;//模块ids
	private Integer institution_id;//所属机构id
	@Transient
	private String institution_name;//所属机构名称
	private String create_time;//创建时间
	private String is_disable;//是否禁用
	@Transient
	private String is_disable_show;
	@Transient
	private String is_sign_up;//是否可以报名
	@Transient
	private String is_pay;//是否可以缴费
	private String is_apply_pay;//是否已申请缴费

	public String getIs_apply_pay() {
		return is_apply_pay;
	}

	public SystemAccountBean setIs_apply_pay(String is_apply_pay) {
		this.is_apply_pay = is_apply_pay;
		return this;
	}

	public String getIs_pay() {
		return is_pay;
	}

	public SystemAccountBean setIs_pay(String is_pay) {
		this.is_pay = is_pay;
		return this;
	}

	public String getIs_sign_up() {
		return is_sign_up;
	}

	public SystemAccountBean setIs_sign_up(String is_sign_up) {
		this.is_sign_up = is_sign_up;
		return this;
	}

	public String getModule_ids() {
		return module_ids;
	}

	public SystemAccountBean setModule_ids(String module_ids) {
		this.module_ids = module_ids;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public SystemAccountBean setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public SystemAccountBean setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getIs_disable_show() {
		return is_disable_show;
	}

	public SystemAccountBean setIs_disable_show(String is_disable_show) {
		this.is_disable_show = is_disable_show;
		return this;
	}

	public Integer getInstitution_id() {
		return institution_id;
	}

	public SystemAccountBean setInstitution_id(Integer institution_id) {
		this.institution_id = institution_id;
		return this;
	}

	public String getInstitution_name() {
		return institution_name;
	}

	public SystemAccountBean setInstitution_name(String institution_name) {
		this.institution_name = institution_name;
		return this;
	}

	public Integer getAccount_id() {
		return account_id;
	}

	public SystemAccountBean setAccount_id(Integer account_id) {
		this.account_id = account_id;
		return this;
	}

	public String getName() {
		return name;
	}

	public SystemAccountBean setName(String name) {
		this.name = name;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public SystemAccountBean setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public SystemAccountBean setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getHead_img() {
		return head_img;
	}

	public SystemAccountBean setHead_img(String head_img) {
		this.head_img = head_img;
		return this;
	}

	public String getSystem_old_password() {
		return system_old_password;
	}

	public SystemAccountBean setSystem_old_password(String system_old_password) {
		this.system_old_password = system_old_password;
		return this;
	}

	public String getSystem_type() {
		return system_type;
	}

	public SystemAccountBean setSystem_type(String system_type) {
		this.system_type = system_type;
		this.system_type_show="0".equals(system_type)?"平台":
						"1".equals(system_type)?"机构":"未知";
		return this;
	}

	public String getSystem_type_show() {
		return system_type_show;
	}

	public SystemAccountBean setSystem_type_show(String system_type_show) {
		this.system_type_show = system_type_show;
		return this;
	}

	public String getCreate_time() {
		return create_time;
	}

	public SystemAccountBean setCreate_time(String create_time) {
		this.create_time = create_time;
		return this;
	}

	public String getIs_disable() {
		return is_disable;
	}

	public SystemAccountBean setIs_disable(String is_disable) {
		this.is_disable = is_disable;
		this.is_disable_show="0".equalsIgnoreCase(is_disable)?"正常":
			"1".equalsIgnoreCase(is_disable)?"禁用":"未知";
		return this;
	}
}
