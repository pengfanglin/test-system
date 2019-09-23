package com.project.bean.institution;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 历史考生信息
 */
@Table(name = "student_history")
public class StudentHistoryBean {
  @Id
  @KeySql(dialect = IdentityDialect.MYSQL)
  private Integer student_id;//主键
  private String name;//姓名
  private String pinyin;//拼音
  private String id_card;//身份证号
  private String phone;//电话
  private String address;//地址
  private String sex;//性别
  private Integer age;//年龄
  private String email;//邮箱
  private String photo;//照片
  private String birthday;//出生日期
  private String profession;//专业
  private String zip_code;//邮编
  private Integer level;//级别
  private String nation;//民族
  private Integer institution_id;//机构id
  @Transient
  private String institution_name;//机构名称
  private String last_apply_time;//最后报名时间
  private String last_exam_name;//最后考试名称
  private String exam_no;//考试代码
  private String create_time;//创建时间
  private String country;//国籍
  private String batch_no;//报考批次号
  private String ticket_no;//准考证号
  private Integer pass_level;//已通过级别
  private String certificate_no;//证书编号
  @Transient
  private String exam_room_address;//考场地址
  private String note;

  public String getNote() {
    return note;
  }

  public StudentHistoryBean setNote(String note) {
    this.note = note;
    return this;
  }

  public String getExam_room_address() {
    return exam_room_address;
  }

  public StudentHistoryBean setExam_room_address(String exam_room_address) {
    this.exam_room_address = exam_room_address;
    return this;
  }

  public String getCertificate_no() {
    return certificate_no;
  }

  public StudentHistoryBean setCertificate_no(String certificate_no) {
    this.certificate_no = certificate_no;
    return this;
  }

  public String getTicket_no() {
    return ticket_no;
  }

  public StudentHistoryBean setTicket_no(String ticket_no) {
    this.ticket_no = ticket_no;
    return this;
  }

  public Integer getPass_level() {
    return pass_level;
  }

  public StudentHistoryBean setPass_level(Integer pass_level) {
    this.pass_level = pass_level;
    return this;
  }

  public String getBatch_no() {
    return batch_no;
  }

  public StudentHistoryBean setBatch_no(String batch_no) {
    this.batch_no = batch_no;
    return this;
  }

  public String getCountry() {
    return country;
  }

  public StudentHistoryBean setCountry(String country) {
    this.country = country;
    return this;
  }

  public String getExam_no() {
    return exam_no;
  }

  public StudentHistoryBean setExam_no(String exam_no) {
    this.exam_no = exam_no;
    return this;
  }

  public Integer getStudent_id() {
    return student_id;
  }

  public StudentHistoryBean setStudent_id(Integer student_id) {
    this.student_id = student_id;
    return this;
  }

  public String getName() {
    return name;
  }

  public StudentHistoryBean setName(String name) {
    this.name = name;
    return this;
  }

  public String getPinyin() {
    return pinyin;
  }

  public StudentHistoryBean setPinyin(String pinyin) {
    this.pinyin = pinyin;
    return this;
  }

  public String getId_card() {
    return id_card;
  }

  public StudentHistoryBean setId_card(String id_card) {
    this.id_card = id_card;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public StudentHistoryBean setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public StudentHistoryBean setAddress(String address) {
    this.address = address;
    return this;
  }

  public String getSex() {
    return sex;
  }

  public StudentHistoryBean setSex(String sex) {
    this.sex = sex;
    return this;
  }

  public Integer getAge() {
    return age;
  }

  public StudentHistoryBean setAge(Integer age) {
    this.age = age;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public StudentHistoryBean setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getPhoto() {
    return photo;
  }

  public StudentHistoryBean setPhoto(String photo) {
    this.photo = photo;
    return this;
  }

  public String getBirthday() {
    return birthday;
  }

  public StudentHistoryBean setBirthday(String birthday) {
    this.birthday = birthday;
    return this;
  }

  public String getProfession() {
    return profession;
  }

  public StudentHistoryBean setProfession(String profession) {
    this.profession = profession;
    return this;
  }

  public String getZip_code() {
    return zip_code;
  }

  public StudentHistoryBean setZip_code(String zip_code) {
    this.zip_code = zip_code;
    return this;
  }

  public Integer getLevel() {
    return level;
  }

  public StudentHistoryBean setLevel(Integer level) {
    this.level = level;
    return this;
  }

  public String getNation() {
    return nation;
  }

  public StudentHistoryBean setNation(String nation) {
    this.nation = nation;
    return this;
  }

  public Integer getInstitution_id() {
    return institution_id;
  }

  public StudentHistoryBean setInstitution_id(Integer institution_id) {
    this.institution_id = institution_id;
    return this;
  }

  public String getInstitution_name() {
    return institution_name;
  }

  public StudentHistoryBean setInstitution_name(String institution_name) {
    this.institution_name = institution_name;
    return this;
  }

  public String getLast_apply_time() {
    return last_apply_time;
  }

  public StudentHistoryBean setLast_apply_time(String last_apply_time) {
    this.last_apply_time = last_apply_time;
    return this;
  }

  public String getLast_exam_name() {
    return last_exam_name;
  }

  public StudentHistoryBean setLast_exam_name(String last_exam_name) {
    this.last_exam_name = last_exam_name;
    return this;
  }

  public String getCreate_time() {
    return create_time;
  }

  public StudentHistoryBean setCreate_time(String create_time) {
    this.create_time = create_time;
    return this;
  }
}
