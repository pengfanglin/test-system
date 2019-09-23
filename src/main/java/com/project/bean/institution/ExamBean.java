package com.project.bean.institution;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 考试科目
 */
@Table(name = "exam")
public class ExamBean {
	@Id
	@KeySql(dialect = IdentityDialect.MYSQL)
	private Integer exam_id;
	private String exam_name;
	private Float 	exam_price;
	private Integer level;
	private String create_time;

	public Integer getExam_id() {
		return exam_id;
	}

	public ExamBean setExam_id(Integer exam_id) {
		this.exam_id = exam_id;
		return this;
	}

	public String getExam_name() {
		return exam_name;
	}

	public ExamBean setExam_name(String exam_name) {
		this.exam_name = exam_name;
		return this;
	}

	public Float getExam_price() {
		return exam_price;
	}

	public ExamBean setExam_price(Float exam_price) {
		this.exam_price = exam_price;
		return this;
	}

	public Integer getLevel() {
		return level;
	}

	public ExamBean setLevel(Integer level) {
		this.level = level;
		return this;
	}

	public String getCreate_time() {
		return create_time;
	}

	public ExamBean setCreate_time(String create_time) {
		this.create_time = create_time;
		return this;
	}
}
