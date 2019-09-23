package com.project.bean.institution;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 考生信息
 */
@Table(name = "student")
public class StudentBean {
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
    private String state;//状态  wait_audit:待审核 pass:审核通过 refuse:拒绝
    private String is_allot;//是否分配考场
    private String is_pay;//是否缴费
    private String is_validate;//是否校验通过
    private String note;//备注
    @Transient
    private String is_validate_show;
    private String error_info;//错误原因
    @Transient
    private String state_show;
    @Transient
    private String is_allot_show;
    @Transient
    private String is_pay_show;
    @Transient
    private Float price;//缴费金额
    @Transient
    private String student_ids;
    private Integer room_id;//考场id
    @Transient
    private String room_no;//考场编号
    @Transient
    private String start_time;//开考时间
    @Transient
    public String end_time;//结束时间
    private String ticket_no;//准考证号
    private String batch_no;//报考批次号
    @Transient
    private String exam_address;//考试地点
    @Transient
    private String room_name;//考试地点
    private String teacher_name;//指导老师名称
    private String teacher_phone;//指导老师电话
    private String recommend_institution;//推荐机构

    public String getTeacher_name() {
        return teacher_name;
    }

    public StudentBean setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
        return this;
    }

    public String getTeacher_phone() {
        return teacher_phone;
    }

    public StudentBean setTeacher_phone(String teacher_phone) {
        this.teacher_phone = teacher_phone;
        return this;
    }

    public String getRecommend_institution() {
        return recommend_institution;
    }

    public StudentBean setRecommend_institution(String recommend_institution) {
        this.recommend_institution = recommend_institution;
        return this;
    }

    public String getNote() {
        return note;
    }

    public StudentBean setNote(String note) {
        this.note = note;
        return this;
    }

    public String getRoom_name() {
        return room_name;
    }

    public StudentBean setRoom_name(String room_name) {
        this.room_name = room_name;
        return this;
    }

    public String getEnd_time() {
        return end_time;
    }

    public StudentBean setEnd_time(String end_time) {
        this.end_time = end_time;
        return this;
    }

    public String getExam_address() {
        return exam_address;
    }

    public StudentBean setExam_address(String exam_address) {
        this.exam_address = exam_address;
        return this;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public StudentBean setBatch_no(String batch_no) {
        this.batch_no = batch_no;
        return this;
    }

    public String getTicket_no() {
        return ticket_no;
    }

    public StudentBean setTicket_no(String ticket_no) {
        this.ticket_no = ticket_no;
        return this;
    }

    public String getStart_time() {
        return start_time;
    }

    public StudentBean setStart_time(String start_time) {
        this.start_time = start_time;
        return this;
    }

    public String getRoom_no() {
        return room_no;
    }

    public StudentBean setRoom_no(String room_no) {
        this.room_no = room_no;
        return this;
    }

    public String getIs_validate_show() {
        return is_validate_show;
    }

    public StudentBean setIs_validate_show(String is_validate_show) {
        this.is_validate_show = is_validate_show;
        return this;
    }

    public String getError_info() {
        return error_info;
    }

    public StudentBean setError_info(String error_info) {
        this.error_info = error_info;
        return this;
    }

    public String getIs_validate() {
        return is_validate;
    }

    public StudentBean setIs_validate(String is_validate) {
        this.is_validate = is_validate;
        this.is_validate_show = "0".equals(is_validate) ? "未通过" : "通过";
        return this;
    }

    public String getStudent_ids() {
        return student_ids;
    }

    public StudentBean setStudent_ids(String student_ids) {
        this.student_ids = student_ids;
        return this;
    }

    public Integer getRoom_id() {
        return room_id;
    }

    public StudentBean setRoom_id(Integer room_id) {
        this.room_id = room_id;
        return this;
    }

    public Float getPrice() {
        return price;
    }

    public StudentBean setPrice(Float price) {
        this.price = price;
        return this;
    }

    public String getState_show() {
        return state_show;
    }

    public StudentBean setState_show(String state_show) {
        this.state_show = state_show;
        return this;
    }

    public String getIs_allot_show() {
        return is_allot_show;
    }

    public StudentBean setIs_allot_show(String is_allot_show) {
        this.is_allot_show = is_allot_show;
        return this;
    }

    public String getIs_pay_show() {
        return is_pay_show;
    }

    public StudentBean setIs_pay_show(String is_pay_show) {
        this.is_pay_show = is_pay_show;
        return this;
    }

    public String getIs_pay() {
        return is_pay;
    }

    public StudentBean setIs_pay(String is_pay) {
        this.is_pay = is_pay;
        this.is_pay_show = "-1".equals(is_pay) ? "已申请" :
                "0".equals(is_pay) ? "未缴费" : "已缴费";
        return this;
    }

    public String getIs_allot() {
        return is_allot;
    }

    public StudentBean setIs_allot(String is_allot) {
        this.is_allot = is_allot;
        this.is_allot_show = "0".equals(is_allot) ? "未分配" : "已分配";
        return this;
    }

    public String getState() {
        return state;
    }

    public StudentBean setState(String state) {
        this.state = state;
        this.state_show = "wait_audit".equals(state) ? "待审核" :
                "pass".equals(state) ? "通过" : "拒绝";
        return this;
    }

    public String getCountry() {
        return country;
    }

    public StudentBean setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getExam_no() {
        return exam_no;
    }

    public StudentBean setExam_no(String exam_no) {
        this.exam_no = exam_no;
        return this;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public StudentBean setStudent_id(Integer student_id) {
        this.student_id = student_id;
        return this;
    }

    public String getName() {
        return name;
    }

    public StudentBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getPinyin() {
        return pinyin;
    }

    public StudentBean setPinyin(String pinyin) {
        this.pinyin = pinyin;
        return this;
    }

    public String getId_card() {
        return id_card;
    }

    public StudentBean setId_card(String id_card) {
        this.id_card = id_card;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public StudentBean setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public StudentBean setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public StudentBean setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public StudentBean setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public StudentBean setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public StudentBean setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public StudentBean setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getProfession() {
        return profession;
    }

    public StudentBean setProfession(String profession) {
        this.profession = profession;
        return this;
    }

    public String getZip_code() {
        return zip_code;
    }

    public StudentBean setZip_code(String zip_code) {
        this.zip_code = zip_code;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public StudentBean setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public String getNation() {
        return nation;
    }

    public StudentBean setNation(String nation) {
        this.nation = nation;
        return this;
    }

    public Integer getInstitution_id() {
        return institution_id;
    }

    public StudentBean setInstitution_id(Integer institution_id) {
        this.institution_id = institution_id;
        return this;
    }

    public String getInstitution_name() {
        return institution_name;
    }

    public StudentBean setInstitution_name(String institution_name) {
        this.institution_name = institution_name;
        return this;
    }

    public String getLast_apply_time() {
        return last_apply_time;
    }

    public StudentBean setLast_apply_time(String last_apply_time) {
        this.last_apply_time = last_apply_time;
        return this;
    }

    public String getLast_exam_name() {
        return last_exam_name;
    }

    public StudentBean setLast_exam_name(String last_exam_name) {
        this.last_exam_name = last_exam_name;
        return this;
    }

    public String getCreate_time() {
        return create_time;
    }

    public StudentBean setCreate_time(String create_time) {
        this.create_time = create_time;
        return this;
    }
}
