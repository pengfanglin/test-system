package com.project.bean.institution;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 考场
 */
@Table(name = "exam_room")
public class ExamRoomBean {
	@Id
	@KeySql(dialect = IdentityDialect.MYSQL)
	private Integer room_id;//主键
	private Integer institution_id;//机构id
	@Transient
	private String institution_name;
	private String room_no;//考场编号
	private Integer exam_number;//考试人数
	private String name;//名称
	private String 	address;//地址
	private String start_time;//开始时间
	private String end_time;//结束时间
	private String batch_no;//报考批次号
	private String student_ids;//考生ids

	public String getStudent_ids() {
		return student_ids;
	}

	public ExamRoomBean setStudent_ids(String student_ids) {
		this.student_ids = student_ids;
		return this;
	}

	public String getBatch_no() {
		return batch_no;
	}

	public ExamRoomBean setBatch_no(String batch_no) {
		this.batch_no = batch_no;
		return this;
	}

	public String getInstitution_name() {
		return institution_name;
	}

	public ExamRoomBean setInstitution_name(String institution_name) {
		this.institution_name = institution_name;
		return this;
	}

	public Integer getInstitution_id() {
		return institution_id;
	}

	public ExamRoomBean setInstitution_id(Integer institution_id) {
		this.institution_id = institution_id;
		return this;
	}

	public String getRoom_no() {
		return room_no;
	}

	public ExamRoomBean setRoom_no(String room_no) {
		this.room_no = room_no;
		return this;
	}

	public Integer getExam_number() {
		return exam_number;
	}

	public ExamRoomBean setExam_number(Integer exam_number) {
		this.exam_number = exam_number;
		return this;
	}

	public Integer getRoom_id() {
		return room_id;
	}

	public ExamRoomBean setRoom_id(Integer room_id) {
		this.room_id = room_id;
		return this;
	}

	public String getName() {
		return name;
	}

	public ExamRoomBean setName(String name) {
		this.name = name;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public ExamRoomBean setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getStart_time() {
		return start_time;
	}

	public ExamRoomBean setStart_time(String start_time) {
		this.start_time = start_time;
		return this;
	}

	public String getEnd_time() {
		return end_time;
	}

	public ExamRoomBean setEnd_time(String end_time) {
		this.end_time = end_time;
		return this;
	}
}
