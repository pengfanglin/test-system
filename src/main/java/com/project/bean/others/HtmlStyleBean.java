package com.project.bean.others;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * html模板
 * @author 方林
 *
 */
@Table(name = "html_style")
public class HtmlStyleBean {
	@Id
	@KeySql(dialect = IdentityDialect.MYSQL)
	private Integer style_id;//主键
	private String style_type;//模板类型
	private String style_desc;//模块内容
	private String create_time;
	public Integer getStyle_id() {
		return style_id;
	}
	public HtmlStyleBean setStyle_id(Integer style_id) {
		this.style_id = style_id;
		return this;
	}
	public String getStyle_type() {
		return style_type;
	}
	public HtmlStyleBean setStyle_type(String style_type) {
		this.style_type = style_type;
		return this;
	}
	public String getStyle_desc() {
		return style_desc;
	}
	public HtmlStyleBean setStyle_desc(String style_desc) {
		this.style_desc = style_desc;
		return this;
	}
	public String getCreate_time() {
		return create_time;
	}
	public HtmlStyleBean setCreate_time(String create_time) {
		this.create_time = create_time;
		return this;
	}
}
