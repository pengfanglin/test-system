package com.project.bean.institution;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 机构级别
 */
@Table(name = "institution_level")
public class InstitutionLevelBean {
	@Id
	@KeySql(dialect = IdentityDialect.MYSQL)
	private Integer level_id;//主键
	private String level_name;//级别
	private Float price;//减免价格
	private String create_time;//创建时间

	public Integer getLevel_id() {
		return level_id;
	}

	public InstitutionLevelBean setLevel_id(Integer level_id) {
		this.level_id = level_id;
		return this;
	}

	public String getLevel_name() {
		return level_name;
	}

	public InstitutionLevelBean setLevel_name(String level_name) {
		this.level_name = level_name;
		return this;
	}

	public Float getPrice() {
		return price;
	}

	public InstitutionLevelBean setPrice(Float price) {
		this.price = price;
		return this;
	}

	public String getCreate_time() {
		return create_time;
	}

	public InstitutionLevelBean setCreate_time(String create_time) {
		this.create_time = create_time;
		return this;
	}
}
