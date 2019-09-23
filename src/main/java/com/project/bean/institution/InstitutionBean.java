package com.project.bean.institution;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 机构
 */
@Table(name = "institution")
public class InstitutionBean {
	@Id
	@KeySql(dialect = IdentityDialect.MYSQL)
	private Integer institution_id;//主键
	private String institution_name;//机构名称
	private String institution_no;//机构编号
	@Transient
	private String username;//用户名
	@Transient
	private String password;//密码
	private String contact;//联系人
	private String phone;//电话
	private Integer area_id;
	private String area_ids;
	@Transient
	private String area_name;//地区
	private String address;//地址
	private Integer sort;//权重
	private String create_time;//创建时间
	private String batch_no;//批次编号
	private Integer level_id;//级别id
	@Transient
	private String level_name;//级别
	private String is_disable;//是否禁用
	private Integer account_id;//账号id

	public String getBatch_no() {
		return batch_no;
	}

	public InstitutionBean setBatch_no(String batch_no) {
		this.batch_no = batch_no;
		return this;
	}

	public String getLevel_name() {
		return level_name;
	}

	public InstitutionBean setLevel_name(String level_name) {
		this.level_name = level_name;
		return this;
	}

	public Integer getInstitution_id() {
		return institution_id;
	}

	public InstitutionBean setInstitution_id(Integer institution_id) {
		this.institution_id = institution_id;
		return this;
	}

	public String getInstitution_name() {
		return institution_name;
	}

	public InstitutionBean setInstitution_name(String institution_name) {
		this.institution_name = institution_name;
		return this;
	}

	public String getInstitution_no() {
		return institution_no;
	}

	public InstitutionBean setInstitution_no(String institution_no) {
		this.institution_no = institution_no;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public InstitutionBean setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public InstitutionBean setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getContact() {
		return contact;
	}

	public InstitutionBean setContact(String contact) {
		this.contact = contact;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public InstitutionBean setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public Integer getArea_id() {
		return area_id;
	}

	public InstitutionBean setArea_id(Integer area_id) {
		this.area_id = area_id;
		return this;
	}

	public String getArea_ids() {
		return area_ids;
	}

	public InstitutionBean setArea_ids(String area_ids) {
		this.area_ids = area_ids;
		return this;
	}

	public String getArea_name() {
		return area_name;
	}

	public InstitutionBean setArea_name(String area_name) {
		this.area_name = area_name;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public InstitutionBean setAddress(String address) {
		this.address = address;
		return this;
	}

	public Integer getSort() {
		return sort;
	}

	public InstitutionBean setSort(Integer sort) {
		this.sort = sort;
		return this;
	}

	public String getCreate_time() {
		return create_time;
	}

	public InstitutionBean setCreate_time(String create_time) {
		this.create_time = create_time;
		return this;
	}

	public Integer getLevel_id() {
		return level_id;
	}

	public InstitutionBean setLevel_id(Integer level_id) {
		this.level_id = level_id;
		return this;
	}

	public String getIs_disable() {
		return is_disable;
	}

	public InstitutionBean setIs_disable(String is_disable) {
		this.is_disable = is_disable;
		return this;
	}

	public Integer getAccount_id() {
		return account_id;
	}

	public InstitutionBean setAccount_id(Integer account_id) {
		this.account_id = account_id;
		return this;
	}

}
