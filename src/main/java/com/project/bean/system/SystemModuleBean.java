package com.project.bean.system;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
/**
 * 系统模块
 */
@Table(name = "system_module")
public class SystemModuleBean implements Cloneable{
	@Id
	@KeySql(dialect = IdentityDialect.MYSQL)
	private Integer module_id;//主键
	private String module_name;//模块名称
	private String module_url;//模块请求路径
	private Integer parent_id;//父模块id
	private String sort;//权重
	private String create_time;//创建时间
	@Transient
	private List<SystemModuleBean> systemModuleBeans;


	public Integer getModule_id() {
		return module_id;
	}
	public SystemModuleBean setModule_id(Integer module_id) {
		this.module_id = module_id;
		return this;
	}
	public String getModule_name() {
		return module_name;
	}
	public SystemModuleBean setModule_name(String module_name) {
		this.module_name = module_name;
		return this;
	}
	public String getModule_url() {
		return module_url;
	}
	public SystemModuleBean setModule_url(String module_url) {
		this.module_url = module_url;
		return this;
	}
	public Integer getParent_id() {
		return parent_id;
	}
	public SystemModuleBean setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
		return this;
	}
	public String getSort() {
		return sort;
	}
	public SystemModuleBean setSort(String sort) {
		this.sort = sort;
		return this;
	}

	public String getCreate_time() {
		return create_time;
	}
	public SystemModuleBean setCreate_time(String create_time) {
		this.create_time = create_time;
		return this;
	}
	public List<SystemModuleBean> getSystemModuleBeans() {
		return systemModuleBeans;
	}
	public SystemModuleBean setSystemModuleBeans(List<SystemModuleBean> systemModuleBeans) {
		this.systemModuleBeans = systemModuleBeans;
		return this;
	}
}
