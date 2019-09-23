package com.project.bean.system;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
/**
 * 系统设置
 */
@Table(name = "setting")
public class SettingBean {
	@Id
	@KeySql(dialect = IdentityDialect.MYSQL)
	private Integer setting_id;//主键
	private String setting_name;//设置名称
	private String setting_key;//设置标识
	private String setting_value;//设置值

	public String getSetting_key() {
		return setting_key;
	}

	public SettingBean setSetting_key(String setting_key) {
		this.setting_key = setting_key;
		return this;
	}

	public Integer getSetting_id() {
		return setting_id;
	}

	public SettingBean setSetting_id(Integer setting_id) {
		this.setting_id = setting_id;
		return this;
	}

	public String getSetting_name() {
		return setting_name;
	}

	public SettingBean setSetting_name(String setting_name) {
		this.setting_name = setting_name;
		return this;
	}

	public String getSetting_value() {
		return setting_value;
	}

	public SettingBean setSetting_value(String setting_value) {
		this.setting_value = setting_value;
		return this;
	}
}
