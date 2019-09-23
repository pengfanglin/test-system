package com.project.bean.institution;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * 机构区域
 */
@Table(name = "area")
public class AreaBean {
	@Id
	@KeySql(dialect = IdentityDialect.MYSQL)
	private Integer area_id;//主键
	private String area_name;//区域名称
	private Float area_price;//区域价格
	private Integer sort;//权重
	private Integer parent_id;//父节点id
	private List<AreaBean> areaBeans;

	public List<AreaBean> getAreaBeans() {
		return areaBeans;
	}

	public AreaBean setAreaBeans(List<AreaBean> areaBeans) {
		this.areaBeans = areaBeans;
		return this;
	}

	public Integer getArea_id() {
		return area_id;
	}

	public AreaBean setArea_id(Integer area_id) {
		this.area_id = area_id;
		return this;
	}

	public String getArea_name() {
		return area_name;
	}

	public AreaBean setArea_name(String area_name) {
		this.area_name = area_name;
		return this;
	}

	public Float getArea_price() {
		return area_price;
	}

	public AreaBean setArea_price(Float area_price) {
		this.area_price = area_price;
		return this;
	}

	public Integer getSort() {
		return sort;
	}

	public AreaBean setSort(Integer sort) {
		this.sort = sort;
		return this;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public AreaBean setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
		return this;
	}
}
