package com.project.bean.institution;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 报名申请
 */
@Table(name = "sign_up_apply")
public class SignUpApplyBean {
	@Id
	@KeySql(dialect = IdentityDialect.MYSQL)
	private Integer apply_id;//主键
	private Integer institution_id;//机构id
	private String start_time;//开始时间
	private String end_time;//结束时间
	private String create_time;//申请时间
	private String update_time;//操作时间
	private String state;//状态
	private String batch_no;//报名批次号
	@Transient
	private String state_show;
	@Transient
	private String institution_name;
	@Transient
	private String ids;

	public String getUpdate_time() {
		return update_time;
	}

	public SignUpApplyBean setUpdate_time(String update_time) {
		this.update_time = update_time;
		return this;
	}

	public String getBatch_no() {
		return batch_no;
	}

	public SignUpApplyBean setBatch_no(String batch_no) {
		this.batch_no = batch_no;
		return this;
	}

	public String getIds() {
		return ids;
	}

	public SignUpApplyBean setIds(String ids) {
		this.ids = ids;
		return this;
	}

	public String getInstitution_name() {
		return institution_name;
	}

	public SignUpApplyBean setInstitution_name(String institution_name) {
		this.institution_name = institution_name;
		return this;
	}

	public Integer getApply_id() {
		return apply_id;
	}

	public SignUpApplyBean setApply_id(Integer apply_id) {
		this.apply_id = apply_id;
		return this;
	}

	public Integer getInstitution_id() {
		return institution_id;
	}

	public SignUpApplyBean setInstitution_id(Integer institution_id) {
		this.institution_id = institution_id;
		return this;
	}

	public String getStart_time() {
		return start_time;
	}

	public SignUpApplyBean setStart_time(String start_time) {
		this.start_time = start_time;
		return this;
	}

	public String getEnd_time() {
		return end_time;
	}

	public SignUpApplyBean setEnd_time(String end_time) {
		this.end_time = end_time;
		return this;
	}

	public String getCreate_time() {
		return create_time;
	}

	public SignUpApplyBean setCreate_time(String create_time) {
		this.create_time = create_time;
		return this;
	}

	public String getState() {
		return state;
	}

	public SignUpApplyBean setState(String state) {
		this.state = state;
		this.state_show="wait_audit".equals(state)?"待审核":
			"accept".equals(state)?"通过":"拒绝";
		return this;
	}

	public String getState_show() {
		return state_show;
	}

	public SignUpApplyBean setState_show(String state_show) {
		this.state_show = state_show;
		return this;
	}
}
